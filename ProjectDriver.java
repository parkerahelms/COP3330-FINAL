// Final Project
// Group Members: Logan Gerhard, Parker Helms



import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// Custom exception class for handling ID related exceptions
class IdException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public IdException(String message) {
        super(message);
    }
}


public class ProjectDriver {

    public static void main(String[] args) {
        // Test your implementation here
    	
    	// Proof that IdException works:
    	/*
    	try {
            // Create an undergraduate student with a valid and unique ID
            Student undergrad1 = new UndergraduateStudent("John Doe", "jd1234", new int[]{1234, 5678}, 3.5, true);
            System.out.println("Undergraduate student created successfully.");
        } catch (IdException e) {
            System.out.println("Error creating undergraduate student: " + e.getMessage());
        }

        try {
            // Attempt to create another undergraduate student with the same ID (should throw an exception)
            Student undergrad2 = new UndergraduateStudent("Jane Smith", "jd1234", new int[]{9876, 5432}, 3.2, false);
            System.out.println("Undergraduate student created successfully.");
        } catch (IdException e) {
            System.out.println("Error creating undergraduate student: " + e.getMessage());
        }

        try {
            // Attempt to create a graduate student with an invalid ID format (should throw an exception)
            Student grad1 = new PhdStudent("Alice Brown", "ab12", "Dr. Smith", "Quantum Physics", 4567);
            System.out.println("Graduate student created successfully.");
        } catch (IdException e) {
            System.out.println("Error creating graduate student: " + e.getMessage());
        }
        /*
    	
    	//HW 5 code to test:
    	/*
    	Student s = null;
		
		try {
			s = new PhdStudent ("Zaydoun BenSellam",
					     "zb5954" , 
					     "Gary Richarson",  
					     "Fuzzy Topology" , 
					      2599 );
		} catch (IdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.printInvoice();
		
		int [] gradCrnsTaken = {7587,8997} ;
		try {
			s = new MsStudent ( "Emily Jones",
					     "em1254",
					     gradCrnsTaken,
					     1997);
		} catch (IdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.printInvoice();
		
		int [] undergradCrnsTaken = {4587,2599};
		try {
			s = new UndergraduateStudent ("Jamila Jones" , 
							"ja5225" , 
							undergradCrnsTaken , 
							3.0,
							false );
		} catch (IdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.printInvoice();
		
		*/
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
        validateId(id); // Validate ID format
        this.undergradCrnsTaken = undergradCrnsTaken;
        this.gpa = gpa;
        this.resident = resident;
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

  @Override // Override the abstract method in the base class
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
