package S_M_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassManager {

    public void addClass(String classId,String className) {
        String query = "INSERT INTO Classes (ClassName, ClassID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connectToClassManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, className);
            pstmt.setString(2, classId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String displayClasses() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM Classes";
        try (Connection conn = DatabaseConnection.connectToClassManager();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                return "No classes found.";
            }

            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("ClassID"))
                      .append(", Class Name: ").append(rs.getString("ClassName"))
                      .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving classes.";
        }
        return result.toString();
    }

    public String deleteClass(String identifier) {
        try {
            int classId = Integer.parseInt(identifier);
            return deleteClassById(classId);
        } catch (NumberFormatException e) {
            return deleteClassByName(identifier);
        }
    }

    private String deleteClassById(int classId) {
        String query = "DELETE FROM Classes WHERE ClassID = ?";
        try (Connection conn = DatabaseConnection.connectToClassManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, classId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return "No class found with ID: " + classId;
            } else {
                return "Class with ID " + classId + " deleted successfully.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while deleting class with ID: " + classId;
        }
    }

    private String deleteClassByName(String className) {
        String query = "DELETE FROM Classes WHERE ClassName = ?";
        try (Connection conn = DatabaseConnection.connectToClassManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, className);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return "No class found with name: " + className;
            } else {
                return "Class with name \"" + className + "\" deleted successfully.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while deleting class with name: " + className;
        }
    }
}
