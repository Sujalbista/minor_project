package com.smartcollege.smartcollege.tableview;

import com.campusflow.campusflow.EntityClass.Teacher;
import com.campusflow.campusflow.database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TeacherView {
    private Teacher teacher;
    private TableColumn<Teacher, String> tAddress;
    private  TableColumn<Teacher, Long> tContact;
    private  TableColumn<Teacher, String> tEmail;
    private TableColumn<Teacher, Integer> tId;
    private  TableColumn<Teacher,String> tName;
    private TableColumn<Teacher, Integer> tSId;
    private TableColumn<Teacher, Integer> tFId;

    private TableView<Teacher> tableView;
    private String search;





    ObservableList<Teacher> teacherData(){

        Vector<Teacher> teacherVector = new Vector<Teacher>();
        Integer id;
        String name;
        String address;
        Integer contact;
        String email;
        Integer faculty;
        Integer subject;

        try{
            String sql = "SELECT * FROM `teachers`;";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                id = result.getInt("tid");
                name = result.getString("first_name")+" "+result.getString("middle_name")+" "+result.getString("last_name");
                address= result.getString("address");
                contact = result.getInt("contact");
                email = result.getString("email");
                faculty =result.getInt("fid");
                subject = result.getInt("subjectId");


                Teacher newTeacher = new Teacher(id,name,address,contact,email,faculty,subject);
                teacherVector.add(newTeacher);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return (ObservableList<Teacher>) FXCollections.observableArrayList(teacherVector);
    }

    public TeacherView(TableView<Teacher> TeacherTable, TableColumn<Teacher, Integer> tID, TableColumn<Teacher, String> tName, TableColumn<Teacher, String> tAddress, TableColumn<Teacher, Long> tContact, TableColumn<Teacher, String> tEmail, TableColumn<Teacher, Integer> tFId, TableColumn<Teacher, Integer> tSId) {
        this.tableView = TeacherTable;
        this.tEmail= tEmail;
        this.tContact = tContact;
        this.tAddress= tAddress;
        this.tName = tName;
        this.tId = tID;
        this.tFId = tFId;
        this.tSId = tSId;

        this.tName.setCellValueFactory(new PropertyValueFactory<Teacher, String>("tName"));
        this.tId.setCellValueFactory(new PropertyValueFactory<Teacher, Integer>("tId"));
        this.tAddress.setCellValueFactory(new PropertyValueFactory<Teacher, String>("tAddress"));
        this.tContact.setCellValueFactory(new PropertyValueFactory<Teacher, Long>("tContact"));
        this.tEmail.setCellValueFactory(new PropertyValueFactory<Teacher, String>("tEmail"));
        this.tFId.setCellValueFactory(new PropertyValueFactory<Teacher, Integer>("tFId"));
        this.tSId.setCellValueFactory(new PropertyValueFactory<Teacher, Integer>("tSId"));

        tableView.setItems(teacherData());
    }

    ObservableList<Teacher> teacherDataSearch(){

        Vector<Teacher> teacherVector = new Vector<Teacher>();
        Integer id;
        String name;
        String address;
        Integer contact;
        String email;
        Integer faculty;
        Integer subject;

        try{
            String sql = "SELECT * FROM `teachers` WHERE pid = '" + search + "' OR first_name = '" + search + "';";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                id = result.getInt("tid");
                name = result.getString("first_name")+" "+result.getString("middle_name")+" "+result.getString("last_name");
                address= result.getString("address");
                contact = Math.toIntExact(result.getLong("contact"));
                email = result.getString("email");
                faculty =result.getInt("fid");
                subject = result.getInt("subjectId");


                Teacher newTeacher = new Teacher(id,name,address,contact,email,faculty,subject);
                teacherVector.add(newTeacher);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return (ObservableList<Teacher>) FXCollections.observableArrayList(teacherVector);
    }

    public TeacherView(String search, TableView<Teacher> TeacherTable, TableColumn<Teacher, Integer> tID, TableColumn<Teacher, String> tName, TableColumn<Teacher, String> tAddress, TableColumn<Teacher, Long> tContact, TableColumn<Teacher, String> tEmail, TableColumn<Teacher, Integer> tFId, TableColumn<Teacher, Integer> tSId) {
        this.tableView = TeacherTable;
        this.tEmail= tEmail;
        this.tContact = tContact;
        this.tAddress= tAddress;
        this.tName = tName;
        this.tId = tID;
        this.tFId = tFId;
        this.tSId = tSId;
        this.search = search;

        this.tName.setCellValueFactory(new PropertyValueFactory<Teacher, String>("tName"));
        this.tId.setCellValueFactory(new PropertyValueFactory<Teacher, Integer>("tId"));
        this.tAddress.setCellValueFactory(new PropertyValueFactory<Teacher, String>("tAddress"));
        this.tContact.setCellValueFactory(new PropertyValueFactory<Teacher, Long>("tContact"));
        this.tEmail.setCellValueFactory(new PropertyValueFactory<Teacher, String>("tEmail"));
        this.tFId.setCellValueFactory(new PropertyValueFactory<Teacher, Integer>("tFId"));
        this.tSId.setCellValueFactory(new PropertyValueFactory<Teacher, Integer>("tSId"));

        tableView.setItems(teacherData());
    }

    public TeacherView(String search) throws SQLException {
        String sql = "SELECT * FROM `teachers` WHERE tid = '" + search + "' OR first_name = '" + search + "'";
        try (PreparedStatement statement = Database.con.prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Integer id = result.getInt("tid");
                String firstName = result.getString("first_name");
                String middleName = result.getString("middle_name");
                String lastName = result.getString("last_name");
                String address = result.getString("address");
                Integer contact = Math.toIntExact(result.getLong("contact"));
                String email = result.getString("email");
                Integer faculty = result.getInt("fid");
                Integer subject = result.getInt("subjectId");

                teacher = new Teacher(id, firstName,middleName,lastName, address, contact, email, faculty, subject);
            }
        }
    }

    public Teacher getTeacher() {
        return teacher;
    }
}
