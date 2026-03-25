import javax.swing.*; // For GUI components
import java.awt.event.*; // For handling button events
import java.util.*;
import java.io.*;
import java.sql.*;

// Student class (same as before)
class Student {
    int id;
    String name;
    double marks;

    Student(int id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    // Used to display student data
    public String toString() {
        return id + " | " + name + " | " + marks;
    }
}

public class studentGUI {

    static ArrayList<Student> students = new ArrayList<>();


        // Oracle DB connection function
        static Connection connectDB() {
            try {
                System.out.println("Trying to connect...");

                Class.forName("oracle.jdbc.driver.OracleDriver");

                String url = "jdbc:oracle:thin:@localhost:1521:XE";
                String user = "SMS";   
                String pass = "ayush";

                Connection con = DriverManager.getConnection(url, user, pass);

                System.out.println("Connected SUCCESS!");

                return con;

            } catch (Exception e) {
                System.out.println(" CONNECTION ERROR:");
                e.printStackTrace(); 
                return null;
            }
        }

        // Fetch all students from DB
        static String getAllStudents() {
            StringBuilder data = new StringBuilder();

            try {
                Connection con = connectDB();

                if(con == null)
                {
                    return "Database connection failed";
                }
                String query = "SELECT * FROM students";
                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    data.append(rs.getInt("id"))
                        .append(" | ")
                        .append(rs.getString("name"))
                        .append(" | ")
                        .append(rs.getDouble("marks"))
                        .append("\n");
                }

                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return data.toString();
        }

        // Delete student from DB by ID
        static void deleteStudentDB(int id) {
            try {
                Connection con = connectDB();

                String query = "DELETE FROM students WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Deleted successfully!");
                } else {
                    System.out.println("Student not found!");
                }

                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Update student in DB
        static void updateStudentDB(int id, String name, double marks) {
            try {
                Connection con = connectDB();

                String query = "UPDATE students SET name = ?, marks = ? WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, name);
                ps.setDouble(2, marks);
                ps.setInt(3, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Updated successfully!");
                } else {
                    System.out.println("Student not found!");
                }

                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Insert student into DB
        static boolean addStudentDB(int id, String name, double marks) {
            try {
                Connection con = connectDB();

                if (con == null) {
                    return false; //  connection fail
                }

                String query = "INSERT INTO students VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setDouble(3, marks);

                ps.executeUpdate();

                System.out.println("Insert function called");

                con.close();

                return true; //  success

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    // Save data to file
    static void saveToFile() {
        try {
            PrintWriter pw = new PrintWriter("students.txt");

            for (Student s : students) {
                pw.println(s.id + "," + s.name + "," + s.marks);
            }

            pw.close();

        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }
    // Load data from file
    static void loadFromFile() {
        try {
            File file = new File("students.txt");

            if (!file.exists()) return;

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double marks = Double.parseDouble(parts[2]);

                students.add(new Student(id, name, marks));
            }

            fileScanner.close();

        } catch (Exception e) {
            System.out.println("Error loading data");
        }
    }

    public static void main(String[] args) {

        loadFromFile();

        // Create window (frame)
        JFrame frame = new JFrame("Student Management System");
        // Set background color
        frame.getContentPane().setBackground(new java.awt.Color(240, 248, 255));

        // Create font
        java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.BOLD, 14);
        frame.setSize(400, 400);
        frame.setLayout(new java.awt.GridLayout(0, 2, 10, 10));// Manual layout
        ((javax.swing.JComponent) frame.getContentPane()).setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Title
        JLabel title = new JLabel("Student Management System", JLabel.CENTER);
        title.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));

        // Add title to frame
        frame.add(title);
        frame.add(new JLabel("")); // empty space (important for grid)

        // Label for ID
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(font);

        // Label for Name
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(font);

        // Label for Marks
        JLabel marksLabel = new JLabel("Marks:");
        marksLabel.setFont(font);

        // Input field for ID
        JTextField idField = new JTextField();

        // Input field for Name
        JTextField nameField = new JTextField();

        // Input field for Marks
        JTextField marksField = new JTextField();

        // Add in correct order
        frame.add(idLabel);
        frame.add(idField);

        frame.add(nameLabel);
        frame.add(nameField);

        frame.add(marksLabel);
        frame.add(marksField);

        // Button to add student
        JButton addButton = new JButton("Add");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new java.awt.GridLayout(1, 4, 10, 10));
        addButton.setBackground(new java.awt.Color(76, 175, 80));
        addButton.setForeground(java.awt.Color.WHITE);
        //addButton.setBounds(50, 220, 100, 30);
        //frame.add(addButton);
        buttonPanel.add(addButton);

        // Button to view students
        JButton viewButton = new JButton("View");
        viewButton.setBackground(new java.awt.Color(33, 150, 243));
        viewButton.setForeground(java.awt.Color.WHITE);
        //viewButton.setBounds(200, 220, 100, 30);
        //frame.add(viewButton);
        buttonPanel.add(viewButton);
        

        // Button to delete student
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new java.awt.Color(244, 67, 54));
        deleteButton.setForeground(java.awt.Color.WHITE);
        //deleteButton.setBounds(50, 270, 100, 30);
        //frame.add(deleteButton);
        buttonPanel.add(deleteButton);

