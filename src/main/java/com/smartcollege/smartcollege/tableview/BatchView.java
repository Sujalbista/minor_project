package com.smartcollege.smartcollege.tableview;

import com.smartcollege.smartcollege.EntityClass.Batch;
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

public class BatchView {
    private static Integer id;
    private TableColumn<Batch, String> semester;
    private TableColumn<Batch, String> year;
    private TableColumn<Batch, Integer> bid;
    private TableColumn<Batch, Integer> fid;
    private TableView<Batch> tableView;

    ObservableList<Batch> batchData() {
        Vector<Batch> batchVector = new Vector<Batch>();
        Integer bid;
        Integer year;
        Integer fid;
        String semester;

        try {
            String sql = "Select * from batch";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                bid = result.getInt("bid");
                semester = result.getString("semester");
                fid = result.getInt("fid");
                year = result.getInt("year");

                Batch newBatch = new Batch(bid,semester, fid, year);
                batchVector.add(newBatch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(batchVector);
    }
    public BatchView(TextField b_Id, TextField b_field, TextField b_field1, TextField b_field2, TableView<Batch> batchTable, TableColumn<Batch, Integer> bid, TableColumn<Batch, String> semester, TableColumn<Batch, Integer> fid, TableColumn<Batch, String> year) throws SQLException{
        this.tableView = batchTable;
        this.bid = bid;
        this.semester = semester;
        this.fid = fid;
        this.year = year;

        this.bid.setCellValueFactory(new PropertyValueFactory<Batch, Integer>("bid"));
        this.semester.setCellValueFactory(new PropertyValueFactory<Batch, String>("semester"));
        this.fid.setCellValueFactory(new PropertyValueFactory<Batch, Integer>("fid"));
        this.year.setCellValueFactory(new PropertyValueFactory<Batch, String>("year"));

        tableView.setItems(batchData());

        tableView.setRowFactory(tv -> {
            TableRow<Batch> row = new TableRow<Batch>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Batch rowData = row.getItem();
                    b_field.setText(rowData.getYear()+"");
                    b_field1.setText(rowData.getFid()+"");
                    b_field2.setText(rowData.getSemester()+"");
                    b_Id.setText(rowData.getBid()+"");
                }
            });
            return row ;
        });
    }
}
