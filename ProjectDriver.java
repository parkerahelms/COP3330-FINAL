// Final Project
// Group Members: Logan Gerhard, Parker Helms



import java.io.Serializable;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

// Custom exception class for handling ID related exceptions
class IdException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public IdException(String message) {
        super(message);
    }
}

public class ProjectDriver {
    public static void main(String[] args) {
    	List<ClassInfo> classInfoList = ClassInfo.readClassInfoFromFile("lect.txt");
        MainMenu mainMenu = new MainMenu(classInfoList);
        while (true) {
            mainMenu.displayMainMenu();
        }
    }
}
//---------------------------
class MainMenu {
    private Scanner scanner;
    private Map<String, Student> studentsMap; // Map to store students by ID
    private List<ClassInfo> classInfoList;

    public MainMenu(List<ClassInfo> classInfoList) {
        scanner = new Scanner(System.in);
        studentsMap = new HashMap<>();
        this.classInfoList = classInfoList; // Initialize classInfoList
    }

    public void displayMainMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Student Management");
        System.out.println("2. Class Management");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                studentManagement();
                break;
            case 2:
                classManagement();
                break;
            case 3:
                System.out.println("Exiting program...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayMainMenu();
        }
    }

    
    //Student Management section:
  //-----------------------------------------------------------------------------------------------------------------------------
    private void studentManagement() {
        try {
            System.out.println("\nStudent Management:");
            System.out.println("a. Add a student");
            System.out.println("b. Search for a student by ID");
            System.out.println("c. Delete a student");
            System.out.println("d. Print the fee invoice of a student by ID");
            System.out.println("e. Print all students grouped by type");
            System.out.println("x. Return to the main menu");
            System.out.print("Choose an option: ");
            char choice = scanner.nextLine().charAt(0);

            switch (choice) {
                case 'a':
                    addStudent();
                    break;
                case 'b':
                    searchStudentById();
                    break;
                case 'c':
                    deleteStudent();
                    break;
                case 'd':
                    printFeeInvoiceById();
                    break;
                case 'e':
                    printAllStudentsGroupedByType();
                    break;
                case 'x':
                    return; // Return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
                    studentManagement();
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input format. Please enter a valid menu choice.");
            scanner.nextLine(); // Clear scanner buffer
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void addStudent() {
        try {
            // Input student details
            System.out.print("Enter Student’s ID: ");
            String id = scanner.nextLine();
            System.out.print("Student Type (PhD, MS, or Undergrad): ");
            String studentType = scanner.nextLine().toUpperCase();

            // Input remaining information based on student type
            String[] studentInfo;
            if (studentType.equals("PHD")) {
                System.out.print("Enter Name| Advisor | Research Subject | and Lab CRNs (comma-separated): ");
            } else if (studentType.equals("MS")) {
                System.out.print("Enter Name | Grad CRNs Taken | and CRN: ");
            } else if (studentType.equals("UNDERGRAD")) {
                System.out.print("Enter Name | Undergrad CRNs Taken | GPA | and Resident (true/false): ");
            }

            studentInfo = scanner.nextLine().split("\\|");

            // Create student object based on type and add to map
            Student newStudent = null;
            switch (studentType) {
                case "PHD":
                    String[] labNumbers = studentInfo[3].split(",");
                    int[] labNumberArray = new int[labNumbers.length];
                    for (int i = 0; i < labNumbers.length; i++) {
                        labNumberArray[i] = Integer.parseInt(labNumbers[i].trim());
                    }
                    newStudent = new PhdStudent(studentInfo[0], id, studentInfo[1], studentInfo[2], labNumberArray, null);
                    break;
                case "MS":
                    String[] crnsTaken = studentInfo[1].split(",");
                    int[] crnArray = new int[crnsTaken.length];
                    for (int i = 0; i < crnsTaken.length; i++) {
                        crnArray[i] = Integer.parseInt(crnsTaken[i].trim());
                    }
                    newStudent = new MsStudent(studentInfo[0], id, crnArray, Integer.parseInt(studentInfo[2]), null);
                    break;
                case "UNDERGRAD":
                    String[] undergradCrns = studentInfo[1].split(",");
                    int[] undergradCrnArray = new int[undergradCrns.length];
                    for (int i = 0; i < undergradCrns.length; i++) {
                        undergradCrnArray[i] = Integer.parseInt(undergradCrns[i].trim());
                    }
                    newStudent = new UndergraduateStudent(studentInfo[0], id, undergradCrnArray, Double.parseDouble(studentInfo[2]), Boolean.parseBoolean(studentInfo[3]), null);
                    break;
                default:
                    System.out.println("Invalid student type.");
                    return;
            }

            studentsMap.put(id, newStudent); // Add student to map
            System.out.println("[" + newStudent.getName() + "] added!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format. Please enter numeric values correctly.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void searchStudentById() {
        try {
            System.out.print("Enter Student’s ID: ");
            String id = scanner.nextLine();
            Student student = studentsMap.get(id); // Get student from map
            if (student != null) {
                System.out.println("Student Found:");
                System.out.println("Name: " + student.getName());
                System.out.println("ID: " + student.getId());
                // Print additional details based on student type
                if (student instanceof PhdStudent) {
                    PhdStudent phdStudent = (PhdStudent) student;
                    System.out.println("Research Subject: " + phdStudent.getResearchSubject());
                } else if (student instanceof MsStudent) {
                    MsStudent msStudent = (MsStudent) student;
                    System.out.println("Grad CRNs Taken: " + Arrays.toString(msStudent.getGradCrnsTaken()));
                } else if (student instanceof UndergraduateStudent) {
                    UndergraduateStudent undergradStudent = (UndergraduateStudent) student;
                    System.out.println("Undergrad CRNs Taken: " + Arrays.toString(undergradStudent.getUndergradCrnsTaken()));
                    System.out.println("GPA: " + undergradStudent.getGpa());
                    System.out.println("Resident: " + undergradStudent.isResident());
                }
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input format. Please enter a valid student ID.");
            scanner.nextLine(); // Clear scanner buffer
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void deleteStudent() {
        try {
            System.out.print("Enter Student’s ID to delete: ");
            String id = scanner.nextLine();
            Student student = studentsMap.get(id); // Get student from map
            if (student != null) {
                studentsMap.remove(id); // Remove student from map
                System.out.println("Student with ID " + id + " deleted successfully.");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input format. Please enter a valid student ID.");
            scanner.nextLine(); // Clear scanner buffer
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void printFeeInvoiceById() {
        try {
            System.out.print("Enter Student’s ID: ");
            String id = scanner.nextLine();
            Student student = studentsMap.get(id); // Get student from map
            if (student != null) {
                student.printInvoice(); // Call the printInvoice method of the student
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void printAllStudentsGroupedByType() {
        try {
            Map<String, List<Student>> studentsByType = new HashMap<>();
            for (Student student : studentsMap.values()) {
                String type = "";
                if (student instanceof PhdStudent) {
                    type = "PhD";
                } else if (student instanceof MsStudent) {
                    type = "MS";
                } else if (student instanceof UndergraduateStudent) {
                    type = "Undergrad";
                }

                List<Student> studentsOfType = studentsByType.getOrDefault(type, new ArrayList<>());
                studentsOfType.add(student);
                studentsByType.put(type, studentsOfType);
            }

            for (Map.Entry<String, List<Student>> entry : studentsByType.entrySet()) {
                String type = entry.getKey();
                List<Student> studentsOfType = entry.getValue();

                System.out.println(type + " Students:");
                for (Student student : studentsOfType) {
                    System.out.println("Name: " + student.getName());
                    System.out.println("ID: " + student.getId());
                    if (student instanceof PhdStudent) {
                        PhdStudent phdStudent = (PhdStudent) student;
                        System.out.println("Research Subject: " + phdStudent.getResearchSubject());
                    } else if (student instanceof MsStudent) {
                        MsStudent msStudent = (MsStudent) student;
                        System.out.println("Grad CRNs Taken: " + Arrays.toString(msStudent.getGradCrnsTaken()));
                    } else if (student instanceof UndergraduateStudent) {
                        UndergraduateStudent undergradStudent = (UndergraduateStudent) student;
                        System.out.println("Undergrad CRNs Taken: " + Arrays.toString(undergradStudent.getUndergradCrnsTaken()));
                        System.out.println("GPA: " + undergradStudent.getGpa());
                        System.out.println("Resident: " + undergradStudent.isResident());
                    }
                    System.out.println(); // Add a newline for better readability between students
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Error: Null pointer exception occurred. Please ensure studentsMap is properly initialized.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
  //-----------------------------------------------------------------------------------------------------------------------------

    private void classManagement() {
        try {
            System.out.println("\nClass Management:");
            System.out.println("a. Search for a class or lab using the class/lab number");
            System.out.println("b. Delete a class (and associated labs)");
            System.out.println("c. Add a lab to a class (when applicable)");
            System.out.println("x. Return to the main menu");
            System.out.print("Choose an option: ");
            char choice = scanner.nextLine().charAt(0);

            switch (choice) {
                case 'a':
                    searchClassOrLab(classInfoList, scanner);
                    break;
                case 'b':
                    // Implement deleteClass() method
                    break;
                case 'c':
                	addLabToClass();
                    break;
                case 'x':
                	return; // Return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
                    classManagement();
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input format. Please enter a valid menu choice.");
            scanner.nextLine(); // Clear scanner buffer
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void searchClassOrLab(List<ClassInfo> classInfoList, Scanner scanner) {
        try {
            System.out.print("Enter the Class/Lab Number: ");
            String searchQuery = scanner.nextLine();

            boolean found = false;
            for (ClassInfo classInfo : classInfoList) {
                // Check if the search query matches the class number
                if (classInfo.getClassNumber().equalsIgnoreCase(searchQuery)) {
                    System.out.println("Class found:");
                    System.out.println(classInfo);
                    found = true;
                    break; // Exit loop once class is found
                }
                // Check if the search query matches any lab number for this class
                if (classInfo.getLabNumbers() != null) {
                    for (int i = 0; i < classInfo.getLabNumbers().size(); i++) {
                        if (classInfo.getLabNumbers().get(i).equalsIgnoreCase(searchQuery)) {
                            System.out.println("Lab found:");
                            System.out.println("Class: " + classInfo.getClassNumber());
                            System.out.println("Lab Number: " + classInfo.getLabNumbers().get(i));
                            System.out.println("Lab Location: " + classInfo.getLabLocations().get(i));
                            System.out.println("Lab for: " + classInfo.getTitle()); // Print class title
                            found = true;
                            break; // Exit loop once lab is found
                        }
                    }
                }
            }
            if (!found) {
                System.out.println("No matching class or lab found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input format. Please enter a valid class/lab number.");
            scanner.nextLine(); // Clear scanner buffer
        } catch (NullPointerException e) {
            System.out.println("Error: Null pointer exception occurred. Please ensure classInfoList is properly initialized.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void addLabToClass() {
        try {
            System.out.print("Enter Class Number to add lab: ");
            String classNumber = scanner.nextLine();
            ClassInfo classInfo = null;
            for (ClassInfo info : classInfoList) {
                if (info.getClassNumber().equals(classNumber)) {
                    classInfo = info;
                    break;
                }
            }
            if (classInfo == null) {
                System.out.println("Class with the given number not found.");
                return;
            }
            if (classInfo.isHasLab()) {
                System.out.println("This class already has a lab assigned.");
                return;
            }

            System.out.print("Enter Lab Number: ");
            String labNumber = scanner.nextLine();
            System.out.print("Enter Lab Location: ");
            String labLocation = scanner.nextLine();

            classInfo.addLab(labNumber, labLocation);
            System.out.println("Lab added to the class successfully.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}


//---------------------------
//Abstract class Student to be used as a base class for UndergraduateStudent, GraduateStudent, PhdStudent, and MsStudent
abstract class Student {
	// Instance variables
	private String name;
	private String id;
	private Map<String, ClassInfo> classInfoMap; // Map to store class information by class number

 
	//Static set to store used IDs
	private static Set<String> usedIds = new HashSet<>();
 
 
	 // Constructor
	 public Student(String name, String id, Map<String, ClassInfo> classInfoMap) throws IdException {
	     this.name = name;
	     validateId(id); // Validate ID format
	     this.id = id;
	     this.classInfoMap = classInfoMap;
	 }
	
	 // Validate ID format and uniqueness
	 private void validateId(String id) throws IdException {
	     // Validate ID format
	     if (!id.matches("[a-zA-Z]{2}\\d{4}")) {
	         throw new IdException("Invalid ID format. ID must be of the form LetterLetterDigitDigitDigitDigit (e.g., er7894).");
	     }
	
	     // Validate ID uniqueness
	     if (usedIds.contains(id)) {
	         throw new IdException("ID " + id + " is already in use.");
	     }
	 }
	
	 // Abstract method to be implemented in subclasses
	 abstract public void printInvoice();
	
	 // Getters for name and id:
	 protected String getName() {
	     return name;
	 }
	
	 protected String getId() {
	     return id;
	 }
	
	 protected Map<String, ClassInfo> getClassInfoMap() {
	     return classInfoMap;
	 }
	}

//---------------------------
//UndergraduateStudent class that extends Student
class UndergraduateStudent extends Student {
	 // Instance variables
	 private int[] undergradCrnsTaken;
	 private double gpa;
	 private boolean resident;
	
	 // Constructor
	 public UndergraduateStudent(String name, String id, int[] undergradCrnsTaken, double gpa, boolean resident, Map<String, ClassInfo> classInfoMap) throws IdException {
	     super(name, id, classInfoMap);
	     this.undergradCrnsTaken = undergradCrnsTaken;
	     this.gpa = gpa;
	     this.resident = resident;
	 }
	
	 // Getter method for gpa
	 public double getGpa() {
	     return gpa;
	 }
	
	 // Getter method for undergradCrnsTaken
	 public int[] getUndergradCrnsTaken() {
	     return undergradCrnsTaken;
	 }
	
	 // Getter method for resident status
	 public boolean isResident() {
	     return resident;
	 }
	
	 @Override // Override the abstract method in the base class
	 // Method to print the invoice for an undergraduate student
	 public void printInvoice() {
	     try {
	         // Constants for legibility and ease of use:
	         final double HEALTH_AND_ID_FEES = 35;
	         final double IN_STATE_TUITION = 120.25;
	         final double OUT_OF_STATE_TUITION = IN_STATE_TUITION * 2;
	
	         System.out.println("\tVALENCE COLLEGE");
	         System.out.println("\tORLANDO FL 10101");
	         System.out.println("\t---------------------\n");
	         System.out.println("\tFee Invoice Prepared for Student:");
	         System.out.println("\t" + getId() + "-" + getName() + "\n"); // Print student name and id
	
	         // Print the course information and cost for each course taken
	         double totalCost = 0; // Initialize total cost
	         double creditHourCost = resident ? IN_STATE_TUITION : OUT_OF_STATE_TUITION; // In-state or out-of-state tuition
	
	         // Print the cost per credit hour
	         System.out.println("\t1 Credit Hour = $" + creditHourCost + "\n");
	         System.out.println("\tCRN\tCR_PREFIX\tCR_HOURS");
	
	         // Loop through each course taken and print the course information and cost
	         for (int crn : undergradCrnsTaken) {
	             ClassInfo classInfo = getClassInfoMap().get(String.valueOf(crn));
	             if (classInfo != null) {
	                 double cost = creditHourCost * classInfo.getCreditHours();
	                 totalCost += cost;
	                 System.out.println("\t" + crn + "\t" + classInfo.getPrefix() + "\t\t" + classInfo.getCreditHours() + "\t$" + String.format("%.2f", cost));
	             }
	         }
	
	         System.out.println("\n\t\tHealth & id fees\t$ " + String.format("%.2f", HEALTH_AND_ID_FEES) + "\n\n\t----------------------------------------"); // Print health and id fees
	
	         totalCost += HEALTH_AND_ID_FEES; //Add health and id fees
	
	         // Determine if the student is eligible for a discount and apply it if so
	         if (gpa >= 3.5 && totalCost > 500) {
	             System.out.println("\t\t\t\t$ " + String.format("%.2f", totalCost));
	             System.out.println("\t\t\t -$ " + String.format("%.2f", (totalCost * 0.25)));
	             totalCost -= totalCost * 0.25;
	             System.out.println("\t\t\t\t----------");
	         }
	
	         // Print the total cost
	         System.out.println("\t\t\tTOTAL PAYMENTS\t$ " + String.format("%.2f", totalCost));
	         System.out.println("\n\n"); // Print new lines for legibility
	     } catch (Exception e) {
	         System.out.println("Error: " + e.getMessage());
	     }
	 }
}


//---------------------------
//GraduateStudent class that extends Student. Base class for PhdStudent and MsStudent.
abstract class GraduateStudent extends Student {
	 // Instance variable
	 private int crn;
	
	 // Constructor
	 public GraduateStudent(String name, String id, int crn, Map<String, ClassInfo> classInfoMap) throws IdException {
	     super(name, id, classInfoMap);
	     this.crn = crn;
	     validateId(id); // Validate ID format
	 }
	
	 // Abstract method to be implemented in subclasses
	 protected abstract int getTAFor();
	
	 // Validate ID format
	 private void validateId(String id) throws IdException {
	     if (!id.matches("[a-zA-Z]{2}\\d{4}")) {
	         throw new IdException("Invalid ID format. ID must be of the form LetterLetterDigitDigitDigitDigit (e.g., er7894).");
	     }
	 }
}

//---------------------------
//PhdStudent class that extends GraduateStudent
class PhdStudent extends GraduateStudent {
	 // Instance variable
	 private String researchSubject;
	 private int[] labCRNs; // Array to store lab CRNs
	
	// Constructor
	    public PhdStudent(String name, String id, String advisor, String researchSubject, int[] labCRNs, Map<String, ClassInfo> classInfoMap) throws IdException {
	        super(name, id, 0, classInfoMap); // Pass 0 for CRN since it's not used in Ph.D. students
	        this.researchSubject = researchSubject;
	        this.labCRNs = labCRNs;
	    }
	
	 // Define the getResearchSubject method
	 public String getResearchSubject() {
	     return researchSubject;
	 }
	
	 @Override // Override the abstract method in the base class
	 // Method to print the invoice for a Phd student
	 public void printInvoice() {
	     try {
	         // Constants for legibility and ease of use:
	         final double HEALTH_AND_ID_FEES = 35;
	         final double RESEARCH_FEE = 700;
	
	         System.out.println("\tVALENCE COLLEGE");
	         System.out.println("\tORLANDO FL 10101");
	         System.out.println("\t---------------------\n");
	         System.out.println("\tFee Invoice Prepared for Student:");
	         System.out.println("\t" + getId() + "-" + getName() + "\n"); // Print student name and id
	         System.out.println("\tResearch:\n" + "\t" + researchSubject + "\t\t\t$ " + String.format("%.2f", RESEARCH_FEE)); // Print research subject and fee
	         System.out.println("\n\t\tHealth & id fees\t$ " + String.format("%.2f", HEALTH_AND_ID_FEES) + "\n\n\t----------------------------------------"); // Print health and id fees
	         double totalCost = RESEARCH_FEE + HEALTH_AND_ID_FEES; // Calculate total cost
	         System.out.println("\t\t\tTotal Payments\t$ " + String.format("%.2f", totalCost)); // Print total cost
	         System.out.println("\n\n"); // Print new lines for legibility
	     } catch (Exception e) {
	         System.out.println("Error: " + e.getMessage());
	     }
	 }
	
	 @Override
	 // Method to get the TA for PhdStudent
	 protected int getTAFor() {
	     // Implement method to return the TA CRN for PhdStudent
	     return 0; // Replace 0 with actual implementation
	 }
}

//---------------------------
//MsStudent class that extends GraduateStudent
class MsStudent extends GraduateStudent {
	 // Instance variable
	 private int[] gradCrnsTaken;
	
	 // Constructor
	 public MsStudent(String name, String id, int[] gradCrnsTaken, int crn, Map<String, ClassInfo> classInfoMap) throws IdException {
	     super(name, id, crn, classInfoMap);
	     this.gradCrnsTaken = gradCrnsTaken;
	 }
	
	 // Getter method for gradCrnsTaken
	 public int[] getGradCrnsTaken() {
	     return gradCrnsTaken;
	 }
	
	 @Override
	 // Method to print the invoice for a Ms student
	 public void printInvoice() {
	     try {
	         // Constants for legibility and ease of use:
	         final double HEALTH_AND_ID_FEES = 35;
	         final double TUITION = 300; // cost per credit hour
	
	         System.out.println("\tVALENCE COLLEGE");
	         System.out.println("\tORLANDO FL 10101");
	         System.out.println("\t---------------------\n");
	         System.out.println("\tFee Invoice Prepared for Student:");
	         System.out.println("\t" + getId() + "-" + getName() + "\n"); // Print student name and id
	
	         double totalCost = 0; // Initialize total cost
	
	         System.out.println("\t1 Credit Hour = $" + String.format("%.2f", TUITION) + "\n");  // Print the cost per credit hour
	         System.out.println("\tCRN\tCR_PREFIX\tCR_HOURS");
	
	         // Loop through each course taken and print the course information and cost
	         for (int crn : gradCrnsTaken) {
	             ClassInfo classInfo = getClassInfoMap().get(String.valueOf(crn));
	             if (classInfo != null) {
	                 double cost = TUITION * classInfo.getCreditHours();
	                 totalCost += cost;
	                 System.out.println("\t" + crn + "\t" + classInfo.getPrefix() + "\t\t" + classInfo.getCreditHours() + "\t$" + String.format("%.2f", cost));
	             }
	         }
	
	         System.out.println("\n\t\tHealth & id fees\t$ " + String.format("%.2f", HEALTH_AND_ID_FEES) + "\n\n\t----------------------------------------"); // Print health and id fees
	
	         totalCost += HEALTH_AND_ID_FEES; // Add health and id fees to total cost
	
	         System.out.println("\t\t\tTOTAL PAYMENTS\t$ " + String.format("%.2f", totalCost)); // Print the total cost
	         System.out.println("\n\n"); // Print new lines for legibility
	     } catch (Exception e) {
	         System.out.println("Error: " + e.getMessage());
	     }
	 }
	
	 @Override
	 // Method to get the TA for MsStudent
	 protected int getTAFor() {
	     // Implement method to return the TA CRN for MsStudent
	     return 0; // Replace 0 with actual implementation
	 }
}


//---------------------------
class ClassInfo {
    private String classNumber;
    private String prefix;
    private String title;
    private String level;
    private String modality;
    private String location;
    private boolean hasLab;
    private int creditHours;
    private List<String> labLocations;
    private List<String> labNumbers;

    public ClassInfo(String classNumber, String prefix, String title, String level, String modality, String location, boolean hasLab, int creditHours) {
        this.classNumber = classNumber;
        this.prefix = prefix;
        this.title = title;
        this.level = level;
        this.modality = modality;
        this.location = location;
        this.hasLab = hasLab;
        this.creditHours = creditHours;
        this.labLocations = new ArrayList<>();
        this.labNumbers = new ArrayList<>();
    }
    
    public void addLab(String labNumber, String labLocation) {
        labNumbers.add(labNumber);
        labLocations.add(labLocation);
        hasLab = true;
    }

    public static List<ClassInfo> readClassInfoFromFile(String fileName) {
        List<ClassInfo> classInfoList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            ClassInfo currentClass = null;
            boolean isLabSection = false; // Flag to indicate if currently parsing lab section
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",\\s*"); // Split with comma followed by optional whitespace
                if (parts.length < 6) {
                    continue; // Skip invalid lines
                }
                String classNumber = parts[0];
                String prefix = parts[1];
                String title = parts[2];
                String level = parts[3];
                String modality = parts[4];

                boolean hasLab = false;
                String location = null;
                int creditHours = 0;

                if (modality.equalsIgnoreCase("Online")) {
                    if (parts.length != 6) {
                        continue; // Skip invalid online class lines
                    }
                    creditHours = Integer.parseInt(parts[5]);
                } else {
                    if (parts.length < 8) {
                        continue; // Skip invalid lines
                    }
                    location = parts[5];
                    hasLab = parts[6].equalsIgnoreCase("YES");
                    creditHours = Integer.parseInt(parts[7]);
                }

                if (!isLabSection) {
                    if (hasLab) {
                        currentClass = new ClassInfo(classNumber, prefix, title, level, modality, location, hasLab, creditHours);
                        classInfoList.add(currentClass);
                        isLabSection = true; // Start parsing lab section
                    } else {
                        ClassInfo classInfo = new ClassInfo(classNumber, prefix, title, level, modality, location, hasLab, creditHours);
                        classInfoList.add(classInfo);
                    }
                } else {
                    // Check if it's a new class
                    if (parts.length >= 6) {
                        isLabSection = false; // End of lab section
                    } else {
                        currentClass.addLab(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classInfoList;
    }

    public void addLabLocation(String location) {
        this.labLocations.add(location);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class Number: ").append(classNumber).append("\n");
        sb.append("Prefix: ").append(prefix).append("\n");
        sb.append("Title: ").append(title).append("\n");
        sb.append("Level: ").append(level).append("\n");
        sb.append("Modality: ").append(modality).append("\n");
        if (modality.equalsIgnoreCase("Online")) {
            sb.append("Credit Hours: ").append(creditHours).append("\n");
        } else {
            sb.append("Location: ").append(location).append("\n");
            sb.append("Has Lab: ").append(hasLab ? "Yes" : "No").append("\n");
            sb.append("Credit Hours: ").append(creditHours).append("\n");
            if (hasLab) {
                sb.append("Lab Locations:\n");
                for (String labLocation : labLocations) {
                    sb.append("\t").append(labLocation).append("\n");
                }
            }
        }
        return sb.toString();
    }

    
    public List<String> getLabNumbers() {
        return labNumbers;
    }

    public void addLabNumber(String labNumber) {
        this.labNumbers.add(labNumber);
    }

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isHasLab() {
		return hasLab;
	}

	public void setHasLab(boolean hasLab) {
		this.hasLab = hasLab;
	}

	public int getCreditHours() {
		return creditHours;
	}

	public void setCreditHours(int creditHours) {
		this.creditHours = creditHours;
	}

	public List<String> getLabLocations() {
		return labLocations;
	}

	public void setLabLocations(List<String> labLocations) {
		this.labLocations = labLocations;
	}
}
