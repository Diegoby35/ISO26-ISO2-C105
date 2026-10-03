package es.uclm.iso2.sescam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RadiologyManagementService {
    private final Map<String, Patient> patients = new HashMap<>();
    private final Map<String, Doctor> doctors = new HashMap<>();
    private final Map<String, AdministrativeStaff> administrativeStaff = new HashMap<>();
    private final Map<String, SystemAdministrator> administrators = new HashMap<>();
    private final Map<String, TestType> catalog = new LinkedHashMap<>();
    private final Map<String, Appointment> appointments = new LinkedHashMap<>();
    private final List<StudyRequest> requests = new ArrayList<>();
    private final List<MedicalReport> reports = new ArrayList<>();

    public RadiologyManagementService() {
        registerDefaultTestCatalog();
    }

    public Patient registerPatient(String id, String name, String email, String nationalHealthNumber) {
        if (patients.containsKey(id)) {
            throw new IllegalArgumentException("Patient already exists: " + id);
        }
        Patient patient = new Patient(id, name, email, nationalHealthNumber);
        patients.put(id, patient);
        return patient;
    }

    public Doctor registerDoctor(String id, String name, String email, List<String> specialties) {
        if (doctors.containsKey(id)) {
            throw new IllegalArgumentException("Doctor already exists: " + id);
        }
        Doctor doctor = new Doctor(id, name, email, specialties);
        doctors.put(id, doctor);
        return doctor;
    }

    public AdministrativeStaff registerAdministrativeStaff(String id, String name, String email) {
        if (administrativeStaff.containsKey(id)) {
            throw new IllegalArgumentException("Administrative staff already exists: " + id);
        }
        AdministrativeStaff staff = new AdministrativeStaff(id, name, email);
        administrativeStaff.put(id, staff);
        return staff;
    }

    public SystemAdministrator registerAdministrator(String id, String name, String email) {
        if (administrators.containsKey(id)) {
            throw new IllegalArgumentException("Administrator already exists: " + id);
        }
        SystemAdministrator administrator = new SystemAdministrator(id, name, email);
        administrators.put(id, administrator);
        return administrator;
    }

    public void addTestType(TestType testType) {
        Objects.requireNonNull(testType, "testType");
        catalog.put(testType.getCode(), testType);
    }

    public TestType getTestType(String code) {
        return catalog.get(code);
    }

    public List<TestType> getCatalog() {
        return Collections.unmodifiableList(new ArrayList<>(catalog.values()));
    }

    public StudyRequest createStudyRequest(
            String patientId,
            String doctorId,
            String testCode,
            String clinicalIndication,
            Priority priority,
            boolean followUpRequired,
            Period followUpPeriod) {
        Patient patient = requirePatient(patientId);
        Doctor doctor = requireDoctor(doctorId);
        TestType testType = requireTestType(testCode);

        StudyRequest request = new StudyRequest(
                "REQ-" + (requests.size() + 1),
                patient,
                doctor,
                testType,
                clinicalIndication,
                priority != null ? priority : testType.getDefaultPriority(),
                LocalDate.now(),
                followUpRequired,
                followUpPeriod);

        requests.add(request);
        return request;
    }

    public Appointment scheduleAppointment(String patientId, String doctorId, String testCode, LocalDateTime scheduledAt, Priority priority) {
        Patient patient = requirePatient(patientId);
        Doctor doctor = requireDoctor(doctorId);
        TestType testType = requireTestType(testCode);

        Appointment appointment = new Appointment(
                "APP-" + (appointments.size() + 1),
                patient,
                doctor,
                testType,
                scheduledAt,
                priority != null ? priority : testType.getDefaultPriority());

        appointments.put(appointment.getId(), appointment);
        return appointment;
    }

    public Appointment confirmAppointment(String appointmentId) {
        Appointment appointment = requireAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        return appointment;
    }

    public Appointment cancelAppointment(String appointmentId) {
        Appointment appointment = requireAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointment;
    }

    public Appointment completeAppointment(String appointmentId) {
        Appointment appointment = requireAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointment;
    }

    public MedicalReport createMedicalReport(String patientId, String doctorId, String appointmentId, String findings, String recommendations) {
        Patient patient = requirePatient(patientId);
        Doctor doctor = requireDoctor(doctorId);
        requireAppointment(appointmentId);

        MedicalReport report = new MedicalReport(
                "REP-" + (reports.size() + 1),
                patient,
                doctor,
                findings,
                recommendations,
                LocalDate.now());
        reports.add(report);
        return report;
    }

    public List<StudyRequest> getPendingRequestsForPatient(String patientId) {
        Patient patient = requirePatient(patientId);
        return requests.stream()
                .filter(request -> request.getPatient().getId().equals(patient.getId()))
                .toList();
    }

    public List<Appointment> getAppointmentsForPatient(String patientId) {
        Patient patient = requirePatient(patientId);
        return appointments.values().stream()
                .filter(appointment -> appointment.getPatient().getId().equals(patient.getId()))
                .toList();
    }

    public List<StudyRequest> getFollowUpRequests() {
        return requests.stream()
                .filter(StudyRequest::isFollowUpRequired)
                .toList();
    }

    public List<MedicalReport> getReportsForPatient(String patientId) {
        Patient patient = requirePatient(patientId);
        return reports.stream()
                .filter(report -> report.getPatient().getId().equals(patient.getId()))
                .toList();
    }

    public long getWaitingDaysForAppointment(String appointmentId) {
        Appointment appointment = requireAppointment(appointmentId);
        return appointment.getScheduledAt().toLocalDate().toEpochDay() - LocalDate.now().toEpochDay();
    }

    private void registerDefaultTestCatalog() {
        addTestType(new TestType(
                "RX-TORSO",
                "Radiografía de tórax",
                StudyModality.RADIOGRAPHY,
                "Sala RX-01",
                15,
                "Sin preparación especial",
                false,
                false,
                "Uso de protector radiológico",
                Priority.ORDINARIO,
                false));

        addTestType(new TestType(
                "US-ABDO",
                "Ecografía abdominal",
                StudyModality.ULTRASOUND,
                "Ecógrafo 02",
                30,
                "Ayuno de 6 horas",
                false,
                false,
                "No aplicar presión excesiva",
                Priority.PREFERENTE,
                true));

        addTestType(new TestType(
                "CT-CRANEAL",
                "Tomografía cerebral",
                StudyModality.CT,
                "TAC 01",
                25,
                "Sin preparación adicional",
                true,
                true,
                "Uso de contraste con control de alergias",
                Priority.URGENT,
                false));

        addTestType(new TestType(
                "MRI-COLUMNA",
                "Resonancia de columna",
                StudyModality.MRI,
                "RM 01",
                40,
                "Retirar metal y revisar implantes",
                false,
                true,
                "Control de compatibilidad con implantes",
                Priority.PREFERENTE,
                true));
    }

    private Patient requirePatient(String id) {
        if (!patients.containsKey(id)) {
            throw new IllegalArgumentException("Unknown patient: " + id);
        }
        return patients.get(id);
    }

    private Doctor requireDoctor(String id) {
        if (!doctors.containsKey(id)) {
            throw new IllegalArgumentException("Unknown doctor: " + id);
        }
        return doctors.get(id);
    }

    private TestType requireTestType(String code) {
        if (!catalog.containsKey(code)) {
            throw new IllegalArgumentException("Unknown test type: " + code);
        }
        return catalog.get(code);
    }

    private Appointment requireAppointment(String id) {
        if (!appointments.containsKey(id)) {
            throw new IllegalArgumentException("Unknown appointment: " + id);
        }
        return appointments.get(id);
    }
}
