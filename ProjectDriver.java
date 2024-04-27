// Final Project
// Group Members: Logan Gerhard, Parker Helms



import java.io.Serializable;
import java.util.HashSet;
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

//---------------------------
/*
// Assuming you have parsed the class information from the file and stored it in a List<ClassInfo> variable named classInfoList

// Iterate through each ClassInfo object in the list
for (ClassInfo classInfo : classInfoList) {
    // Access and print class information using getters
    System.out.println("Class Number: " + classInfo.getClassNumber());
    System.out.println("Prefix: " + classInfo.getPrefix());
    System.out.println("Title: " + classInfo.getTitle());
    System.out.println("Level: " + classInfo.getLevel());
    System.out.println("Modality: " + classInfo.getModality());
    if (!classInfo.getModality().equalsIgnoreCase("Online")) {
        System.out.println("Location: " + classInfo.getLocation());
        System.out.println("Has Lab: " + (classInfo.isHasLab() ? "Yes" : "No"));
    }
    System.out.println("Credit Hours: " + classInfo.getCreditHours());
    if (classInfo.isHasLab()) {
        System.out.println("Lab Locations:");
        for (String labLocation : classInfo.getLabLocations()) {
            System.out.println("\t" + labLocation);
        }
    }
    System.out.println(); // Add a newline for better readability between classes
}
*/

/*
//Test parsing of lect.txt
for (ClassInfo classInfo : classInfoList) {
    System.out.println(classInfo);
}
*/

//List<ClassInfo> classInfoList = ClassInfo.readClassInfoFromFile("lect.txt");

public class ProjectDriver {

    public static void main(String[] args) {
    	
    	
    	MainMenu mainMenu = new MainMenu();
        while (true) {
            mainMenu.displayMainMenu();
        }
    	

    	
    }


}
//---------------------------
class MainMenu {
    private Scanner scanner;
    private Map<String, Student> studentsMap; // Map to store students by ID

    public MainMenu() {
        scanner = new Scanner(System.in);
        studentsMap = new HashMap<>();
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

    private void studentManagement() {
        System.out.println("\nStudent Management:");
        System.out.println("a. Add a student");
        System.out.println("b. Search for a student by ID");
        System.out.println("c. Delete a student");
        System.out.println("d. Print the fee invoice of a student by ID");
        System.out.println("e. Print all students grouped by type");
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
                // Implement deleteStudent() method
                break;
            case 'd':
                // Implement printFeeInvoice() method
                break;
            case 'e':
                // Implement printAllStudentsGroupedByType() method
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                studentManagement();
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
                System.out.print("Enter Name, Advisor, Research Subject, and CRN: ");
            } else if (studentType.equals("MS")) {
                System.out.print("Enter Name, Grad CRNs Taken, and CRN: ");
            } else if (studentType.equals("UNDERGRAD")) {
                System.out.print("Enter Name, Undergrad CRNs Taken, GPA, and Resident (true/false): ");
            }

            studentInfo = scanner.nextLine().split("\\|");

            // Create student object based on type and add to map
            Student newStudent = null;
            switch (studentType) {
                case "PHD":
                    newStudent = new PhdStudent(studentInfo[0], id, studentInfo[1], studentInfo[2], Integer.parseInt(studentInfo[3]));
                    break;
                case "MS":
                    String[] crnsTaken = studentInfo[1].split(",");
                    int[] crnArray = new int[crnsTaken.length];
                    for (int i = 0; i < crnsTaken.length; i++) {
                        crnArray[i] = Integer.parseInt(crnsTaken[i].trim());
                    }
                    newStudent = new MsStudent(studentInfo[0], id, crnArray, Integer.parseInt(studentInfo[2]));
                    break;
                case "UNDERGRAD":
                    String[] undergradCrns = studentInfo[1].split(",");
                    int[] undergradCrnArray = new int[undergradCrns.length];
                    for (int i = 0; i < undergradCrns.length; i++) {
                        undergradCrnArray[i] = Integer.parseInt(undergradCrns[i].trim());
                    }
                    newStudent = new UndergraduateStudent(studentInfo[0], id, undergradCrnArray, Double.parseDouble(studentInfo[2]), Boolean.parseBoolean(studentInfo[3]));
                    break;
                default:
                    System.out.println("Invalid student type.");
                    return;
            }

            studentsMap.put(id, newStudent); // Add student to map
            System.out.println("[" + newStudent.getName() + "] added!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void searchStudentById() {
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
    }

    private void classManagement() {
        System.out.println("\nClass Management:");
        System.out.println("a. Search for a class or lab using the class/lab number");
        System.out.println("b. Delete a class (and associated labs)");
        System.out.println("c. Add a lab to a class (when applicable)");
        System.out.println("d. Display the list of classes and labs");
        System.out.print("Choose an option: ");
        char choice = scanner.nextLine().charAt(0);

        switch (choice) {
            case 'a':
                // Implement searchClassOrLab() method
                break;
            case 'b':
                // Implement deleteClass() method
                break;
            case 'c':
                // Implement addLabToClass() method
                break;
            case 'd':
                // Implement displayClassesAndLabs() method
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                classManagement();
        }
    }
}


//---------------------------
//Abstract class Student to be used as a base class for UndergraduateStudent, GraduateStudent, PhdStudent, and MsStudent
abstract class Student {
    // Instance variables
    private String name;
    private String id;
    
