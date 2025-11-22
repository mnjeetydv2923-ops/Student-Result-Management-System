import java.util.InputMismatchException;
import java.util.Scanner;

// Custom checked exception
class InvalidMarksException extends Exception {
    public InvalidMarksException(String message) {
        super(message);
    }
}

class Student {
    private int rollNumber;
    private String studentName;
    private int[] marks = new int[3];

    public Student(int rollNumber, String studentName, int[] marks) throws InvalidMarksException {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        validateMarks(marks);
        System.arraycopy(marks, 0, this.marks, 0, 3);
    }

    private void validateMarks(int[] marks) throws InvalidMarksException {
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] < 0 || marks[i] > 100) {
                throw new InvalidMarksException("Invalid marks for subject " + (i+1) + ": " + marks[i]);
            }
        }
    }

    public double calculateAverage() {
        int sum = 0;
        for (int m : marks) sum += m;
        return sum / 3.0;
    }

    public String getResultStatus() {
        for (int m : marks) {
            if (m < 40) return "Fail";
        }
        return "Pass";
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void displayResult() {
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Student Name: " + studentName);
        System.out.print("Marks: ");
        for (int m : marks) System.out.print(m + " ");
        System.out.println();
        System.out.println("Average: " + calculateAverage());
        System.out.println("Result: " + getResultStatus());
    }
}

public class ResultManager {
    private static final int MAX_STUDENTS = 50;
    private Student[] students = new Student[MAX_STUDENTS];
    private int count = 0;
    private Scanner scanner = new Scanner(System.in);

    public void mainMenu() {
        try {
            while (true) {
                System.out.println("=====Student Result Management System =====");
                System.out.println("1. Add Student");
                System.out.println("2. Show Student Details");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> showStudentDetails();
                    case 3 -> {
                        System.out.println("Exiting program. Thank you!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (InputMismatchException ime) {
            System.out.println("Input mismatch: please enter the correct data type.");
        } finally {
            // ensure scanner closed / final message
            System.out.println("Closing manager. Goodbye.");
            scanner.close();
        }
    }

    public void addStudent() {
        try {
            if (count >= MAX_STUDENTS) {
                System.out.println("Student storage full.");
                return;
            }
            System.out.print("Enter Roll Number: ");
            int roll = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine();

            int[] marks = new int[3];
            for (int i = 0; i < 3; i++) {
                System.out.print("Enter marks for subject " + (i+1) + ": ");
                marks[i] = scanner.nextInt();
            }

            // create student; constructor validates marks and may throw InvalidMarksException
            Student s = new Student(roll, name, marks);
            students[count++] = s;
            System.out.println("Student added successfully. Returning to main menu...");
        } catch (InvalidMarksException ime) {
            System.out.println("Error:" + ime.getMessage() + " Returning to main menu...");
        } catch (InputMismatchException ime) {
            System.out.println("Input mismatch encountered. Returning to main menu...");
            scanner.nextLine(); // clear invalid token
        }
    }

    public void showStudentDetails() {
        try {
            System.out.print("Enter Roll Number to search: ");
            int roll = scanner.nextInt();
            boolean found = false;
            for (int i = 0; i < count; i++) {
                if (students[i].getRollNumber() == roll) {
                    students[i].displayResult();
                    found = true;
                    break;
                }
            }
            if (!found) System.out.println("Student not found.");
        } catch (InputMismatchException ime) {
            System.out.println("Invalid input for roll number.");
            scanner.nextLine();
        }
    }

    public static void main(String[] args) {
        new ResultManager().mainMenu();
    }
}
