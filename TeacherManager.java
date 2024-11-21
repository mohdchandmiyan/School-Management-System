package S_M_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherManager {
    
    public void addTeacher( String teacherId,String firstName, String lastName) {
        String query = "INSERT INTO Teachers (FirstName, LastName, TeacherID) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.connectToTeacherManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, teacherId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String displayTeachers() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM Teachers";
        try (Connection conn = DatabaseConnection.connectToTeacherManager();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                return "No teachers found.";
            }

            while (rs.next()) {
                result.append("ID: ").append(rs.getString("TeacherID"))
                      .append(", Name: ").append(rs.getString("FirstName"))
                      .append(" ").append(rs.getString("LastName"))
                      .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving teachers.";
        }
        return result.toString();
    }

    public String deleteTeacher(String identifier) {
        try {
            int teacherId = Integer.parseInt(identifier);
            return deleteTeacherById(teacherId);
        } catch (NumberFormatException e) {
            return deleteTeacherByName(identifier);
        }
    }

    private String deleteTeacherById(int teacherId) {
        String query = "DELETE FROM Teachers WHERE TeacherID = ?";
        try (Connection conn = DatabaseConnection.connectToTeacherManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, teacherId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return "No teacher found with ID: " + teacherId;
            } else {
                return "Teacher with ID " + teacherId + " deleted successfully.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while deleting teacher with ID: " + teacherId;
        }
    }

    private String deleteTeacherByName(String fullName) {
        String[] nameParts = fullName.split(" ", 2);
        if (nameParts.length != 2) {
            return "Please provide both first and last name.";
        }

        String query = "DELETE FROM Teachers WHERE FirstName = ? AND LastName = ?";
        try (Connection conn = DatabaseConnection.connectToTeacherManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nameParts[0]);
            pstmt.setString(2, nameParts[1]);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return "No teacher found with name: " + fullName;
            } else {
                return "Teacher with name " + fullName + " deleted successfully.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while deleting teacher(s) with name: " + fullName;
        }
    }
}