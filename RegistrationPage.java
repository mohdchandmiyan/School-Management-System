package S_M_System;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistrationPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegistrationPage() {
        setTitle("Registration Page");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());

        JPanel registrationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        registrationPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        registrationPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registrationPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        registrationPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        registrationPanel.add(confirmPasswordField, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 3;
        registrationPanel.add(registerButton, gbc);

        JButton backButton = new JButton("Back to Login");
        gbc.gridy = 4;
        registrationPanel.add(backButton, gbc);

        add(registrationPanel);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

            if (password.equals(confirmPassword)) {
                String result = registerUser(username, password);
                JOptionPane.showMessageDialog(this, result);
                if (result.equals("User registered successfully!")) {
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        LoginPage loginPage = new LoginPage();
                        loginPage.setVisible(true);
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            });
        });
    }

    private String registerUser(String username, String password) {
        String checkQuery = "SELECT * FROM Users WHERE username = ?";
        String insertQuery = "INSERT INTO Users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.connectToRegistrationPage();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return "Username already exists!";
                }
            }

            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();
            return "User registered successfully!";

        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred during registration.";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationPage registrationPage = new RegistrationPage();
            registrationPage.setVisible(true);
        });
    }
}