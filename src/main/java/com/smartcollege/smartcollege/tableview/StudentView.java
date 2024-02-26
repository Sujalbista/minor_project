package com.smartcollege.smartcollege.tableview;

import com.smartcollege.smartcollege.EntityClass.Student;
import com.smartcollege.smartcollege.database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class StudentView {

    private Student student;

    private TableColumn<Student, String> stdAddress;
    private TableColumn<Student, Integer> stdContact;
    private TableColumn<Student, String> stdEmail;
    private TableColumn<Student, Integer> stdParentId;
    private TableColumn<Student, String> stdFaculty;
    private TableColumn<Student, String> stdBatch;
    private TableColumn<Student, String> stdName;
    private TableColumn<Student, Integer> stdID;
    private TableView<Student> tableView;
    private String search;

    ObservableList<Student> studentData() {
        Vector<Student> studentVector = new Vector<Student>();
        Integer id;
        String name;
        String address;
        Long contact;
        String email;
        String faculty;
        String batch;
        Integer parentId;

        try {
            String sql = "SELECT * FROM `students` INNER JOIN batch on batch.bid = students.bid INNER JOIN faculty on faculty.fid = batch.fid;";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                id = result.getInt("sid");
                name = result.getString("first_name") + " " + result.getString("middle_name") + " " + result.getString("last_name");
                address = result.getString("address");
                contact = result.getLong("contact");
                email = result.getString("email");
                faculty = result.getString("faculty_name");
                batch = result.getString("year");
                parentId = result.getInt("pid");

                Student newStudent = new Student(id, name, address, contact, email, faculty, batch, parentId);
                studentVector.add(newStudent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(studentVector);
    }
    ObservableList<Student> studentDataSearch() {
        Vector<Student> studentVector = new Vector<Student>();
        Integer id;
        String name;
        String address;
        Long contact;
        String email;
        String faculty;
        String batch;
        Integer parentId;

        try {
            String sql = "SELECT * FROM `students` INNER JOIN batch on batch.bid = students.bid INNER JOIN faculty on faculty.fid = batch.fid WHERE sid = '" + search + "' OR first_name = '" + search + "'";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                id = result.getInt("sid");
                name = result.getString("first_name") + " " + result.getString("middle_name") + " " + result.getString("last_name");
                address = result.getString("address");
                contact = result.getLong("contact");
                email = result.getString("email");
                faculty = result.getString("faculty_name");
                batch = result.getString("year");
                parentId = result.getInt("pid");

                Student newStudent = new Student(id, name, address, contact, email, faculty, batch, parentId);
                studentVector.add(newStudent);

                System.out.println(name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(studentVector);
    }

    public StudentView(TableView<Student> studentTable, TableColumn<Student, Integer> stdID, TableColumn<Student, String> stdName, TableColumn<Student, String> stdAddress, TableColumn<Student, Integer> stdContact, TableColumn<Student, String> stdEmail, TableColumn<Student, String> stdFaculty, TableColumn<Student, String> stdBatch, TableColumn<Student, Integer> stdParentId) throws SQLException {
        this.tableView = studentTable;
        this.stdParentId = stdParentId;
        this.stdBatch = stdBatch;
        this.stdFaculty = stdFaculty;
        this.stdEmail = stdEmail;
        this.stdContact = stdContact;
        this.stdAddress = stdAddress;
        this.stdName = stdName;
        this.stdID = stdID;


        this.stdName.setCellValueFactory(new PropertyValueFactory<Student, String>("stdName"));
        this.stdID.setCellValueFactory(new PropertyValueFactory<Student, Integer>("stdID"));
        this.stdAddress.setCellValueFactory(new PropertyValueFactory<Student, String>("stdAddress"));
        this.stdContact.setCellValueFactory(new PropertyValueFactory<Student, Integer>("stdContact"));
        this.stdEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("stdEmail"));
        this.stdFaculty.setCellValueFactory(new PropertyValueFactory<Student, String>("stdFaculty"));
        this.stdBatch.setCellValueFactory(new PropertyValueFactory<Student, String>("stdBatch"));
        this.stdParentId.setCellValueFactory(new PropertyValueFactory<Student, Integer>("stdParentId"));

        tableView.setItems(studentData());
    }


    public StudentView(String search, TableView<Student> studentTable, TableColumn<Student, Integer> stdID, TableColumn<Student, String> stdName, TableColumn<Student, String> stdAddress, TableColumn<Student, Integer> stdContact, TableColumn<Student, String> stdEmail, TableColumn<Student, String> stdFaculty, TableColumn<Student, String> stdBatch, TableColumn<Student, Integer> stdParentId) throws SQLException {
        this.tableView = studentTable;
        this.stdParentId = stdParentId;
        this.stdBatch = stdBatch;
        this.stdFaculty = stdFaculty;
        this.stdEmail = stdEmail;
        this.stdContact = stdContact;
        this.stdAddress = stdAddress;
        this.stdName = stdName;
        this.stdID = stdID;
        this.search = search;

        System.out.println(this.search);

        this.stdName.setCellValueFactory(new PropertyValueFactory<Student, String>("stdName"));
        this.stdID.setCellValueFactory(new PropertyValueFactory<Student, Integer>("stdID"));
        this.stdAddress.setCellValueFactory(new PropertyValueFactory<Student, String>("stdAddress"));
        this.stdContact.setCellValueFactory(new PropertyValueFactory<Student, Integer>("stdContact"));
        this.stdEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("stdEmail"));
        this.stdFaculty.setCellValueFactory(new PropertyValueFactory<Student, String>("stdFaculty"));
        this.stdBatch.setCellValueFactory(new PropertyValueFactory<Student, String>("stdBatch"));
        this.stdParentId.setCellValueFactory(new PropertyValueFactory<Student, Integer>("stdParentId"));

        tableView.setItems(studentDataSearch());
    }

    public StudentView(String search) {
        String sql = "SELECT * FROM `students` INNER JOIN batch on batch.bid = students.bid INNER JOIN faculty on faculty.fid = batch.fid WHERE sid = '" + search + "' OR first_name = '" + search + "'";
        try (PreparedStatement statement = Database.con.prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int id = result.getInt("sid");
                String firstName = result.getString("first_name");
                String middleName = result.getString("middle_name");
                String lastName = result.getString("last_name");
                String fullname= firstName + middleName + lastName;
                String address = result.getString("address");
                Long contact = result.getLong("contact");
                String email = result.getString("email");
                String faculty = result.getString("fid");
                String batch = result.getString("bid");
                int parentId = result.getInt("pid");
                float entrancescore = result.getInt("entrance_score");


                student = new Student(id,firstName,middleName,lastName , address, contact, email, faculty, batch, parentId,entrancescore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Student getStudent() {
        return student;
    }
}
