package S_M_System;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginPage() {
        setTitle("Login Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) (screenSize.getWidth() / 2) - 150;
        int centerY = (int) (screenSize.getHeight() / 2) - 50;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(centerX - 100, centerY - 20, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(centerX, centerY - 20, 160, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(centerX - 100, centerY + 20, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(centerX, centerY + 20, 160, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(centerX, centerY + 60, 160, 25);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(centerX, centerY + 100, 160, 25);
        add(registerButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String result = authenticate(username, password);
            JOptionPane.showMessageDialog(null, result);

            if (result.equals("Login successful!")) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    SchoolManagementSystem sms = new SchoolManagementSystem();
                    sms.setVisible(true);
                });
            }
        });

        registerButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                RegistrationPage registrationPage = new RegistrationPage();
                registrationPage.setVisible(true);
            });
        });
    }

    private String authenticate(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.connectToRegistrationPage();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return "Login successful!";
            } else {
                return "Invalid username or password.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred during authentication.";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}