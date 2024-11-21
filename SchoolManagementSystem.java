package S_M_System;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class SchoolManagementSystem extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private StudentManager studentManager;
    private TeacherManager teacherManager;
    private ClassManager classManager;
    private SubjectManager subjectManager;

    public SchoolManagementSystem() {
        setTitle("School Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        studentManager = new StudentManager();
        teacherManager = new TeacherManager();
        classManager = new ClassManager();
        subjectManager = new SubjectManager();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createEntityPanel("Student", studentManager), "Students");
        mainPanel.add(createEntityPanel("Teacher", teacherManager), "Teachers");
        mainPanel.add(createEntityPanel("Class", classManager), "Classes");
        mainPanel.add(createEntityPanel("Subject", subjectManager), "Subjects");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu navigationMenu = new JMenu("Navigation");
        JMenuItem studentMenuItem = new JMenuItem("Manage Students");
        JMenuItem teacherMenuItem = new JMenuItem("Manage Teachers");
        JMenuItem classMenuItem = new JMenuItem("Manage Classes");
        JMenuItem subjectMenuItem = new JMenuItem("Manage Subjects");

        studentMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Students"));
        teacherMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Teachers"));
        classMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Classes"));
        subjectMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Subjects"));

        navigationMenu.add(studentMenuItem);
        navigationMenu.add(teacherMenuItem);
        navigationMenu.add(classMenuItem);
        navigationMenu.add(subjectMenuItem);
        menuBar.add(navigationMenu);

        setJMenuBar(menuBar);
        add(mainPanel);

        cardLayout.show(mainPanel, "Students");
    }

    private JPanel createEntityPanel(String entityType, EntityManager manager) {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();

        inputPanel.add(new JLabel(entityType + " ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel(entityType + " Name:"));
        inputPanel.add(nameField);

        JButton addButton = new JButton("Add " + entityType);
        JButton displayButton = new JButton("Display " + entityType + "s");
        JButton deleteByIdButton = new JButton("Delete " + entityType + " by ID");
        JButton deleteByNameButton = new JButton("Delete " + entityType + " by Name");

        inputPanel.add(addButton);
        inputPanel.add(displayButton);
        inputPanel.add(deleteByIdButton);
        inputPanel.add(deleteByNameButton);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, scrollPane);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);

        panel.add(splitPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();

            if (!id.isEmpty() && !name.isEmpty()) {
                manager.addEntity(id, name);
                idField.setText("");
                nameField.setText("");
                displayArea.setText(entityType + " added successfully.");
            } else {
                displayArea.setText("All fields must be filled to add a " + entityType + ".");
            }
        });

        displayButton.addActionListener(e -> {
            displayArea.setText(manager.displayEntities());
        });

        deleteByIdButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(panel, "Enter " + entityType + " ID to delete:");
            if (id != null && !id.isBlank()) {
                displayArea.setText(manager.deleteEntityById(id));
            } else {
                displayArea.setText("Invalid ID entered.");
            }
        });

        deleteByNameButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(panel, "Enter " + entityType + " Name to delete:");
            if (name != null && !name.isBlank()) {
                displayArea.setText(manager.deleteEntityByName(name));
            } else {
                displayArea.setText("Invalid name entered.");
            }
        });

        return panel;
    }

    interface EntityManager {
        void addEntity(String id, String name);

        String displayEntities();

        String deleteEntityById(String id);

        String deleteEntityByName(String name);
    }

    class StudentManager implements EntityManager {
        private Map<String, String> students = new HashMap<>();

        public void addEntity(String id, String name) {
            students.put(id, name);
        }

        public String displayEntities() {
            if (students.isEmpty()) return "No students available.";
            StringBuilder result = new StringBuilder("Students:\n");
            students.forEach((id, name) -> result.append("ID: ").append(id).append(", Name: ").append(name).append("\n"));
            return result.toString();
        }

        public String deleteEntityById(String id) {
            return students.remove(id) != null
                    ? "Student with ID " + id + " has been removed."
                    : "No student found with ID " + id + ".";
        }

        public String deleteEntityByName(String name) {
            String idToRemove = students.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equalsIgnoreCase(name))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            if (idToRemove != null) {
                students.remove(idToRemove);
                return "Student with name \"" + name + "\" has been removed.";
            }
            return "No student found with name \"" + name + "\".";
        }
    }

    class TeacherManager extends StudentManager {
    }

    class ClassManager extends StudentManager {
    }

    class SubjectManager extends StudentManager {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SchoolManagementSystem app = new SchoolManagementSystem();
            app.setVisible(true);
        });
    }
}