        // Button to update student
        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new java.awt.Color(255, 152, 0));
        updateButton.setForeground(java.awt.Color.WHITE);
        //updateButton.setBounds(200, 270, 100, 30);
        //frame.add(updateButton);
        buttonPanel.add(updateButton);

        // Add panel to frame
        frame.add(new JLabel("")); // empty cell (important)
        frame.add(buttonPanel);

        // Text area to display students
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        // Set size of scroll area
        scrollPane.setPreferredSize(new java.awt.Dimension(300, 120));
        //scrollPane.setBounds(50, 320, 300, 100);

        frame.add(scrollPane);

        // Add button action
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                System.out.println("Add button clicked");

                // Get values from input fields
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                double marks = Double.parseDouble(marksField.getText());

                // NAME VALIDATION
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Name cannot be empty!");
                    return;
                }

                //  MARKS VALIDATION
                if (marks < 0 || marks > 100) {
                    JOptionPane.showMessageDialog(frame, "Marks must be between 0 and 100!");
                    return;
                }

                // Add student to list
                //addStudentDB(id, name, marks);

                //saveToFile();  //to save student at file
                boolean success = addStudentDB(id, name, marks);

                // Show success message
                if(success){
                JOptionPane.showMessageDialog(frame, "Student Added!");
                }
                else{
                JOptionPane.showMessageDialog(frame, "Database Error");
 
                }
                // Clear fields
                idField.setText("");
                nameField.setText("");
                marksField.setText("");
            }
        });

        // View button action
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("View button clicked");

                // StringBuilder data = new StringBuilder();

                // Collect student data
                // for (Student s : students) {
                //     data.append(s.toString()).append("\n");
                // }

                // Show data in popup
                displayArea.setText(getAllStudents());
            }
        });

        // Delete button action
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                System.out.println("Deleted clicked");

                // Get ID from input field
                int id = Integer.parseInt(idField.getText());

                deleteStudentDB(id);

                displayArea.setText(getAllStudents());

                // Remove student using removeIf
                //boolean removed = students.removeIf(s -> s.id == id);

                //if (removed) {
                    //saveToFile();
                    //JOptionPane.showMessageDialog(frame, "Student Deleted Successfully!");
                //} else {
                //JOptionPane.showMessageDialog(frame, "Student not found!");
                //}

                // Clear field
                //idField.setText("");
            }
        });

        // Update button action
        updateButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            try {
                // Get values from input fields
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                double marks = Double.parseDouble(marksField.getText());

                updateStudentDB(id, name, marks);

                displayArea.setText(getAllStudents());

                // Loop through students to find matching ID
                for (Student s : students) {

                    if (s.id == id) {

                        // Validate name
                        if (name.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Name cannot be empty!");
                            return;
                        }

                        // Validate marks
                        if (marks < 0 || marks > 100) {
                            JOptionPane.showMessageDialog(frame, "Marks must be between 0 and 100!");
                            return;
                        }

                        // Update data
                        s.name = name;
                        s.marks = marks;

                        saveToFile();

                        JOptionPane.showMessageDialog(frame, "Student Updated Successfully!");

                        // Clear fields
                        idField.setText("");
                        nameField.setText("");
                        marksField.setText("");

                        return;
                    }
                }

                // If student not found
                JOptionPane.showMessageDialog(frame, "Student not found!");

            } catch (Exception ex) {
                // Handle invalid input (like text instead of number)
                JOptionPane.showMessageDialog(frame, "Invalid input!");
            }
        }
    });
        // Make window visible
        frame.setVisible(true);
    }
}