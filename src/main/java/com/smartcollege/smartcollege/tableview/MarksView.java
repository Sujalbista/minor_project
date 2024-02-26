package com.smartcollege.smartcollege.tableview;

import com.smartcollege.smartcollege.EntityClass.Marks;
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

public class MarksView {
    private TableView<Marks> mvTable;

    private TableColumn<Marks, Integer> mvtMID;

    private TableColumn<Marks, Float> mvtMarks;

    private TableColumn<Marks, String> mvtName;

    private TableColumn<Marks, String> mvtSemester;

    private TableColumn<Marks, String> mvtSubject;

    private TableColumn<Marks, String> mvtTerminal;

    private String bid;
    private String term;
    private String sem;



    ObservableList<Marks> marksData(){

        Vector<Marks> parentVector = new Vector<Marks>();
        int tMid;
        String tName;
        String tTerminal;
        String tSemester;
        String tSubject;
        Float tMarks;


        try{
            String sql = "SELECT students.first_name,students.last_name,term,"+bid+"marksheet.semester,sub_name,marks,mid from `"+bid+"marksheet` inner join students on "+bid+"marksheet.sid = students.sid inner join subjects on subjects.subId = "+bid+"marksheet.subId where "+bid+"marksheet.semester='"+sem+"' and term='"+term+"'";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                tName = result.getString("first_name")+" "+result.getString("last_name");
                tTerminal = result.getString("term");
                tSemester = result.getString("semester");
                tSubject = result.getString("sub_name");
                tMarks = result.getFloat("marks");
                tMid = result.getInt("mid");

                Marks newMarks = new Marks(tMid,tName,tSemester,tTerminal,tSubject,tMarks);
                parentVector.add(newMarks);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return (ObservableList<Marks>) FXCollections.observableArrayList(parentVector);
    }
    public MarksView(TableView<Marks> mvTable, TableColumn<Marks, Integer> mvtMID, TableColumn<Marks, Float> mvtMarks, TableColumn<Marks, String> mvtName, TableColumn<Marks, String> mvtSemester, TableColumn<Marks, String> mvtSubject, TableColumn<Marks, String> mvtTerminal, String bid, String term, String sem) {
        this.mvTable = mvTable;
        this.mvtMID = mvtMID;
        this.mvtMarks = mvtMarks;
        this.mvtName = mvtName;
        this.mvtSemester = mvtSemester;
        this.mvtSubject = mvtSubject;
        this.mvtTerminal = mvtTerminal;
        this.bid = bid;
        this.term = term;
        this.sem = sem;

        this.mvtMID.setCellValueFactory(new PropertyValueFactory<Marks,Integer>("mid"));
        this.mvtName.setCellValueFactory(new PropertyValueFactory<Marks,String>("name"));
        this.mvtMarks.setCellValueFactory(new PropertyValueFactory<Marks,Float>("marks"));
        this.mvtSemester.setCellValueFactory(new PropertyValueFactory<Marks,String>("semester"));
        this.mvtTerminal.setCellValueFactory(new PropertyValueFactory<Marks,String>("terminal"));
        this.mvtSubject.setCellValueFactory(new PropertyValueFactory<Marks,String>("subject"));

        this.mvTable.getItems().addAll(marksData());
    }

}
