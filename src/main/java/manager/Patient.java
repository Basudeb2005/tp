package manager;

import java.util.ArrayList;
import java.util.List;

public class Patient {

    private String id;
    private String name;
    private String dob;
    private String contactInfo;
    private String gender;
    private String address;
    private final List<String> medicalHistory;
    private final List<Appointment> appointments;

    public Patient(String id, String name, String dob, String gender, String address,
                   String contactInfo, List<String> medicalHistory) {
        assert id != null && !id.isBlank() : "Patient ID cannot be null or blank";
        assert name != null && !name.isBlank() : "Patient name cannot be null or blank";
        assert dob != null : "Date of birth cannot be null";
        assert gender != null : "Gender cannot be null";
        assert address != null : "Address cannot be null";
        assert contactInfo != null : "Contact info cannot be null";
        assert medicalHistory != null : "Medical history list cannot be null";
        
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.contactInfo = contactInfo;
        this.medicalHistory = new ArrayList<>(medicalHistory);
        this.appointments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Gets the gender of the patient.
     *
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the address of the patient.
     *
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    public List<String> getMedicalHistory() {
        return medicalHistory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        assert appointment != null : "Appointment cannot be null";
        assert appointment.getNric().equals(this.id) : "Appointment NRIC must match patient ID";
        appointments.add(appointment);
    }

    public void deleteAppointment(String apptId) {
        assert apptId != null && !apptId.isBlank() : "Appointment ID cannot be null or blank";
        for (Appointment appt : appointments) {
            if (appt.getId().equals(apptId)) {
                appointments.remove(appt);
                break;
            }
        }
    }

    @Override
    public String toString() {
        String formattedMedicalHistory;
        if (medicalHistory.isEmpty()) {
            formattedMedicalHistory = "None";
        } else {
            formattedMedicalHistory = String.join(", ", medicalHistory);
        }

        String result = String.format(
                "Patient NRIC: %s\n"
                        + "Name: %s\n"
                        + "Date of Birth: %s\n"
                        + "Gender: %s\n"
                        + "Address: %s\n"
                        + "Contact: %s\n"
                        + "Medical History: %s",
                id, name, dob, gender, address, contactInfo, formattedMedicalHistory);

        if (appointments.isEmpty()) {
            result += "\nAppointments: None";
        } else {
            result += "\nAppointments:";
            for (Appointment appt : appointments) {
                result += String.format(
                        "\n- [%s][%s]: %s (%s)",
                        appt.getId(),
                        appt.getStatusIcon(),
                        appt.getDateTime().format(Appointment.OUTPUT_FORMAT),
                        appt.getDescription());
            }
        }
        return result;
    }

    public String toStringForListView() {
        String result = String.format(
                "Patient NRIC: %s\n   "
                        + "Name: %s\n   "
                        + "Date of Birth: %s\n   "
                        + "Gender: %s\n   "
                        + "Address: %s\n   "
                        + "Contact: %s",
                id, name, dob, gender, address, contactInfo);

        if (medicalHistory.isEmpty()) {
            result += "\n   Medical History: None";
        } else {
            result += "\n   Medical History:";
            for (String h : medicalHistory) {
                result += "\n   - " + h;
            }
        }

        if (appointments.isEmpty()) {
            result += "\n   Appointments: None";
        } else {
            result += "\n   Appointments:";
            for (Appointment appt : appointments) {
                result += String.format(
                        "\n   - [%s][%s]: %s (%s)",
                        appt.getId(),
                        appt.getStatusIcon(),
                        appt.getDateTime().format(Appointment.OUTPUT_FORMAT),
                        appt.getDescription());
            }
        }
        return result;
    }

    public String toFileFormat() {
        return this.id + "|" + this.name + "|" + this.dob + "|" + this.gender + "|"
                + this.address + "|" + this.contactInfo + "|" + this.medicalHistory;
    }
}
