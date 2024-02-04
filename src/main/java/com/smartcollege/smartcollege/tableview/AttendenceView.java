package com.smartcollege.smartcollege.tableview;

import com.smartcollege.smartcollege.EntityClass.AttendenceStudent;
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

public class AttendenceView {
    private TableColumn<AttendenceStudent, String> aDateComumn;

    private TableColumn<AttendenceStudent, String> aFacultyColumn;

    private TableColumn<AttendenceStudent, String> aNameColumn;

    private TableColumn<AttendenceStudent, String> aSemester;

    private TableColumn<AttendenceStudent, Integer> aSidColumn;

    private TableColumn<AttendenceStudent, String> aTimeColumn;
    private TableView<AttendenceStudent> attendenceTable;

    private String batch;
    private String searchDate;
    ObservableList<AttendenceStudent> studentData(){
        Vector<AttendenceStudent> studentVector = new Vector<AttendenceStudent>();
        Integer id;
        String name;
        String faculty;
        String semester;
        String date;
        String time;
        try{
            String sql = "SELECT faculty.faculty_name,students.first_name,students.middle_name,students.last_name,students.sid,ofSemester,date from `"+this.batch+"attendence` INNER JOIN students on "+this.batch+"attendence.sid=students.sid INNER JOIN batch on batch.bid = students.bid INNER JOIN faculty on faculty.fid = students.fid where date = '"+searchDate+"'";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                name = result.getString("first_name")+" "+result.getString("middle_name")+" "+result.getString("last_name");
                id = result.getInt("sid");
                date = result.getString("date");
                semester = result.getString("ofSemester");
                faculty = result.getString("faculty_name");
                time = "N/A";
                AttendenceStudent newStudent = new AttendenceStudent(id,name,semester,date,faculty,time);
                studentVector.add(newStudent);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }


        return FXCollections.observableArrayList(studentVector);
    }

    public AttendenceView(TableView<AttendenceStudent> table, TableColumn<AttendenceStudent, String> date,TableColumn<AttendenceStudent, String> faculty,TableColumn<AttendenceStudent, String> name,TableColumn<AttendenceStudent, String> semester,TableColumn<AttendenceStudent, Integer> id,TableColumn<AttendenceStudent, String> time){
        this.attendenceTable =table;
        this.aSidColumn = id;
        this.aSemester = semester;
        this.aTimeColumn = time;
        this.aDateComumn = date;
        this.aFacultyColumn = faculty;
        this.aNameColumn = name;

        this.aSidColumn.setCellValueFactory(new PropertyValueFactory<AttendenceStudent,Integer>("stdID"));
        this.aSemester.setCellValueFactory(new PropertyValueFactory<AttendenceStudent,String>("stdSemester"));
        this.aTimeColumn.setCellValueFactory(new PropertyValueFactory<AttendenceStudent,String>("time"));
        this.aDateComumn.setCellValueFactory(new PropertyValueFactory<AttendenceStudent,String>("date"));
        this.aFacultyColumn.setCellValueFactory(new PropertyValueFactory<AttendenceStudent,String>("stdFaculty"));
        this.aNameColumn.setCellValueFactory(new PropertyValueFactory<AttendenceStudent,String>("stdName"));


    }
    public void updateTable(String batch,String date){
        this.searchDate =date;
        this.batch =batch;
        this.attendenceTable.setItems(studentData());
    }
}
