package com.smartcollege.smartcollege.tableview;

import com.smartcollege.smartcollege.EntityClass.Student;
import com.smartcollege.smartcollege.database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class MarksStudentSearch {

    private TableColumn<Student, String> stdSemester;
    private TableColumn<Student,String> stdBatch;
    private  TableColumn<Student,String> stdName;
    private TableColumn<Student,Integer> stdID;
    private TableView<Student> tableView;
    private String search;
    private TextField mStdID;
    ObservableList<Student> studentData(){
        Vector<Student> studentVector = new Vector<Student>();
        Integer id;
        String name;
        String batch;
        String semester;
        try{
            String sql = "SELECT * FROM `students` INNER JOIN batch on batch.bid = students.bid INNER JOIN faculty on faculty.fid = batch.fid where students.first_name LIKE \"%"+search+"%\" OR students.middle_name LIKE \"%"+search+"%\" OR students.last_name LIKE \"%"+search+"%\";";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                id = result.getInt("sid");
                name = result.getString("first_name")+" "+result.getString("middle_name")+" "+result.getString("last_name");
                batch = result.getString("year");
                semester = result.getString("semester");

                Student newStudent = new Student(id,name,batch,semester);
                studentVector.add(newStudent);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }


        return FXCollections.observableArrayList(studentVector);
    }
    public MarksStudentSearch(TextField mStdID, TableView<Student> studentTable, TableColumn<Student, Integer> stdID, TableColumn<Student, String> stdName, TableColumn<Student, String> stdBatch, TableColumn<Student, String> stdSemester) {
        this.tableView = studentTable;
        this.stdBatch = stdBatch;
        this.stdSemester= stdSemester;
        this.stdName = stdName;
        this.stdID = stdID;
        this.search = mStdID.getText();
        this.mStdID = mStdID;

        this.stdName.setCellValueFactory(new PropertyValueFactory<Student, String>("stdName"));
        this.stdID.setCellValueFactory(new PropertyValueFactory<Student, Integer>("stdID"));
        this.stdBatch.setCellValueFactory(new PropertyValueFactory<Student, String>("stdBatch"));
        this.stdSemester.setCellValueFactory(new PropertyValueFactory<Student, String>("stdSemester"));

        tableView.setItems(studentData());

        tableView.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<Student>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Student rowData = row.getItem();
                    mStdID.setText(rowData.getStdID()+"");
                }
            });
            return row ;
        });
    }





}
