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

    private TableColumn<Student, String> stdAddress;
    private  TableColumn<Student, Integer> stdContact;
    private  TableColumn<Student, String> stdEmail;
    private TableColumn<Student, Integer> stdParentId;
    private TableColumn<Student, String> stdFaculty;
    private TableColumn<Student,String> stdBatch;
    private  TableColumn<Student,String> stdName;
    private TableColumn<Student,Integer> stdID;
    private TableView<Student> tableView;
    ObservableList<Student> studentData(){
        Vector<Student> studentVector = new Vector<Student>();
        Integer id;
        String name;
        String address;
        Integer contact;
        String email;
        String faculty;
        String batch;
        Integer parentId;

        try{
            String sql = "SELECT * FROM `students` INNER JOIN batch on batch.bid = students.bid INNER JOIN faculty on faculty.fid = batch.fid;";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                id = result.getInt("sid");
                name = result.getString("first_name")+" "+result.getString("middle_name")+" "+result.getString("last_name");
                address= result.getString("address");
                contact = Math.toIntExact(result.getLong("contact"));
                email = result.getString("email");
                faculty = result.getString("faculty_name");
                batch = result.getString("year");
                parentId = result.getInt("pid");

                Student newStudent = new Student(id,name,address,contact,email,faculty,batch,parentId);
                studentVector.add(newStudent);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(studentVector);
    }
    public StudentView(javafx.scene.control.TableView<Student> studentTable, javafx.scene.control.TableColumn<Student, Integer> stdID, javafx.scene.control.TableColumn<Student, String> stdName, javafx.scene.control.TableColumn<Student, String> stdAddress, javafx.scene.control.TableColumn<Student, Integer> stdContact, javafx.scene.control.TableColumn<Student, String> stdEmail, javafx.scene.control.TableColumn<Student, String> stdFaculty, javafx.scene.control.TableColumn<Student, String> stdBatch, javafx.scene.control.TableColumn<Student, Integer> stdParentId) {
        this.tableView = studentTable;
        this.stdParentId = stdParentId;
        this.stdBatch = stdBatch;
        this.stdFaculty= stdFaculty;
        this.stdEmail= stdEmail;
        this.stdContact = stdContact;
        this.stdAddress= stdAddress;
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





}
