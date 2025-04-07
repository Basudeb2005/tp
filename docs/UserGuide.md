# Basudeb Chakraborty - Project Portfolio Page

## Overview
ClinicEase is a desktop app with a command-line interface for doctors to manage patients, appointments, and prescriptions. It allows tracking medical histories, scheduling, and exporting detailed prescriptions in HTML format.

## Summary of Contributions

### Code contributed
[RepoSense Code Dashboard](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&tabOpen=true&tabType=authorship&tabAuthor=Basudeb2005&tabRepo=AY2425S2-CS2113-T11b-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false) 

Full URL: https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&tabOpen=true&tabType=authorship&tabAuthor=Basudeb2005&tabRepo=AY2425S2-CS2113-T11b-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false

### Enhancements implemented

#### 1. Core Application Structure
I designed and implemented the foundational structure of the application, including:
- Initial UI class setup with welcome/goodbye messages and command reading functionality
- Parser framework for processing user commands
- Basic application workflow in ClinicEase main class

#### 2. Appointment Management System
I implemented key appointment features:
- Appointment class design with unique IDs and status tracking
- Add appointment command to schedule patient appointments
- Delete appointment functionality to remove unnecessary appointments
- Appointment display functionality in UI with proper formatting

#### 3. Prescription Management System
I implemented the complete Prescription Management System, a key feature that enables doctors to create and manage patient prescriptions in the ClinicEase application:
- **Prescription Class Design**: Designed and implemented the `Prescription` class with essential attributes and functionality:
    - Storage of patient details, symptoms, medicines, and notes
    - Automatic generation of unique prescription IDs
    - Timestamp recording for each prescription

- **Prescription Commands**: Implemented three main commands for managing prescriptions:
    - `add-prescription`: Allows doctors to create new prescriptions with patient ID, symptoms, medicines, and optional notes
    - `view-all-prescriptions`: Shows all prescriptions for a specific patient
    - `view-prescription`: Displays details of a specific prescription by ID

- **HTML Generation**: Created a feature that generates HTML prescription documents for printing:
    - Professional layout with CSS styling
    - Organized sections for patient information, symptoms, medicines, and special instructions
    - Print functionality via a button in the HTML document

- **Storage and Persistence**: Implemented storage functionality to save and retrieve prescriptions from the file system with a custom serialization format.

- **Comprehensive Testing**: Created an extensive test suite for the prescription subsystem: Unit tests for all prescription-related classes, Integration tests for prescription management functionality, End-to-end testing of the prescription workflow.

### Contributions to the User Guide

- Added the "Managing Prescriptions" section, documenting:
    - Command format and examples for adding prescriptions
    - Instructions for viewing all prescriptions for a patient
    - Instructions for viewing specific prescriptions and generating HTML reports
    - Command summary section for all prescription-related commands

### Contributions to the Developer Guide

- Added documentation for the core application structure
- Added documentation for the prescription management features, including Use cases for adding, viewing, and generating prescriptions, Manual testing instructions for prescription management, and User stories related to prescription functionality.

### Contributions to team-based tasks

- Set up the initial application architecture that the team built upon
- Integrated the prescription management subsystem with the existing patient management system  and ensured compatibility of prescription commands with the overall parsing structure

### Features I'm proud of

1. **HTML Prescription Generation**
   - The ability to generate professional-looking prescriptions that doctors can print was a feature I'm particularly proud of implementing. This bridges the gap between digital management and the physical documents patients need.

2. **Comprehensive Prescription Workflow**
   - Creating a complete end-to-end solution that handles everything from prescription creation to storage to retrieval to presentation was a significant achievement.

3. **Robust Testing Framework**
   - The extensive test suite I developed ensures the reliability of the prescription system, with test coverage for all major components and operations.

4. **Technical Documentation**
   - The detailed sequence and class diagrams help future developers understand the architecture and implementation of the prescription management feature.

5. **Architecture Documentation Blueprint**  
   - Created initial architecture diagram
   - Created the sequence diagrams  
   - Collaborated with the team to refine and evolve the architecture diagram 

### Issues Resolved

1. **Better User Experience**  
   - Fixed vague error messages for prescription commands  
   - Provided clearer, more intuitive feedback  

2. **Prescription Storage Bug Fixed**  
   - Resolved "Index out of bounds" error when loading over 5 prescriptions  
   - Enabled support for unlimited entries and prevented data loss  
