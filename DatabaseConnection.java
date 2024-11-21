package S_M_System;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String USER = "root";
    private static final String PASSWORD = "chand786";
    private static final String USER_DB_URL = "jdbc:mysql://localhost:3306/UserDatabase";

    public static Connection connectToStudentManager() {
        return connect("jdbc:mysql://localhost:3306/StudentManager");
    }

    public static Connection connectToTeacherManager() {
        return connect("jdbc:mysql://localhost:3306/TeacherManager");
    }

    public static Connection connectToClassManager() {
        return connect("jdbc:mysql://localhost:3306/ClassManager");
    }

    public static Connection connectToSubjectManager() {
        return connect("jdbc:mysql://localhost:3306/SubjectManager");
    }

    public static Connection connectToRegistrationPage() {
        return connect("jdbc:mysql://localhost:3306/RegistrationPage");
    }

    private static Connection connect(String url) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, USER, PASSWORD);
            System.out.println("Connection to database successful!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
        return conn;
    }






}