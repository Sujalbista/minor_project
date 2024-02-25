package com.smartcollege.smartcollege.tableview;

import com.campusflow.campusflow.EntityClass.Faculty;
import com.campusflow.campusflow.database.Database;
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

public class FacultyView {

    private TableColumn<Faculty, String> faculty;
    private TableColumn<Faculty, Integer> did;
    private TableColumn<Faculty, Integer> fid;
    private TableView<Faculty> tableView;


    ObservableList<Faculty> FacultyData() {
        Vector<Faculty> FacultyVector = new Vector<Faculty>();
        Integer did;
        Integer fid;
        String faculty;

        try {
            String sql = "Select * from faculty";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                did = result.getInt("did");
                fid = result.getInt("fid");
                faculty = result.getString("faculty_name");
                Faculty newFaculty = new Faculty(did,fid,faculty);
                FacultyVector.add(newFaculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(FacultyVector);
    }
    public FacultyView(TextField f_field,TextField f_field1,TextField f_field2,TableView<Faculty> FacultyTable, TableColumn<Faculty, Integer> did, TableColumn<Faculty, Integer> fid, TableColumn<Faculty, String> faculty) throws SQLException{
        this.tableView = FacultyTable;
        this.did = did;
        this.fid = fid;
        this.faculty = faculty;

        this.did.setCellValueFactory(new PropertyValueFactory<Faculty, Integer>("did"));
        this.fid.setCellValueFactory(new PropertyValueFactory<Faculty, Integer>("fid"));
        this.faculty.setCellValueFactory(new PropertyValueFactory<Faculty, String>("faculty"));

        tableView.setItems(FacultyData());
        tableView.setRowFactory(tv -> {
            TableRow<Faculty> row = new TableRow<Faculty>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Faculty rowData = row.getItem();
                    f_field.setText(rowData.getFid()+"");
                    f_field1.setText(rowData.getFaculty()+"");
                    f_field2.setText(rowData.getDid()+"");

                }
            });
            return row ;
        });
    }
}
