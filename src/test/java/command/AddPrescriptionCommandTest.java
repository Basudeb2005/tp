package command;

import manager.ManagementSystem;
import manager.Patient;
import manager.Prescription;
import miscellaneous.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddPrescriptionCommandTest {
    private ManagementSystem system;
    private Ui ui;
    private final String patientId = "S1234567A";

    @BeforeEach
    void setUp() {
        List<Patient> patients = new ArrayList<>();
        Patient patient = new Patient(patientId, "John Doe", "1990-01-01", "Male", 
                                      "123 Main St", "98765432", new ArrayList<>());
        patients.add(patient);
        system = new ManagementSystem(patients);
        ui = new Ui();
    }

    @Test
    void execute_validPrescription_success() throws Exception {
        // Arrange
        String[] args = {patientId, "Fever,Cough", "Paracetamol,Cough syrup", "Take medicine after meals"};
        AddPrescriptionCommand command = new AddPrescriptionCommand(args);

        // Act
        command.execute(system, ui);

        // Assert
        List<Prescription> prescriptions = system.getPrescriptions();
        assertEquals(1, prescriptions.size());
        Prescription prescription = prescriptions.get(0);
        
        assertEquals(patientId, prescription.getPatientId());
        assertEquals(2, prescription.getSymptoms().size());
        assertEquals("Fever", prescription.getSymptoms().get(0));
        assertEquals("Cough", prescription.getSymptoms().get(1));
        assertEquals(2, prescription.getMedicines().size());
        assertEquals("Paracetamol", prescription.getMedicines().get(0));
        assertEquals("Cough syrup", prescription.getMedicines().get(1));
        assertEquals("Take medicine after meals", prescription.getNotes());
        
        // Check ID format
        String expectedIdPattern = patientId + "-\\d+";
        assertTrue(prescription.getId().matches(expectedIdPattern), 
                "Prescription ID should be in format 'NRIC-SEQ'");
    }

    @Test
    void execute_nonExistentPatient_failsToCreate() throws Exception {
        // Arrange
        String nonExistentPatientId = "S9876543B"; // This patient doesn't exist in the system
        String[] args = {nonExistentPatientId, "Fever,Cough", "Paracetamol,Cough syrup", "Take medicine after meals"};
        AddPrescriptionCommand command = new AddPrescriptionCommand(args);

        // Act
        command.execute(system, ui);

        // Assert
        List<Prescription> prescriptions = system.getPrescriptions();
        assertEquals(0, prescriptions.size()); // This should be 0 if properly implemented
    }
} 