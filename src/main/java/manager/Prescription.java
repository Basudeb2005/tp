package manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a prescription for a patient.
 */
public class Prescription {
    private static int prescriptionCount = 1;
    private final int sequenceNumber;
    private final String patientId;
    private final LocalDateTime dateTime;
    private final List<String> symptoms;
    private final List<String> medicines;
    private final String notes;
    
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Constructs a new Prescription object.
     *
     * @param patientId The ID of the patient
     * @param symptoms List of symptoms
     * @param medicines List of prescribed medicines
     * @param notes Additional notes for the prescription
     */
    public Prescription(String patientId, List<String> symptoms, List<String> medicines, String notes) {
        assert patientId != null && !patientId.isBlank() : "Patient ID cannot be null or blank";
        assert symptoms != null : "Symptoms list cannot be null";
        assert medicines != null : "Medicines list cannot be null";
        assert notes != null : "Notes cannot be null";
        
        this.sequenceNumber = prescriptionCount++;
        this.patientId = patientId;
        this.dateTime = LocalDateTime.now();
        this.symptoms = new ArrayList<>(symptoms);
        this.medicines = new ArrayList<>(medicines);
        this.notes = notes;
    }

    /**
     * Gets the prescription identifier by combining patient ID and sequence number.
     *
     * @return The prescription ID in format "NRIC-SEQ"
     */
    public String getId() {
        return patientId + "-" + sequenceNumber;
    }

    /**
     * Gets the sequence number of this prescription.
     *
     * @return The sequence number
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Gets the patient ID associated with this prescription.
     *
     * @return The patient ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Gets the date and time when the prescription was created.
     *
     * @return The date and time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Gets the symptoms recorded in this prescription.
     *
     * @return List of symptoms
     */
    public List<String> getSymptoms() {
        return symptoms;
    }

    /**
     * Gets the medicines prescribed.
     *
     * @return List of medicines
     */
    public List<String> getMedicines() {
        return medicines;
    }

    /**
     * Gets the additional notes for this prescription.
     *
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Generates an HTML string representation of the prescription for printing.
     *
     * @param patient The patient associated with this prescription
     * @return HTML formatted prescription
     */
    public String toHtml(Patient patient) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n<title>Prescription for ")
            .append(patient.getName())
            .append("</title>\n")
            .append("<style>\n")
            .append("body { font-family: Arial, sans-serif; margin: 40px; }\n")
            .append(".prescription { border: 1px solid #ccc; padding: 20px; max-width: 800px; margin: 0 auto; }\n")
            .append(".header { text-align: center; margin-bottom: 20px; }\n")
            .append(".header h1 { color: #2c3e50; }\n")
            .append(".patient-info { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 20px; }\n")
            .append(".patient-info div { padding: 5px; }\n")
            .append(".section { margin-top: 15px; border-top: 1px solid #eee; padding-top: 15px; }\n")
            .append(".section h3 { color: #3498db; }\n")
            .append("ul { padding-left: 20px; }\n")
            .append(".notes { background-color: #f9f9f9; padding: 10px; border-left: 3px solid #3498db; }\n")
            .append(".footer { margin-top: 30px; text-align: center; font-size: 0.9em; color: #7f8c8d; }\n")
            .append(".print-btn { display: block; margin: 20px auto; padding: 10px 20px; ")
            .append("background-color: #3498db; color: white; border: none; border-radius: 4px; cursor: pointer; }\n")
            .append("@media print { .print-btn { display: none; } }\n")
            .append("</style>\n")
            .append("<script>\n")
            .append("function printPrescription() {\n")
            .append("  window.print();\n")
            .append("}\n")
            .append("</script>\n")
            .append("</head>\n<body>\n")
            .append("<div class=\"prescription\">\n")
            .append("  <div class=\"header\">\n")
            .append("    <h1>Medical Prescription</h1>\n")
            .append("    <p>Prescription ID: ").append(getId()).append("</p>\n")
            .append("    <p>Date: ").append(dateTime.format(DATETIME_FORMAT)).append("</p>\n")
            .append("  </div>\n")
            .append("  <div class=\"patient-info\">\n")
            .append("    <div><strong>Patient NRIC:</strong> ").append(patient.getId()).append("</div>\n")
            .append("    <div><strong>Name:</strong> ").append(patient.getName()).append("</div>\n")
            .append("    <div><strong>Date of Birth:</strong> ").append(patient.getDob()).append("</div>\n")
            .append("    <div><strong>Gender:</strong> ").append(patient.getGender()).append("</div>\n")
            .append("    <div><strong>Contact:</strong> ").append(patient.getContactInfo()).append("</div>\n")
            .append("    <div><strong>Address:</strong> ").append(patient.getAddress()).append("</div>\n")
            .append("  </div>\n");

        // Symptoms section
        html.append("  <div class=\"section\">\n")
            .append("    <h3>Symptoms</h3>\n")
            .append("    <ul>\n");
        
        for (String symptom : symptoms) {
            html.append("      <li>").append(symptom).append("</li>\n");
        }
        
        html.append("    </ul>\n")
            .append("  </div>\n");

        // Medicines section
        html.append("  <div class=\"section\">\n")
            .append("    <h3>Prescribed Medicines</h3>\n")
            .append("    <ul>\n");
        
        for (String medicine : medicines) {
            html.append("      <li>").append(medicine).append("</li>\n");
        }
        
        html.append("    </ul>\n")
            .append("  </div>\n");

        // Notes section
        if (!notes.isEmpty()) {
            html.append("  <div class=\"section\">\n")
                .append("    <h3>Additional Notes</h3>\n")
                .append("    <div class=\"notes\">").append(notes).append("</div>\n")
                .append("  </div>\n");
        }

        html.append("  <div class=\"footer\">\n")
            .append("    <p>This prescription is valid for 30 days from the date of issue.</p>\n")
            .append("  </div>\n")
            .append("</div>\n")
            .append("<button class=\"print-btn\" onclick=\"printPrescription()\">Print Prescription</button>\n")
            .append("</body>\n</html>");

        return html.toString();
    }

    /**
     * Returns a string representation of the prescription.
     *
     * @return String representation of the prescription
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Prescription [").append(getId()).append("] (").append(dateTime.format(DATETIME_FORMAT)).append(")\n");
        result.append("Patient ID: ").append(patientId).append("\n");
        
        result.append("Symptoms: ");
        if (symptoms.isEmpty()) {
            result.append("None\n");
        } else {
            result.append("\n");
            for (String symptom : symptoms) {
                result.append("- ").append(symptom).append("\n");
            }
        }
        
        result.append("Medicines: ");
        if (medicines.isEmpty()) {
            result.append("None\n");
        } else {
            result.append("\n");
            for (String medicine : medicines) {
                result.append("- ").append(medicine).append("\n");
            }
        }
        
        result.append("Notes: ").append(notes.isEmpty() ? "None" : notes);
        
        return result.toString();
    }

    /**
     * Returns a string representation suitable for file storage.
     *
     * @return String formatted for file storage
     */
    public String toFileFormat() {
        return patientId + "|" + sequenceNumber + "|" + dateTime.format(DATETIME_FORMAT) + "|" 
                + String.join(",", symptoms) + "|" + String.join(",", medicines) + "|" + notes;
    }
} 