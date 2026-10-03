package es.uclm.iso2.sescam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RadiologyManagementServiceTest {

    @Test
    void shouldRegisterPatientAndScheduleAppointment() {
        RadiologyManagementService service = new RadiologyManagementService();

        Patient patient = service.registerPatient("P-001", "Ana García", "ana@example.com", "01020304A");
        Doctor doctor = service.registerDoctor("D-001", "Dr. Pérez", "perez@example.com", List.of("radiología"));

        StudyRequest request = service.createStudyRequest(
                patient.getId(),
                doctor.getId(),
                "RX-TORSO",
                "Tos persistente",
                Priority.URGENT,
                false,
                null);

        Appointment appointment = service.scheduleAppointment(
                patient.getId(),
                doctor.getId(),
                "RX-TORSO",
                LocalDateTime.of(2026, 11, 20, 10, 30),
                Priority.URGENT);

        assertNotNull(patient);
        assertNotNull(request);
        assertNotNull(appointment);
        assertEquals(AppointmentStatus.SCHEDULED, appointment.getStatus());
        assertEquals("P-001", appointment.getPatient().getId());
    }

    @Test
    void shouldCreateFollowUpRequestForPeriodicTests() {
        RadiologyManagementService service = new RadiologyManagementService();

        Patient patient = service.registerPatient("P-002", "Luis Gómez", "luis@example.com", "11121314B");
        Doctor doctor = service.registerDoctor("D-002", "Dr. López", "lopez@example.com", List.of("ecografía"));

        StudyRequest request = service.createStudyRequest(
                patient.getId(),
                doctor.getId(),
                "US-ABDO",
                "Dolor abdominal",
                Priority.PREFERENTE,
                true,
                Period.ofMonths(6));

        assertNotNull(request);
        assertEquals(true, request.isFollowUpRequired());
        assertEquals(Period.ofMonths(6), request.getFollowUpPeriod());
        assertEquals(1, service.getFollowUpRequests().size());
    }

    @Test
    void shouldConfirmAndCompleteAppointment() {
        RadiologyManagementService service = new RadiologyManagementService();

        service.registerPatient("P-003", "Marta Ruiz", "marta@example.com", "12131415C");
        service.registerDoctor("D-003", "Dr. Vidal", "vidal@example.com", List.of("tomografía"));

        Appointment scheduled = service.scheduleAppointment(
                "P-003",
                "D-003",
                "CT-CRANEAL",
                LocalDateTime.now().plusDays(2),
                Priority.URGENT);

        Appointment confirmed = service.confirmAppointment(scheduled.getId());
        assertEquals(AppointmentStatus.CONFIRMED, confirmed.getStatus());

        Appointment completed = service.completeAppointment(scheduled.getId());
        assertEquals(AppointmentStatus.COMPLETED, completed.getStatus());
    }

    @Test
    void shouldRejectDuplicatePatients() {
        RadiologyManagementService service = new RadiologyManagementService();

        service.registerPatient("P-004", "Pedro", "pedro@example.com", "99887766D");

        assertThrows(IllegalArgumentException.class, () ->
                service.registerPatient("P-004", "Pedro duplicado", "otros@example.com", "12345678Z"));
    }

    @Test
    void shouldReportMedicalFindings() {
        RadiologyManagementService service = new RadiologyManagementService();

        service.registerPatient("P-005", "Sara", "sara@example.com", "55544433E");
        service.registerDoctor("D-004", "Dr. Flores", "flores@example.com", List.of("RM"));

        Appointment appointment = service.scheduleAppointment(
                "P-005",
                "D-004",
                "MRI-COLUMNA",
                LocalDateTime.now().plusDays(5),
                Priority.PREFERENTE);

        MedicalReport report = service.createMedicalReport(
                "P-005",
                "D-004",
                appointment.getId(),
                "Sin hallazgos relevantes",
                "Continuar seguimiento anual");

        assertNotNull(report);
        assertEquals("P-005", report.getPatient().getId());
        assertFalse(report.getFindings().isBlank());
    }
}
