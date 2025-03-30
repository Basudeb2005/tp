package miscellaneous;


import command.AddPatientCommand;
import command.Command;
import command.DeletePatientCommand;
import command.ExitCommand;
import command.AddAppointmentCommand;
import command.DeleteAppointmentCommand;
import command.EditPatientCommand;
import command.ListAppointmentCommand;
import command.EditPatientHistoryCommand;
import command.ListPatientCommand;
import command.SortAppointmentCommand;
import command.StoreMedHistoryCommand;
import command.ViewPatientCommand;
import command.ViewMedHistoryCommand;
import command.MarkApppointmentCommand;
import command.UnmarkAppointmentCommand;
import command.FindAppointmentCommand;
import command.AddPrescriptionCommand;
import command.ViewPrescriptionCommand;
import command.ViewAllPrescriptionsCommand;
import exception.InvalidInputFormatException;
import exception.UnknownCommandException;
import manager.Appointment;
import manager.Patient;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static manager.Appointment.INPUT_FORMAT;

public class Parser {
    public static Command parse(String userInput) throws InvalidInputFormatException, UnknownCommandException {
        // Split into two parts to extract the command keyword and its detail
        String[] parts = userInput.split(" ", 2);
        String commandWord = parts[0].toLowerCase();

        switch (commandWord) {
        case "bye":
            return new ExitCommand();
        case "add-patient":
            return new AddPatientCommand(parseAddPatient(userInput));
        case "delete-patient":
            return new DeletePatientCommand(parseDeletePatient(userInput));
        case "view-patient":
            return new ViewPatientCommand(parseViewPatient(userInput));
        case "list-patient":
            return new ListPatientCommand();
        case "store-history":
            return new StoreMedHistoryCommand(parseStoreHistory(userInput));
        case "view-history":
            return new ViewMedHistoryCommand(parseViewHistory(userInput));
        case "add-appointment":
            return new AddAppointmentCommand(parseAddAppointment(userInput));
        case "delete-appointment":
            return new DeleteAppointmentCommand(parseDeleteAppointment(userInput));
        case "list-appointment":
            return new ListAppointmentCommand();
        case "sort-appointment":
            return new SortAppointmentCommand(parseSortAppointment(userInput));
        case "edit-patient":
            return new EditPatientCommand(parseEditPatient(userInput));
        case "edit-history":
            return new EditPatientHistoryCommand(parseEditHistory(userInput));
        case "mark-appointment":
            return new MarkApppointmentCommand(parseMarkAppointment(userInput));
        case "unmark-appointment":
            return new UnmarkAppointmentCommand(parseUnmarkAppointment(userInput));
        case "find-appointment":
            return new FindAppointmentCommand(parseFindAppointment(userInput));
        case "add-prescription":
            return new AddPrescriptionCommand(parseAddPrescription(userInput));
        case "view-prescription":
            return new ViewPrescriptionCommand(parseViewPrescription(userInput));
        case "view-all-prescriptions":
            return new ViewAllPrescriptionsCommand(parseViewAllPrescriptions(userInput));
        default:
            throw new UnknownCommandException("Unknown command. Please try again.");
        }
    }

    private static Patient parseAddPatient(String input) throws InvalidInputFormatException {
        String temp = input.replaceFirst("(?i)add-patient\\s*", "");
        String name = extractValue(temp, "n/");
        String nric = extractValue(temp, "ic/");
        String birthdate = extractValue(temp, "dob/");
        String gender = extractValue(temp, "g/");
        String phone = extractValue(temp, "p/");
        String address = extractValue(temp, "a/");
        String history = extractValue(temp, "h/");

        if (name == null || nric == null || birthdate == null || gender == null || phone == null || address == null) {
            throw new InvalidInputFormatException("Patient details are incomplete!" + System.lineSeparator()
                    + "Also, please use: add-patient n/NAME ic/NRIC dob/BIRTHDATE g/GENDER p/PHONE a/ADDRESS");
        }

        List<String> medHistory = new ArrayList<>();
        if (history != null && !history.trim().isEmpty()) {
            String[] entries = history.split(",\\s*");
            for (String entry : entries) {
                medHistory.add(entry.trim());
            }
        }

        return new Patient(nric.trim(), name.trim(), birthdate.trim(),
                gender.trim(), address.trim(), phone.trim(), medHistory);
    }

