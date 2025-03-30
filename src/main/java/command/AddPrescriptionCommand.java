//@@author Basudeb2005
package command;

import exception.DuplicatePatientIDException;
import exception.UnloadedStorageException;
import manager.Prescription;
import manager.ManagementSystem;
import manager.Patient;
import miscellaneous.Ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a command to add a prescription.
 */
public class AddPrescriptionCommand extends Command {
    private final String patientId;
    private final List<String> symptoms;
    private final List<String> medicines;
    private final String notes;

    /**
     * Constructs an AddPrescriptionCommand with the given parameters.
     *
     * @param args Array containing [patientId, symptomsStr, medicinesStr, notes]
     */
    public AddPrescriptionCommand(String[] args) {
        this.patientId = args[0];
        
        // Parse symptoms with better handling of spaces
        String[] symptomArray = args[1].split(",");
        this.symptoms = new ArrayList<>();
        for (String symptom : symptomArray) {
            String trimmed = symptom.trim();
            if (!trimmed.isEmpty()) {
                this.symptoms.add(trimmed);
            }
        }
        
        // Parse medicines with better handling of spaces
        String[] medicineArray = args[2].split(",");
        this.medicines = new ArrayList<>();
        for (String medicine : medicineArray) {
            String trimmed = medicine.trim();
            if (!trimmed.isEmpty()) {
                this.medicines.add(trimmed);
            }
        }
        
        this.notes = args[3];
    }

    /**
     * Executes the add prescription command.
     *
     * @param system The management system
     * @param ui The user interface
     */
    @Override
    public void execute(ManagementSystem system, Ui ui) throws DuplicatePatientIDException, UnloadedStorageException {
        ui.showLine();
        
        try {
            Patient patient = system.viewPatient(patientId);
            if (patient == null) {
                System.out.println("Patient with ID " + patientId + " not found.");
                ui.showLine();
                return;
            }
            
            Prescription prescription = new Prescription(patientId, symptoms, medicines, notes);
            system.addPrescription(prescription);
            
            System.out.println("Successfully added prescription:");
            System.out.println(prescription);
            
            System.out.println("\nPrescription has been generated.");
            System.out.println("View the prescription for the patient with ID: " + patientId);
            System.out.println("and prescription ID: " + prescription.getId());
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        ui.showLine();
    }
}
//@@author 