 // Static set to store used IDs
    private static Set<String> usedIds = new HashSet<>();
    
    // Constructor
    public Student(String name, String id) throws IdException {
        this.name = name;
        validateId(id); // Validate ID format
        this.id = id;
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

    // Static memory array to store course information and credit hours:
    // Defining here to allow direct access to the static array within the subclasses
    protected static String[] coursePrefix = new String[10000];
    protected static int[] creditHours = new int[10000];

    static {
        // Initialize coursePrefix and creditHours
        coursePrefix[4587] = "MAT 236";
        creditHours[4587] = 4;
        coursePrefix[4599] = "COP 220";
        creditHours[4599] = 3;
        coursePrefix[8997] = "GOL 124";
        creditHours[8997] = 1;
        coursePrefix[9696] = "COP 100";
        creditHours[9696] = 3;
        coursePrefix[4580] = "MAT 136";
        creditHours[4580] = 1;
        coursePrefix[2599] = "COP 260";
        creditHours[2599] = 3;
        coursePrefix[1997] = "CAP 424";
        creditHours[1997] = 1;
        coursePrefix[5696] = "KOL 110";
        creditHours[5696] = 2;
        coursePrefix[7587] = "MAT 936";
        creditHours[7587] = 5;
        coursePrefix[1599] = "COP 111";
        creditHours[1599] = 3;
        coursePrefix[6997] = "GOL 109";
        creditHours[6997] = 1;
        coursePrefix[2696] = "COP 101";
        creditHours[2696] = 3;
        coursePrefix[5580] = "MAT 636";
        creditHours[5580] = 2;
        coursePrefix[2099] = "COP 268";
        creditHours[2099] = 3;
        coursePrefix[4997] = "CAP 427";
        creditHours[4997] = 1;
        coursePrefix[3696] = "KOL 910";
        creditHours[3696] = 2;
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
    public UndergraduateStudent(String name, String id, int[] undergradCrnsTaken, double gpa, boolean resident) throws IdException {
        super(name, id);
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
                double cost = creditHourCost * creditHours[crn];
                totalCost += cost;
                System.out.println("\t" + crn + "\t" + coursePrefix[crn] + "\t\t" + creditHours[crn] + "\t$" + String.format("%.2f", cost));
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

    // Validate ID format
    private void validateId(String id) throws IdException {
        if (!id.matches("[a-zA-Z]{2}\\d{4}")) {
            throw new IdException("Invalid ID format. ID must be of the form LetterLetterDigitDigitDigitDigit (e.g., er7894).");
        }
    }
}


//---------------------------
//GraduateStudent class that extends Student. Base class for PhdStudent and MsStudent.
abstract class GraduateStudent extends Student {
    // Instance variable
    private int crn;

    // Constructor
    public GraduateStudent(String name, String id, int crn) throws IdException {
        super(name, id);
        validateId(id); // Validate ID format
        this.crn = crn;
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

  // Constructor
  public PhdStudent(String name, String id, String advisor, String researchSubject, int crn) throws IdException {
      super(name, id, crn);
      this.researchSubject = researchSubject;
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
    public MsStudent(String name, String id, int[] gradCrnsTaken, int crn) throws IdException {
        super(name, id, crn);
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
            final double TUITION = 300; //cost per credit hour

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
                double cost = TUITION * creditHours[crn];
                totalCost += cost;
                System.out.println("\t" + crn + "\t" + coursePrefix[crn] + "\t\t" + creditHours[crn] + "\t$" + String.format("%.2f", cost));
            }

            System.out.println("\n\t\tHealth & id fees\t$ " + String.format("%.2f", HEALTH_AND_ID_FEES) + "\n\n\t----------------------------------------"); // Print health and id fees

            totalCost += HEALTH_AND_ID_FEES; //Add health and id fees to total cost

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
    }

    public static List<ClassInfo> readClassInfoFromFile(String fileName) {
        List<ClassInfo> classInfoList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            ClassInfo currentClass = null;
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

                if (hasLab) {
                    currentClass = new ClassInfo(classNumber, prefix, title, level, modality, location, hasLab, creditHours);
                    classInfoList.add(currentClass);
                    // Read lab locations
                    while ((line = br.readLine()) != null) {
                        if (line.split(",\\s*").length > 2) {
                            break; // Break if line contains class information
                        }
                        currentClass.addLabLocation(line.trim());
                    }
                } else {
                    ClassInfo classInfo = new ClassInfo(classNumber, prefix, title, level, modality, location, hasLab, creditHours);
                    classInfoList.add(classInfo);
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
