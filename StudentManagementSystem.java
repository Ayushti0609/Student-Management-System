import java.util.*;
import java.io.*;  // For File Handling(save/load)

// Student class to store data
class Student {
    int id;
    String name;
    double marks;

    Student(int id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    // Method to display student details
    void display() {
        System.out.println("ID: " + id + ", Name: " + name + ", Marks: " + marks);
    }
}

public class StudentManagementSystem {

    static ArrayList<Student> students = new ArrayList<>(); // List to store students
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();  // Load existing data when program starts

        while (true) {
            
            System.out.println("\n====================================");
            System.out.println("    STUDENT MANAGEMENT SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Update Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    addStudent(); // call method to add student.
                    break;

                case 2:
                    viewStudents(); // call method to display student.
                    break;
                
                case 3:
                    searchStudent();  // call method to search student.
                    break;

                case 4:
                    deleteStudent();  // call method to delete student.
                    break;

                case 5:
                    updateStudent();  //Call method to update student
                    break;

                case 6:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // Function to add student

    static void addStudent() {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        sc.nextLine(); // clear buffer Otherwise Name input clears.
        
        // check if ID is valid
        if(id <= 0){
            System.out.println("ID must be positive");
            return;
        }

        //Check if ID already exists
        for(Student s: students)
        {
            if(s.id==id)
            {
                System.out.println("ID already Exists : ");
                return;
            }
        }
        
        System.out.print("Enter Name: ");
        String name = sc.nextLine();    //take marks input

        // Check if name is empty
        if(name.trim().isEmpty())
        {
            System.out.println(" Name cannot be empty");
            return;
        }

        // take marks input

        System.out.print("Enter Marks: ");
        double marks = sc.nextDouble();  

        // Validate marks
        if(marks<0 || marks >100)
        {
            System.out.println("Invalid marks! Enter between 0-100");
            return;
        }

        students.add(new Student(id, name, marks)); //Adding student after validations pass

        saveToFile();   //Save Student Data after adding student

        System.out.println("Student Added Successfully!!");
    }

    // Function to view all students
    static void viewStudents() {
        // check if list is empty
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        // Print table header
        System.out.println("\n---------------------------------------------");
        System.out.printf("%-5s %-15s %-10s\n", "ID", "Name", "Marks");
        System.out.println("---------------------------------------------");

        // Loop through students and print formatted data
        for (Student s : students) {
            System.out.printf("%-5d %-15s %-10.2f\n", s.id, s.name, s.marks);
        }
        System.out.println("---------------------------------------------");
    }

    // Function to Search Student.

    static void searchStudent()
    {
        System.out.println("Enter Student ID to Search : ");
        int id=sc.nextInt();

        // Loop Through lit to find student
        for(Student s:students)   
        {
            if(s.id==id)
            {
                // When student found
                System.out.println("\n--- Student Found ---");
                System.out.printf("%-5s %-15s %-10s\n", "ID", "Name", "Marks");
                System.out.println("---------------------------------");
                System.out.printf("%-5d %-15s %-10.2f\n", s.id, s.name, s.marks);
                s.display();
                return;
            }
        }
        //if not found

        System.out.println("Student Not Found! ");
    } 

    // Function To Delete Student by ID.

    static void deleteStudent()
    {
        System.out.println("Enter Student ID to delete");
        int id=sc.nextInt();

        // removeIf removes student if condition matches
        boolean removed=students.removeIf(s->s.id==id);

        if(removed)
        {
            saveToFile();  //Student data update after deleting a student 
            System.out.println("Student Deleted Succesfully : ");
        }
        else{
            System.out.println("Student not found : ");
        }
    }

    // Function to save all student data into a file

    static void saveToFile() {
    try {
        // PrintWriter use to write in file.
        PrintWriter pw = new PrintWriter("students.txt");

        // Loop through all students and write their data
        for (Student s : students)
        {
            // Format: id,name,marks
            pw.println(s.id + "," + s.name + "," + s.marks);
        }

        pw.close(); // File close 

        } catch (Exception e) 
        {
        // Handle any error during file writing
        System.out.println("Error saving data.");
        }
    }

//---------------Function to load student data from file when program starts

    static void loadFromFile() {
    try {
        File file = new File("students.txt");

        // If file does not exist, simply return
        if (!file.exists()) return;

        Scanner fileScanner = new Scanner(file);

        // Read file line by line
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();

            // Split data using comma
            String[] parts = line.split(",");

            // Convert string data into proper types
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double marks = Double.parseDouble(parts[2]);

            // Create Student object and add to list
            students.add(new Student(id, name, marks));
        }

        fileScanner.close(); // Close file after reading

    } catch (Exception e) {
        // Handle any error during file reading
        System.out.println("Error loading data.");
    }
    }

    // Function to update student details

    static void updateStudent() 
    {
        // Ask user for student ID
        System.out.print("Enter ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Student s : students) 
        {
            if (s.id == id) 
            {

                System.out.print("Enter new name: ");
                String newName = sc.nextLine();  // take new name input

                // Check if name is empty
                if (newName.trim().isEmpty()) {
                    System.out.println("Name cannot be empty.");
                    return;
                }

                System.out.print("Enter new marks: ");
                double newMarks = sc.nextDouble();  // take new marks input

                // Check marks range
                if (newMarks < 0 || newMarks > 100) {
                    System.out.println("Marks must be between 0 and 100.");
                    return;
                }

                // Update student data
                s.name = newName;
                s.marks = newMarks;

                saveToFile(); // Save updated data

                System.out.println("Student Updated Successfully!");
                return;  // exit after update
            }
        }
        System.out.println("Student not found."); // If no student Found
    }
}