    private static String parseDeletePatient(String input) throws InvalidInputFormatException {
        if (input.length() < 15) {
            throw new InvalidInputFormatException("Invalid command format. Use: delete-patient NRIC");
        }

        String nric = input.substring(15).trim();
        return nric;
    }

    private static String parseViewPatient(String input) throws InvalidInputFormatException {
        if (input.length() < 13) {
            throw new InvalidInputFormatException("Invalid command format. Use: view-patient NRIC");
        }

        String nric = input.substring(13).trim(); // Extract and trim NRIC
        return nric;
    }

    public static String[] parseViewHistory(String input) throws InvalidInputFormatException {
        // Remove the command prefix "view-history" (case-insensitive) and get the remaining string.
        String temp = input.replaceFirst("(?i)view-history\\s*", "");
        String type;
        String nameOrIc;

        // Check if the remaining string starts with "ic/" or "n/" (case-insensitive).
        if (temp.toLowerCase().startsWith("ic/")) {
            type = "ic";
            // Extract the real content after "ic/" using extractValue(...)
            nameOrIc = extractValue(temp, "ic/");
        } else {
            // If there's no explicit prefix, try to detect NRIC vs. name.
            // Uses a simple regex matching a 9-character format: e.g., S1234567A
            if (temp.matches("^[A-Za-z]\\d{7}[A-Za-z]$")) {
                type = "ic";
                nameOrIc = temp.trim();
            } else {
                // Otherwise, assume it's a name
                type = "n";
                nameOrIc = temp.trim();
            }
        }

        // Return null if the parsed value is null or empty
        if (nameOrIc == null || nameOrIc.isEmpty()) {
            throw new InvalidInputFormatException("Invalid format. Please use: view-history NRIC or view-history NAME");
        }

        // Return the result as [type, value]
        return new String[]{type, nameOrIc};
    }

    public static String[] parseStoreHistory(String input) throws InvalidInputFormatException {
        // Remove the command prefix "store-history" (case-insensitive)
        // and get the remaining string.
        String temp = input.replaceFirst("(?i)store-history\\s*", "");

        // Extract n/NAME, ic/NRIC, and h/MEDICAL_HISTORY from the remaining string
        String name = extractValue(temp, "n/");
        String nric = extractValue(temp, "ic/");
        String medHistory = extractValue(temp, "h/");

        // If any part is missing, return null to indicate a parse failure
        if (name == null || nric == null || medHistory == null) {
            throw new InvalidInputFormatException("Invalid format. " +
                    "Please use: store-history n/NAME ic/NRIC h/MEDICAL_HISTORY");
        }

        // Return the trimmed values as an array
        return new String[]{name.trim(), nric.trim(), medHistory.trim()};
    }

    public static Appointment parseAddAppointment(String input) throws InvalidInputFormatException {
        String temp = input.replaceFirst("(?i)add-appointment\\s+", "");
        String nric = extractValue(temp, "ic/");
        String date = extractValue(temp, "dt/");
        String time = extractValue(temp, "t/");
        String desc = extractValue(temp, "dsc/");

        if (nric == null || date == null || time == null || desc == null) {
            String msg = "Missing details or wrong format for add-appointment!" + System.lineSeparator()
                    + "Please use: add-appointment ic/NRIC dt/DATE t/TIME dsc/DESCRIPTION";
            throw new InvalidInputFormatException(msg);
        }

        try {
            String combined = date.trim() + " " + time.trim();
            LocalDateTime dateTime = LocalDateTime.parse(combined, INPUT_FORMAT);
            return new Appointment(nric.trim(), dateTime, desc.trim());
        } catch (DateTimeParseException e) {
            throw new InvalidInputFormatException("Invalid date/time format. Please use: dt/yyyy-MM-dd and t/HHmm");
        }
    }

