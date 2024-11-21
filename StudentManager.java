package S_M_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentManager {

    public void addStudent(String firstName, String lastName, String studentId) {
        String query = "INSERT INTO Students (FirstName, LastName, StudentID) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.connectToStudentManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String displayStudents() {
        String query = "SELECT * FROM Students";
        StringBuilder result = new StringBuilder();

        try (Connection conn = DatabaseConnection.connectToStudentManager();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                return "No students found.";
            }

            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("StudentID"))
                      .append(", Name: ").append(rs.getString("FirstName"))
                      .append(" ").append(rs.getString("LastName"))
                      .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public String deleteStudent(String identifier) {
        try {
            int studentId = Integer.parseInt(identifier);
            return deleteStudentById(studentId);
        } catch (NumberFormatException e) {
            return deleteStudentByName(identifier);
        }
    }

    private String deleteStudentById(int studentId) {
        String query = "DELETE FROM Students WHERE StudentID = ?";
        try (Connection conn = DatabaseConnection.connectToStudentManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, studentId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return "No student found with StudentID: " + studentId;
            } else {
                return "Student with StudentID " + studentId + " deleted successfully.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while deleting student with StudentID: " + studentId;
        }
    }

    private String deleteStudentByName(String name) {
        String query = "DELETE FROM Students WHERE FirstName = ? OR LastName = ?";
        try (Connection conn = DatabaseConnection.connectToStudentManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return "No student found with Name: " + name;
            } else {
                return "Student(s) with Name " + name + " deleted successfully.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while deleting student(s) with Name: " + name;
        }
    }
}