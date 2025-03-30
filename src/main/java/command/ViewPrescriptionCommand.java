package command;

import exception.DuplicatePatientIDException;
import exception.UnloadedStorageException;
import manager.ManagementSystem;
import manager.Patient;
import manager.Prescription;
import miscellaneous.Ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents a command to view and generate an HTML file for a prescription.
 */
public class ViewPrescriptionCommand extends Command {
    private final String prescriptionId;
    
    /**
     * Constructs a new ViewPrescriptionCommand.
     *
     * @param prescriptionId The ID of the prescription to view
     */
    public ViewPrescriptionCommand(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
    
    /**
     * Executes the view prescription command.
     *
     * @param system The management system
     * @param ui The user interface
     */
    @Override
    public void execute(ManagementSystem system, Ui ui) throws DuplicatePatientIDException, UnloadedStorageException {
        ui.showLine();
        
        try {
            Prescription prescription = system.findPrescriptionById(prescriptionId);
            if (prescription == null) {
                System.out.println("Prescription with ID " + prescriptionId + " not found.");
                ui.showLine();
                return;
            }
            
            Patient patient = system.viewPatient(prescription.getPatientId());
            if (patient == null) {
                System.out.println("Patient with ID " + prescription.getPatientId() + " not found.");
                ui.showLine();
                return;
            }
            
            System.out.println("Prescription details:");
            System.out.println(prescription);
            
            // Generate HTML file
            String html = prescription.toHtml(patient);
            String folderPath = "prescriptions";
            String fileName = "prescription_" + prescription.getId() + ".html";
            
            try {
                // Create directory if it doesn't exist
                Path dirPath = Paths.get(folderPath);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }
                
                // Write the prescription HTML to file
                File file = new File(folderPath + File.separator + fileName);
                FileWriter writer = new FileWriter(file);
                writer.write(html);
                writer.close();
                
                System.out.println("\nPrescription HTML file generated at: " + file.getAbsolutePath());
                System.out.println("Open this file in a web browser to view and print the prescription.");
                
            } catch (IOException e) {
                System.out.println("Error generating prescription file: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        ui.showLine();
    }
} 