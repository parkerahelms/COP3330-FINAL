import java.util.ArrayList;
/**
 * unit5.hw
 * @author phelms
 */
public class DriverClass {
    public static void main(String[] args) {
        Student s;
        //***********
        s = new PhdStudent ("Zaydoun BenSellam",
                "zb5954" ,
                "Gary Richarson",
                "Fuzzy Topology" ,
                2599 );
        s.printInvoice();
        //***********
        int [] gradCrnsTaken = {7587,8997} ;
        s = new MsStudent ( "Emily Jones",
                "em1254",
                gradCrnsTaken,
                1997);
        s.printInvoice();
        //***********
        int [] undergradCrnsTaken = {4587,2599};
        s = new UndergraduateStudent ("Jamila Jones" ,
                "ja5225" ,
                undergradCrnsTaken ,
                3.0,
                false );
        s.printInvoice();
    }//end of main
}
class Course {
    String name;
    int hours;

    public Course (String curName, int curHours) {
        name = curName;
        hours = curHours;
    }
}
//---------------------------
abstract class Student {
    protected String name, id;
    protected ArrayList<Course> catolog = new ArrayList<Course>();
    public Student ( String curName , String curId) {
        name = curName;
        id = curId;
        //init array of courses to empty values
        for (int i = 0;i < 10000;i++)
            catolog.add(null);
        //now fill class info
        catolog.set(4587,new Course("MAT 236",4));
        catolog.set(4599,new Course("COP 220",3));
        catolog.set(8997,new Course("GOL 124",1));
        catolog.set(9696,new Course("COP 100",3));
        catolog.set(4580,new Course("MAT 136",1));
        catolog.set(2599,new Course("COP 206",3));
        catolog.set(1997,new Course("CAP 424",1));
        catolog.set(5696,new Course("KOL 110",2));
        catolog.set(7587,new Course("MAT 936",4));
        catolog.set(1599,new Course("COP 111",3));
        catolog.set(6977,new Course("GOL 109",1));
        catolog.set(2696,new Course("COP 101",3));
        catolog.set(5580,new Course("MAT 636",1));
        catolog.set(2099,new Course("COP 268",3));
        catolog.set(4997,new Course("CAP 427",1));
        catolog.set(3696,new Course("KOL 910",2));
    }
    abstract public void printInvoice();
}
//---------------------------
class UndergraduateStudent extends Student{
    private int[] crnsTaken;
    private double gpa;
    private boolean resident;
    public UndergraduateStudent(String curName , String curId , int [] undergradCrnsTaken , double curGpa, boolean curResident)
    {
        super (curName , curId );
        crnsTaken = undergradCrnsTaken;
        gpa = curGpa;
        resident = curResident;
    }
    //to return the total payment amount
    //element 0 = total after discount (non-discounted requests will ONLY contain this element
    //element 1 = total BEFORE discount
    //element 2 = reduction from discount
    private ArrayList<Double> calculateTotalPayment () {
        //total the credited classes
        ArrayList<Double> total = new ArrayList<Double>();
        total.add(0.0);
        for (int i = 0;i < crnsTaken.length;i++)
            total.set(0,total.get(0) + 120.25 * catolog.get(crnsTaken[i]).hours);
        total.set(0, total.get(0) + 35); //add health fee
        //check for discount status
        if (gpa >= 3.5) {
            //copy total before discount to the correct array element
            total.add(total.get(0));
            //calculate the difference the discount will apply and save to the last element
            total.add(total.get(0) * 0.25);
            //subtract the difference from the total after discount in first element
            total.set(0, total.get(0) - total.get(2));
        }
        return total;
    }
    @Override
    public void printInvoice() {
        System.out.println("\n\nVALENCE COLLEGE\nORLANDO FL 10101\n"
                + "*************************\n\nFee Invoice Prepared for:");
        System.out.println("[" + name + "][" + id + "]");
        System.out.println("\n1 Credit Hour = $120.25");
        System.out.format("\n%-10s%-20s%-20s", "CRN", "CR_PREFIX","CREDIT HOURS");
        for (int i = 0;i < crnsTaken.length;i++) {
            System.out.format("\n%-10d%-20s%-20d%s%.2f", crnsTaken[i], catolog.get(crnsTaken[i]).name,catolog.get(crnsTaken[i]).hours, "$", (double)catolog.get(crnsTaken[i]).hours * 120.25);
        }
        System.out.println("\n\nHealth & id fees $35.00\n\n----------------------------------------");
        //get total payment information
        ArrayList<Double> totalInfo = calculateTotalPayment();
        //check if a discount is applied
        if (totalInfo.size() > 1) {
            System.out.format("\n%10s%.2f", "$", totalInfo.get(1)); //print total before discount
            System.out.format("\n%10s%.2f", "-$", totalInfo.get(2)); //print subtracted amount
            System.out.format("\n%10s", "----------");
            System.out.format("\n%10s%.2f", "Total Payments $", totalInfo.get(0));
        }
        else //otherwise print the first value in the array returned, which is the total
            System.out.format("\n%10s%.2f", "Total Payments $", totalInfo.get(0));
    }
}
//---------------------------
abstract class GraduateStudent extends Student {
    protected int crnTA;
    public GraduateStudent ( String curName , String curId , int curCrn ) {
        //crn is the crn that the grad student is a teaching assistant for
        super ( curName , curId );
        crnTA = curCrn;
    }
}
//---------------------------
class PhdStudent extends GraduateStudent {
    private String advisor, subject;
    public PhdStudent (String curName, String curId , String curAdvisor, String researchSubject , int curCrn ) {
        //crn is the course number that the Phd student is a teaching assistant for
        super ( curName , curId , curCrn );
        advisor = curAdvisor;
        subject = researchSubject;
    }
    @Override
    public void printInvoice() {
        System.out.println("\n\nVALENCE COLLEGE\nORLANDO FL 10101\n"
                + "*************************\n\nFee Invoice Prepared for:");
        System.out.println("[" + name + "][" + id + "]");
        System.out.println("\nRESEARCH");
        System.out.format("\n%-10s%30.2f", subject, 700.0);
        System.out.println("\n\nHealth & id fees $35.00\n\n----------------------------------------");
        //get total payment information
        System.out.format("\n%10s%.2f", "Total Payments $", 735.0);
    }
}
//---------------------------
class MsStudent extends GraduateStudent {
    private int[] crnsTaken;
    private int crnTA;
    public MsStudent (String curName, String curId , int [] gradCrnsTaken , int curCrn ) {
        // gradCoursesTaken is the array of the crns that the Ms student is taking
        //crn is the course number that the Phd student is a teaching assistant for
        super ( curName , curId , curCrn );
        crnsTaken = gradCrnsTaken;
        crnTA = curCrn;
    }
    @Override
    public void printInvoice() {
        System.out.println("\n\nVALENCE COLLEGE\nORLANDO FL 10101\n"
                + "*************************\n\nFee Invoice Prepared for:");
        System.out.println("[" + name + "][" + id + "]");
        System.out.println("\n1 Credit Hour = $300.00");
        System.out.format("\n%-10s%-20s%-20s", "CRN", "CR_PREFIX","CREDIT HOURS");
        for (int i = 0;i < crnsTaken.length;i++) {
            System.out.format("\n%-10d%-20s%-20d%s%.2f", crnsTaken[i], catolog.get(crnsTaken[i]).name,catolog.get(crnsTaken[i]).hours, "$", (double)catolog.get(crnsTaken[i]).hours * 300);
        }
        System.out.println("\n\nHealth & id fees $35.00\n\n----------------------------------------");
        //get total payment information
        double total = 0.0;
        for (int i = 0;i < crnsTaken.length;i++)
            total += 300 * catolog.get(crnsTaken[i]).hours;
        total += 35; //add health fee
        System.out.format("\n%10s%.2f", "Total Payments $", total);
    }
}