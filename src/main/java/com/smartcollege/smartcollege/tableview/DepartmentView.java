package com.smartcollege.smartcollege.tableview;

import com.campusflow.campusflow.EntityClass.Department;
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

public class DepartmentView {
    private TableColumn<Department, String> Department;
    private TableColumn<Department, Integer> did;
    private TableColumn<Department, Integer> hod;
    private TableView<Department> tableView;

    ObservableList<Department> DepartmentData() {
        Vector<Department> DepartmentVector = new Vector<Department>();
        Integer did;
        Integer hod;
        String Department;

        try {
            String sql = "Select * from department";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                did = result.getInt("did");
                Department = result.getString("name");
                hod = result.getInt("hod");
                Department newDepartment = new Department(did,hod,Department);
                DepartmentVector.add(newDepartment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(DepartmentVector);
    }
    public DepartmentView(TextField f_field,TextField f_field1,TextField f_field2, TableView<Department> departmentTable, TableColumn<Department, Integer> did, TableColumn<Department, String> Department, TableColumn<Department, Integer> hod) throws SQLException{
        this.tableView = departmentTable;
        this.did = did;
        this.hod = hod;
        this.Department = Department;

        this.did.setCellValueFactory(new PropertyValueFactory<Department, Integer>("did"));
        this.hod.setCellValueFactory(new PropertyValueFactory<Department, Integer>("hod"));
        this.Department.setCellValueFactory(new PropertyValueFactory<Department, String>("department"));

        tableView.setItems(DepartmentData());
        tableView.setRowFactory(tv -> {
            TableRow<Department> row = new TableRow<Department>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Department rowData = row.getItem();
                    f_field.setText(rowData.getDid()+"");
                    f_field1.setText(rowData.getHod()+"");
                    f_field2.setText(rowData.getDepartment()+"");
                }
            });
            return row ;
        });
    }
}
