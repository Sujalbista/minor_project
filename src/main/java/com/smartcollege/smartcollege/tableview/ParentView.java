package com.smartcollege.smartcollege.tableview;

import com.campusflow.campusflow.EntityClass.Parent;
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

public class ParentView {
    private Parent parent;
    private TableColumn<Parent, String> pAddress;
    private  TableColumn<Parent, Long> pContact;
    private  TableColumn<Parent, String> pEmail;
    private TableColumn<Parent, Integer> pId;
    private  TableColumn<Parent,String> pName;
    private TableView<Parent> tableView;
    private String search;

    ObservableList<Parent> parentData(){

        Vector<Parent> parentVector = new Vector<Parent>();
        Integer id;
        String name;
        String address;
        Integer contact;
        String email;

        try{
            String sql = "SELECT * FROM `parents`;";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                id = result.getInt("pid");
                name = result.getString("first_name")+" "+result.getString("middle_name")+" "+result.getString("last_name");
                address= result.getString("address");
                contact = Math.toIntExact(result.getLong("contact"));
                email = result.getString("email");

                Parent newParent = new Parent(id,name,address,contact,email);
                parentVector.add(newParent);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return (ObservableList<Parent>) FXCollections.observableArrayList(parentVector);
    }

    public ParentView(TableView<Parent> ParentTable, TableColumn<Parent, Integer> pID, TableColumn<Parent, String> pName, TableColumn<Parent, String> pAddress, TableColumn<Parent, Long> pContact, TableColumn<Parent, String> pEmail) {
        this.tableView = ParentTable;
        this.pEmail= pEmail;
        this.pContact = pContact;
        this.pAddress= pAddress;
        this.pName = pName;
        this.pId = pID;


        this.pName.setCellValueFactory(new PropertyValueFactory<Parent, String>("pName"));
        this.pId.setCellValueFactory(new PropertyValueFactory<Parent, Integer>("pId"));
        this.pAddress.setCellValueFactory(new PropertyValueFactory<Parent, String>("pAddress"));
        this.pContact.setCellValueFactory(new PropertyValueFactory<Parent, Long>("pContact"));
        this.pEmail.setCellValueFactory(new PropertyValueFactory<Parent, String>("pEmail"));

        tableView.setItems(parentData());
    }

    ObservableList<Parent> parentDataSearch(){

        Vector<Parent> parentVector = new Vector<Parent>();
        Integer id;
        String name;
        String address;
        Integer contact;
        String email;

        try{
            String sql = "SELECT * FROM `parents`WHERE pid = '" + search + "' OR first_name = '" + search + "';";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                id = result.getInt("pid");
                name = result.getString("first_name")+" "+result.getString("middle_name")+" "+result.getString("last_name");
                address= result.getString("address");
                contact = Math.toIntExact(result.getLong("contact"));
                email = result.getString("email");

                Parent newParent = new Parent(id,name,address,contact,email);
                parentVector.add(newParent);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return (ObservableList<Parent>) FXCollections.observableArrayList(parentVector);
    }
    public ParentView(String search, TableView<Parent> ParentTable, TableColumn<Parent, Integer> pID, TableColumn<Parent, String> pName, TableColumn<Parent, String> pAddress, TableColumn<Parent, Long> pContact, TableColumn<Parent, String> pEmail) {
        this.tableView = ParentTable;
        this.pEmail= pEmail;
        this.pContact = pContact;
        this.pAddress= pAddress;
        this.pName = pName;
        this.pId = pID;
        this.search= search;


        this.pName.setCellValueFactory(new PropertyValueFactory<Parent, String>("pName"));
        this.pId.setCellValueFactory(new PropertyValueFactory<Parent, Integer>("pId"));
        this.pAddress.setCellValueFactory(new PropertyValueFactory<Parent, String>("pAddress"));
        this.pContact.setCellValueFactory(new PropertyValueFactory<Parent, Long>("pContact"));
        this.pEmail.setCellValueFactory(new PropertyValueFactory<Parent, String>("pEmail"));

        tableView.setItems(parentDataSearch());
    }
    public ParentView(String search) {
        String sql = "SELECT * FROM `parents` WHERE pid = '" + search + "' OR first_name = '" + search + "'";
        try (PreparedStatement statement = Database.con.prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int id = result.getInt("pid");
                String firstName = result.getString("first_name");
                String middleName = result.getString("middle_name");
                String lastName = result.getString("last_name");
                String address = result.getString("address");
                Integer contact = Math.toIntExact(result.getLong("contact"));
                String email = result.getString("email");
                parent = new Parent(id,firstName,middleName,lastName , address, contact,email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Parent getParent() {
        return parent;
    }
}


