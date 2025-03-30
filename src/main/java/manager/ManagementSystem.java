package manager;

import exception.DuplicatePatientIDException;
import exception.UnloadedStorageException;
import miscellaneous.Ui;
import storage.Storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ManagementSystem {
    private final List<Appointment> appointments;
    private final List<Patient> patients;
    private final List<Prescription> prescriptions;

    public ManagementSystem(List<Patient> list) {
        assert list != null : "Patient list cannot be null";
        appointments = new ArrayList<>();
        patients = list;
        prescriptions = new ArrayList<>();
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Gets the list of prescriptions.
     * 
     * @return The list of prescriptions
     */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void addPatient(Patient patient) throws DuplicatePatientIDException, UnloadedStorageException {
        assert patient != null : "Patient cannot be null";
        assert patients != null : "Patient list cannot be null";

        for (Patient existingPatient : patients) {
            assert existingPatient != null : "Existing patient in list cannot be null";
            if (existingPatient.getId().equals(patient.getId())) {
                throw new DuplicatePatientIDException("Patient ID already exists!");
            }
        }
        patients.add(patient);
        Storage.savePatients(patients);
    }

    public Patient deletePatient(String nric) throws UnloadedStorageException {
        assert nric != null && !nric.isBlank() : "NRIC must not be null or blank";
        assert patients != null : "Patient list cannot be null";
        
        for (Patient patient : patients) {
            if (patient.getId().equals(nric)) {
                patients.remove(patient);
                Storage.savePatients(patients);
                return patient;
            }
        }
        return null;
    }

    //@@author dylancmznus
    public Patient viewPatient(String nric) {
        assert nric != null && !nric.isBlank() : "NRIC must not be null or blank";
        Patient matchedPatient = null;
        for (Patient patient : patients) {
            if (patient.getId().equals(nric)) {
                matchedPatient = patient;
                break;
            }
        }
        return matchedPatient;
    }

    //@@author jyukuan
    public void editPatient(String nric, String newName, String newDob, String newGender,
            String newAddress, String newPhone) {
        Patient patient = findPatientByNric(nric);
        if (patient == null) {
            System.out.println("Patient with NRIC " + nric + " not found.");
            return;
        }
        if (newName != null && !newName.isBlank()) {
            patient.setName(newName);
        }
        if (newDob != null && !newDob.isBlank()) {
            patient.setDob(newDob);
        }
        if (newGender != null && !newGender.isBlank()) {
            patient.setGender(newGender);
        }
        if (newAddress != null && !newAddress.isBlank()) {
            patient.setAddress(newAddress);
        }
        if (newPhone != null && !newPhone.isBlank()) {
            patient.setContactInfo(newPhone);
        }
        System.out.println("Patient with NRIC " + nric + " updated successfully.");
    }

    public void storeMedicalHistory(String name, String nric, String medHistory) {
        Patient existingPatient = findPatientByNric(nric);

        if (existingPatient == null) {
            existingPatient = new Patient(nric, name, "", "", "", "", null);
            patients.add(existingPatient);
            Ui.showLine();
            System.out.println("New patient " + name + " (NRIC: " + nric + ") created.");
        } else {
            Ui.showLine();
        }

        String[] historyEntries = medHistory.split(",\\s*");
        for (String entry : historyEntries) {
            if (!existingPatient.getMedicalHistory().contains(entry.trim())) {
                existingPatient.getMedicalHistory().add(entry.trim());
            }
        }
        System.out.println("Medical history added for " + name + " (NRIC: " + nric + ").");
        Ui.showLine();
    }

    public void viewMedicalHistoryByNric(String nric) {
        Patient foundPatients = findPatientByNric(nric.trim());

        if (foundPatients == null) {
            Ui.showLine();
            System.out.println("No patient/patients found with NRIC " + nric + ".");
        } else {
            Ui.showLine();
            Ui.showPatientHistory(foundPatients);
        }
    }

    public void viewMedicalHistoryByName(String name) {
        List<Patient> foundPatients = findPatientsByName(name.trim());

        Ui.showLine();

        if (foundPatients.isEmpty()) {
            System.out.println("No patients found with name '" + name + "'.");
            Ui.showLine();
        } else {
            System.out.println("Found " + foundPatients.size() + " patient(s) with name '" + name + "'");
            for (Patient p : foundPatients) {
                Ui.showPatientHistory(p);
            }
        }
    }

    public void editPatientHistory(String nric, String oldHistory, String newHistory) {
        Patient patient = findPatientByNric(nric);
        if (patient == null) {
            System.out.println("Patient with NRIC " + nric + " not found.");
            return;
        }
        List<String> histories = patient.getMedicalHistory();
        boolean foundOld = false;
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).equalsIgnoreCase(oldHistory.trim())) {
                histories.set(i, newHistory.trim());
                foundOld = true;
                System.out.println("Replaced old history \"" + oldHistory + "\" with \"" + newHistory + "\".");
                break;
            }
        }
        if (!foundOld) {
            System.out.println("Old history \"" + oldHistory + "\" not found for patient " + patient.getName());
        }
    }

    private Patient findPatientByNric(String nric) {
        String object = nric.trim().toUpperCase();
        for (Patient p : patients) {
            String patientId = p.getId().trim().toUpperCase();
            if (patientId.equals(object)) {
                return p;
            }
        }
        return null;
    }

    private List<Patient> findPatientsByName(String name) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) {
            if (p.getName().trim().equalsIgnoreCase(name)) {
                result.add(p);
            }
        }
        return result;
    }

    //@@author chwenyee
    public void addAppointment(Appointment appointment) throws IllegalArgumentException {
        assert appointment != null : "Appointment cannot be null";
        assert patients != null : "Patient list cannot be null";
        
        Patient patient = findPatientByNric(appointment.getNric());
        if (patient == null) {
            throw new IllegalArgumentException("Patient with NRIC: " + appointment.getNric() + " not found");
        }

        appointments.add(appointment);
        patient.addAppointment(appointment);
    }

    public Appointment deleteAppointment(String apptId) {
        assert apptId != null && !apptId.isBlank() : "Appointment ID cannot be null or blank";
        assert appointments != null : "Appointment list cannot be null";
        
        for (Appointment appointment : appointments) {
            if (appointment.getId().equalsIgnoreCase(apptId)) {
                appointments.remove(appointment);
                Patient patient = findPatientByNric(appointment.getNric());
                if (patient != null) {
                    patient.deleteAppointment(apptId);
                }
                return appointment;
            }
        }
        return null;
    }

    public List<Appointment> sortAppointmentsByDateTime(List<Appointment> appointments) {
        appointments.sort(Comparator.comparing(Appointment::getDateTime));
        return appointments;
    }

    public List<Appointment> sortAppointmentsById(List<Appointment> appointments) {
        appointments.sort(Comparator.comparing(Appointment::getId));
        return appointments;
    }

    //@@author dylancmznus
    public Appointment markAppointment(String apptId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equalsIgnoreCase(apptId)) {
                appointment.markAsDone();
                return appointment;
            }
        }
        return null;
    }

    public Appointment unmarkAppointment(String apptId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equalsIgnoreCase(apptId)) {
                appointment.unmarkAsDone();
                return appointment;
            }
        }
        return null;
    }

    public Appointment findAppointmentByNric(String nric) {
        for (Appointment appt : appointments) {
            if (appt.getNric().equals(nric)) {
                return appt;
            }
        }
        return null;
    }

    /**
     * Adds a prescription to the system.
     *
     * @param prescription The prescription to add
     * @throws IllegalArgumentException if the patient doesn't exist
     */
    public void addPrescription(Prescription prescription) throws IllegalArgumentException {
        assert prescription != null : "Prescription cannot be null";
        
        Patient patient = findPatientByNric(prescription.getPatientId());
        if (patient == null) {
            throw new IllegalArgumentException("Patient with NRIC: " + prescription.getPatientId() + " not found");
        }
        
        prescriptions.add(prescription);
    }

    /**
     * Finds a prescription by its ID.
     *
     * @param prescriptionId The ID of the prescription to find
     * @return The found prescription or null if not found
     */
    public Prescription findPrescriptionById(String prescriptionId) {
        assert prescriptionId != null && !prescriptionId.isBlank() : "Prescription ID cannot be null or blank";
        
        for (Prescription prescription : prescriptions) {
            if (prescription.getId().equals(prescriptionId)) {
                return prescription;
            }
        }
        return null;
    }

    /**
     * Gets all prescriptions for a specific patient.
     *
     * @param patientId The patient's ID
     * @return List of prescriptions for the patient
     */
    public List<Prescription> getPrescriptionsForPatient(String patientId) {
        assert patientId != null && !patientId.isBlank() : "Patient ID cannot be null or blank";
        
        List<Prescription> patientPrescriptions = new ArrayList<>();
        for (Prescription prescription : prescriptions) {
            if (prescription.getPatientId().equals(patientId)) {
                patientPrescriptions.add(prescription);
            }
        }
        return patientPrescriptions;
    }

}
