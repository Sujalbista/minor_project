package com.smartcollege.smartcollege.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.smartcollege.smartcollege.database.Database.con;
import static com.smartcollege.smartcollege.database.Database.connected;

public class Notice {
    public static List<String> getStudentEmails() {
        List<String> emails = new ArrayList<>();

        if (connected) {
            try {
                String sql = "SELECT email FROM students";
                PreparedStatement statement = con.prepareStatement(sql);
                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    String email = result.getString("email");
                    emails.add(email);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Database Not Connected!");
        }

        return emails;
    }

    public static List<String> getStudentEmailsByBatchAndSemester(int batchId) {
        List<String> emails = new ArrayList<>();

        if (connected) {
            try {
                String sql = "SELECT email FROM students WHERE bid = ?";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setInt(1, batchId);
                //statement.setString(2, semester);
                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    String email = result.getString("email");
                    emails.add(email);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Database Not Connected!");
        }

        return emails;
    }


}