    public static String parseDeleteAppointment(String input) throws InvalidInputFormatException {
        if (!input.matches("(?i)delete-appointment\\s+A\\d+")) {
            throw new InvalidInputFormatException("Invalid format! Please use: " +
                    "delete-appointment APPOINTMENT_ID");
        }

        String apptId = input.replaceFirst("(?i)delete-appointment\\s*", "").trim();
        return apptId;
    }

    private static String parseSortAppointment(String input) throws InvalidInputFormatException {
        String temp = input.replaceFirst("(?i)sort-appointment\\s*", "");

        switch (temp.toLowerCase()) {
        case "bydate":
            return "date";
        case "byid":
            return "id";
        default:
            throw new InvalidInputFormatException("Invalid format! Please use: 'sort-appointment byDate' or " +
                    "'sort-appointment byId' (case-insensitive).");
        }
    }

    private static String parseMarkAppointment(String input) throws InvalidInputFormatException {
        String apptId = input.replaceFirst("(?i)mark-appointment\\s*", "").trim();
        if (apptId.isEmpty()) {
            throw new InvalidInputFormatException("Invalid format! Use: mark-appointment APPOINTMENT_ID");
        }
        return apptId;
    }

    private static String parseUnmarkAppointment(String input) throws InvalidInputFormatException {
        String apptId = input.replaceFirst("(?i)unmark-appointment\\s*", "").trim();
        if (apptId.isEmpty()) {
            throw new InvalidInputFormatException("Invalid format! Use: unmark-appointment APPOINTMENT_ID");
        }
        return apptId;
    }

    private static String parseFindAppointment(String input) throws InvalidInputFormatException {
        String patientId = input.replaceFirst("(?i)find-appointment\\s*", "").trim();
        if (patientId.isEmpty()) {
            throw new InvalidInputFormatException("Invalid format! Use: find-appointment PATIENT_NRIC");
        }
        return patientId;
    }

    private static String extractValue(String input, String prefix) {
        assert prefix != null : "Prefix cannot be null";

        String lowerInput = input.toLowerCase();
        String lowerPrefix = prefix.toLowerCase();
        int start = -1;

        // Find the first occurrence of the prefix that is either at the start or come before blank space
        // Ensure checks are not done at where the prefix can't fully fit
        for (int i = 0; i <= lowerInput.length() - lowerPrefix.length(); i++) {
            boolean isParamPrefixMatch = lowerInput.startsWith(lowerPrefix, i);
            // Check if the character before the prefix is blank space in input to have a valid input format
            boolean isParamAtValidPosition = (i == 0) || Character.isWhitespace(input.charAt(i - 1));
            if (isParamPrefixMatch && isParamAtValidPosition) {
                start = i;
                break;
            }
        }

        if (start < 0) {
            return null;
        }

        start += prefix.length();
        // Add prescription-specific prefixes s/, m/, and nt/ to the list of possible prefixes
        String[] possible = {"n/", "ic/", "dob/", "g/", "p/", "a/", "dt/", "t/", "dsc/", "h/", "old/", "new/", "s/", "m/", "nt/"};
        int end = input.length();

        // Determine where the current parameter's detail ends by finding the start of the next parameter
        for (String p : possible) {
            if (p.equalsIgnoreCase(prefix)) {
                continue;
            }
            String lowerP = p.toLowerCase();
            // Find the next occurrence of p that is either at the start or come before blank space
            for (int i = start; i <= lowerInput.length() - lowerP.length(); i++) {
                boolean isNextParamPrefixMatch = lowerInput.startsWith(lowerP, i);
                // Check if the character before the prefix is blank space in input to have a valid input format
                boolean isNextParamAtValidPosition = (i == 0) || Character.isWhitespace(input.charAt(i - 1));
                if (isNextParamPrefixMatch && isNextParamAtValidPosition) {
                    if (i < end) {
                        end = i;
                    }
                    break;
                }
            }
        }

        String detail = input.substring(start, end).trim();
        return detail.isEmpty() ? null : detail;
    }

