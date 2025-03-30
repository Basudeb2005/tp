package command;

import exception.DuplicatePatientIDException;
import exception.UnloadedStorageException;
import manager.ManagementSystem;
import manager.Patient;
import manager.Prescription;
import miscellaneous.Ui;

import java.util.List;

/**
 * Represents a command to view all prescriptions for a patient.
 */
public class ViewAllPrescriptionsCommand extends Command {
    private final String patientId;
    
    /**
     * Constructs a new ViewAllPrescriptionsCommand.
     *
     * @param patientId The ID of the patient whose prescriptions to view
     */
    public ViewAllPrescriptionsCommand(String patientId) {
        this.patientId = patientId;
    }
    
    /**
     * Executes the view all prescriptions command.
     *
     * @param system The management system
     * @param ui The user interface
     */
    @Override
    public void execute(ManagementSystem system, Ui ui) throws DuplicatePatientIDException, UnloadedStorageException {
        ui.showLine();
        
        Patient patient = system.viewPatient(patientId);
        if (patient == null) {
            System.out.println("Patient with ID " + patientId + " not found.");
            ui.showLine();
            return;
        }
        
        List<Prescription> patientPrescriptions = system.getPrescriptionsForPatient(patientId);
        
        if (patientPrescriptions.isEmpty()) {
            System.out.println("No prescriptions found for patient " + patient.getName() + " (" + patientId + ").");
        } else {
            System.out.println("Prescriptions for patient " + patient.getName() + " (" + patientId + "):");
            System.out.println();
            
            for (Prescription prescription : patientPrescriptions) {
                System.out.println("Prescription ID: " + prescription.getId());
                System.out.println("Date: " + prescription.getDateTime().format(Prescription.DATETIME_FORMAT));
                
                System.out.println("Symptoms:");
                for (String symptom : prescription.getSymptoms()) {
                    System.out.println("- " + symptom);
                }
                
                System.out.println("Medicines:");
                for (String medicine : prescription.getMedicines()) {
                    System.out.println("- " + medicine);
                }
                
                System.out.println("Notes: " + (prescription.getNotes().isEmpty() ? "None" : prescription.getNotes()));
                System.out.println();
            }
            
            System.out.println("Total prescriptions: " + patientPrescriptions.size());
            System.out.println("Use 'view-prescription PRESCRIPTION_ID' to view details and generate HTML.");
        }
        
        ui.showLine();
    }
} 