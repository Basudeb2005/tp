//@@author Basudeb2005
package manager;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrescriptionTest {

    @Test
    void constructor_validInput_success() {
        // Arrange
        String patientId = "S1234567A";
        List<String> symptoms = Arrays.asList("Fever", "Cough");
        List<String> medicines = Arrays.asList("Paracetamol", "Cough syrup");
        String notes = "Take medicine after meals";

        // Act
        Prescription prescription = new Prescription(patientId, symptoms, medicines, notes);

        // Assert
        assertEquals(patientId, prescription.getPatientId());
        assertEquals(symptoms, prescription.getSymptoms());
        assertEquals(medicines, prescription.getMedicines());
        assertEquals(notes, prescription.getNotes());
        String expectedFormat = patientId + "-" + prescription.getSequenceNumber();
        assertEquals(expectedFormat, prescription.getId());
        assertNotNull(prescription.getDateTime());
    }

    @Test
    void toHtml_validPatient_generatesCorrectHtml() {
        // Arrange
        String patientId = "S1234567A";
        String name = "John Doe";
        String dob = "1990-01-01";
        String gender = "Male";
        String address = "123 Main St";
        String contactInfo = "98765432";
        List<String> medicalHistory = new ArrayList<>();
        medicalHistory.add("Asthma");
        
        Patient patient = new Patient(patientId, name, dob, gender, address, contactInfo, medicalHistory);
        
        List<String> symptoms = Arrays.asList("Fever", "Cough");
        List<String> medicines = Arrays.asList("Paracetamol", "Cough syrup");
        String notes = "Take medicine after meals";
        
        Prescription prescription = new Prescription(patientId, symptoms, medicines, notes);
        
        // Act
        String html = prescription.toHtml(patient);
        
        // Assert
        assertNotNull(html);
        assertTrue(html.contains("<title>Prescription for John Doe</title>"));
        assertTrue(html.contains("<div><strong>Patient NRIC:</strong> S1234567A</div>"));
        assertTrue(html.contains("<div><strong>Name:</strong> John Doe</div>"));
        assertTrue(html.contains("<li>Fever</li>"));
        assertTrue(html.contains("<li>Paracetamol</li>"));
        assertTrue(html.contains("<div class=\"notes\">Take medicine after meals</div>"));
        assertTrue(html.contains("Prescription ID: " + prescription.getId()));
    }
}
//@@author 