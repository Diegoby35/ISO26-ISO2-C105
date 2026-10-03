package es.uclm.iso2.sescam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract class User {
    private final String id;
    private final String name;
    private final String email;

    protected User(String id, String name, String email) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.email = Objects.requireNonNull(email, "email");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

class Patient extends User {
    private final String nationalHealthNumber;

    public Patient(String id, String name, String email, String nationalHealthNumber) {
        super(id, name, email);
        this.nationalHealthNumber = Objects.requireNonNull(nationalHealthNumber, "nationalHealthNumber");
    }

    public String getNationalHealthNumber() {
        return nationalHealthNumber;
    }
}

class Doctor extends User {
    private final List<String> specialties;

    public Doctor(String id, String name, String email, List<String> specialties) {
        super(id, name, email);
        this.specialties = new ArrayList<>(specialties);
    }

    public List<String> getSpecialties() {
        return new ArrayList<>(specialties);
    }
}

class AdministrativeStaff extends User {
    public AdministrativeStaff(String id, String name, String email) {
        super(id, name, email);
    }
}

class SystemAdministrator extends User {
    public SystemAdministrator(String id, String name, String email) {
        super(id, name, email);
    }
}

class TestType {
    private final String code;
    private final String title;
    private final StudyModality modality;
    private final String equipment;
    private final int estimatedDurationMinutes;
    private final String preparation;
    private final boolean requiresContrast;
    private final boolean informedConsentRequired;
    private final String safetyRequirements;
    private final Priority defaultPriority;
    private final boolean periodicFollowUpPossible;

    public TestType(
            String code,
            String title,
            StudyModality modality,
            String equipment,
            int estimatedDurationMinutes,
            String preparation,
            boolean requiresContrast,
            boolean informedConsentRequired,
            String safetyRequirements,
            Priority defaultPriority,
            boolean periodicFollowUpPossible) {
        this.code = Objects.requireNonNull(code, "code");
        this.title = Objects.requireNonNull(title, "title");
        this.modality = Objects.requireNonNull(modality, "modality");
        this.equipment = Objects.requireNonNull(equipment, "equipment");
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.preparation = Objects.requireNonNull(preparation, "preparation");
        this.safetyRequirements = Objects.requireNonNull(safetyRequirements, "safetyRequirements");
        this.defaultPriority = Objects.requireNonNull(defaultPriority, "defaultPriority");
        this.requiresContrast = requiresContrast;
        this.informedConsentRequired = informedConsentRequired;
        this.periodicFollowUpPossible = periodicFollowUpPossible;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public StudyModality getModality() {
        return modality;
    }

    public String getEquipment() {
        return equipment;
    }

    public int getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }

    public String getPreparation() {
        return preparation;
    }

    public boolean isRequiresContrast() {
        return requiresContrast;
    }

    public boolean isInformedConsentRequired() {
        return informedConsentRequired;
    }

    public String getSafetyRequirements() {
        return safetyRequirements;
    }

    public Priority getDefaultPriority() {
        return defaultPriority;
    }

    public boolean isPeriodicFollowUpPossible() {
        return periodicFollowUpPossible;
    }
}

class StudyRequest {
    private final String id;
    private final Patient patient;
    private final Doctor doctor;
    private final TestType testType;
    private final String clinicalIndication;
    private final Priority priority;
    private final LocalDate requestedDate;
    private final boolean followUpRequired;
    private final Period followUpPeriod;

    public StudyRequest(
            String id,
            Patient patient,
            Doctor doctor,
            TestType testType,
            String clinicalIndication,
            Priority priority,
            LocalDate requestedDate,
            boolean followUpRequired,
            Period followUpPeriod) {
        this.id = Objects.requireNonNull(id, "id");
        this.patient = Objects.requireNonNull(patient, "patient");
        this.doctor = Objects.requireNonNull(doctor, "doctor");
        this.testType = Objects.requireNonNull(testType, "testType");
        this.clinicalIndication = Objects.requireNonNull(clinicalIndication, "clinicalIndication");
        this.priority = Objects.requireNonNull(priority, "priority");
        this.requestedDate = Objects.requireNonNull(requestedDate, "requestedDate");
        this.followUpRequired = followUpRequired;
        this.followUpPeriod = followUpPeriod;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public TestType getTestType() {
        return testType;
    }

    public String getClinicalIndication() {
        return clinicalIndication;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public boolean isFollowUpRequired() {
        return followUpRequired;
    }

    public Period getFollowUpPeriod() {
        return followUpPeriod;
    }
}

class Appointment {
    private final String id;
    private final Patient patient;
    private final Doctor doctor;
    private final TestType testType;
    private final LocalDateTime scheduledAt;
    private final Priority priority;
    private AppointmentStatus status;

    public Appointment(String id, Patient patient, Doctor doctor, TestType testType, LocalDateTime scheduledAt, Priority priority) {
        this.id = Objects.requireNonNull(id, "id");
        this.patient = Objects.requireNonNull(patient, "patient");
        this.doctor = Objects.requireNonNull(doctor, "doctor");
        this.testType = Objects.requireNonNull(testType, "testType");
        this.scheduledAt = Objects.requireNonNull(scheduledAt, "scheduledAt");
        this.priority = Objects.requireNonNull(priority, "priority");
        this.status = AppointmentStatus.SCHEDULED;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public TestType getTestType() {
        return testType;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public Priority getPriority() {
        return priority;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}

class MedicalReport {
    private final String id;
    private final Patient patient;
    private final Doctor doctor;
    private final String findings;
    private final String recommendations;
    private final LocalDate issuedAt;

    public MedicalReport(String id, Patient patient, Doctor doctor, String findings, String recommendations, LocalDate issuedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.patient = Objects.requireNonNull(patient, "patient");
        this.doctor = Objects.requireNonNull(doctor, "doctor");
        this.findings = Objects.requireNonNull(findings, "findings");
        this.recommendations = Objects.requireNonNull(recommendations, "recommendations");
        this.issuedAt = Objects.requireNonNull(issuedAt, "issuedAt");
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getFindings() {
        return findings;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public LocalDate getIssuedAt() {
        return issuedAt;
    }
}
