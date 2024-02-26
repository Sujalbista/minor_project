package com.smartcollege.smartcollege.database;

import com.smartcollege.smartcollege.EmailSender;
import com.smartcollege.smartcollege.utils.HtmlMaker;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
                String sql = "SELECT email FROM `students` INNER JOIN batch on batch.bid = students.bid WHERE batch.bid = " + batchId + "";

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

    public static String sendMarksheetToParents(String batch,String term,String sem){
        String feedback = null;
        if(connected){
            try{
                String studentSql = "SELECT students.sid,parents.pid,students.first_name as sname,parents.email as pemail, students.email as semail,parents.first_name as pname from students inner join parents on parents.pid = students.pid where bid="+batch;
                PreparedStatement studentStatement = con.prepareStatement(studentSql);
                ResultSet students = studentStatement.executeQuery();

                while(students.next()){
                    String marksSql = "SELECT marks, sub_name from "+batch+"marksheet inner join subjects on subjects.subId = "+batch+"marksheet.subId where "+batch+"marksheet.semester = '"+sem+"' and term = '"+term+"' and sid="+students.getInt("sid")+";";
                    PreparedStatement marksStatement = con.prepareStatement(marksSql);
                    ResultSet marks = marksStatement.executeQuery();
                    System.out.println("Fetching marks of one student!");
                    String htmlcontent = HtmlMaker.createMarksheetHtml(term,sem,students.getString("sname"),students.getString("pname"),marks);
                    Address[] toAddress = {new InternetAddress(students.getString("semail")),
                            new InternetAddress(students.getString("pemail"))};
                    EmailSender.sendMarksheet(toAddress,"Examination result",htmlcontent);
                }
                feedback = "Done";
            }catch (SQLException e){
                feedback = e.toString();
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        }
        return feedback;
    }

}
