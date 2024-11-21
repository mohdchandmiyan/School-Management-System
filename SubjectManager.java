package S_M_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectManager {

    public void addSubject(String subjectId, String subjectName) {
        String query = "INSERT INTO Subjects (SubjectID, SubjectName) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connectToSubjectManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, subjectId);
            pstmt.setString(2, subjectName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String displaySubjects() {
        String query = "SELECT * FROM Subjects";
        StringBuilder result = new StringBuilder();

        try (Connection conn = DatabaseConnection.connectToSubjectManager();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                return "No subjects found.";
            }

            while (rs.next()) {
                result.append("ID: ").append(rs.getString("SubjectID"))
                      .append(", Name: ").append(rs.getString("SubjectName"))
                      .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving subjects.";
        }

        return result.toString();
    }

    public String deleteSubject(String identifier) {
        try {
            int subjectId = Integer.parseInt(identifier);
            return deleteSubjectById(subjectId);
        } catch (NumberFormatException e) {
            return deleteSubjectByName(identifier);
        }
    }

    private String deleteSubjectById(int subjectId) {
        String query = "DELETE FROM Subjects WHERE SubjectID = ?";
        try (Connection conn = DatabaseConnection.connectToSubjectManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, subjectId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return "No subject found with SubjectID: " + subjectId;
            } else {
                return "Subject with SubjectID " + subjectId + " deleted successfully.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while deleting subject with SubjectID: " + subjectId;
        }
    }

    private String deleteSubjectByName(String name) {
        String query = "DELETE FROM Subjects WHERE SubjectName = ?";
        try (Connection conn = DatabaseConnection.connectToSubjectManager();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return "No subject found with Name: " + name;
            } else {
                return "Subject(s) with Name " + name + " deleted successfully.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while deleting subject(s) with Name: " + name;
        }
    }
}