    private static String[] parseEditPatient(String input) throws InvalidInputFormatException {
        String temp = input.replaceFirst("(?i)edit-patient\\s*", "");
        String nric = extractValue(temp, "ic/");
        if (nric == null) {
            throw new InvalidInputFormatException("Missing NRIC! Use: edit-patient ic/NRIC [n/NAME] " +
                    "[dob/BIRTHDATE] [g/GENDER] [a/ADDRESS] [p/PHONE]");
        }
        String name = extractValue(temp, "n/");
        String dob = extractValue(temp, "dob/");
        String gender = extractValue(temp, "g/");
        String address = extractValue(temp, "a/");
        String phone = extractValue(temp, "p/");

        return new String[]{nric, name, dob, gender, address, phone};
    }

    private static String[] parseEditHistory(String input) throws InvalidInputFormatException {
        String temp = input.replaceFirst("(?i)edit-history\\s*", "");

        String nric = extractValue(temp, "ic/");
        if (nric == null) {
            throw new InvalidInputFormatException("Missing NRIC! Use: edit-history ic/NRIC " +
                    "old/OLD_HISTORY new/NEW_HISTORY");
        }

        String oldHistory = extractValue(temp, "old/");
        String newHistory = extractValue(temp, "new/");

        if (oldHistory == null || newHistory == null) {
            throw new InvalidInputFormatException("Missing old or new history text! Use: edit-history " +
                    "ic/NRIC old/OLD_TEXT new/NEW_TEXT");
        }

        return new String[]{nric, oldHistory, newHistory};
    }

    public static Patient parsePatient(String line) {
        String[] tokens = line.split("\\|");
        if (tokens.length < 7) {
            return null;
        }

        String id = tokens[0];
        String name = tokens[1];
        String dob = tokens[2];
        String gender = tokens[3];
        String address = tokens[4];
        String contact = tokens[5];
        List<String> medHistory = Arrays.asList(tokens[6].split(","));

        return new Patient(id, name, dob, gender, address, contact, medHistory);
    }

    /**
     * Parses the input string for an add prescription command.
     *
     * @param input The input string
     * @return String array containing [patientId, symptomsStr, medicinesStr, notes]
     * @throws InvalidInputFormatException if the input format is invalid
     */
    private static String[] parseAddPrescription(String input) throws InvalidInputFormatException {
        String temp = input.replaceFirst("(?i)add-prescription\\s*", "");
        String patientId = extractValue(temp, "ic/");
        String symptomsStr = extractValue(temp, "s/");
        String medicinesStr = extractValue(temp, "m/");
        String notes = extractValue(temp, "nt/");

        if (patientId == null || symptomsStr == null || medicinesStr == null) {
            throw new InvalidInputFormatException("Prescription details are incomplete!" + System.lineSeparator()
                    + "Please use: add-prescription ic/NRIC s/SYMPTOMS m/MEDICINES [nt/NOTES]");
        }

        // If notes is null, set it to empty string
        if (notes == null) {
            notes = "";
        }

        return new String[]{patientId.trim(), symptomsStr.trim(), medicinesStr.trim(), notes.trim()};
    }

    /**
     * Parses the input string for a view prescription command.
     *
     * @param input The input string
     * @return The prescription ID
     * @throws InvalidInputFormatException if the input format is invalid
     */
    private static String parseViewPrescription(String input) throws InvalidInputFormatException {
        if (input.length() < 18) {
            throw new InvalidInputFormatException("Invalid command format. Use: view-prescription PRESCRIPTION_ID");
        }

        String prescriptionId = input.substring(18).trim();
        if (prescriptionId.isEmpty()) {
            throw new InvalidInputFormatException("Prescription ID cannot be empty!");
        }
        
        return prescriptionId;
    }

    /**
     * Parses the input string for viewing all prescriptions for a patient.
     *
     * @param input The input string
     * @return The patient ID
     * @throws InvalidInputFormatException if the input format is invalid
     */
    private static String parseViewAllPrescriptions(String input) throws InvalidInputFormatException {
        String temp = input.replaceFirst("(?i)view-all-prescriptions\\s*", "");
        String patientId = temp.trim();
        
        if (patientId.isEmpty()) {
            throw new InvalidInputFormatException("Patient ID cannot be empty!" + System.lineSeparator()
                    + "Please use: view-all-prescriptions PATIENT_NRIC");
        }
        
        return patientId;
    }

}
