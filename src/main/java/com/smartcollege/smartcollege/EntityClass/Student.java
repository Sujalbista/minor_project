package com.smartcollege.smartcollege.EntityClass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {
    private SimpleIntegerProperty stdID;
    private SimpleStringProperty stdName;
    private SimpleStringProperty stdAddress;
    private SimpleIntegerProperty stdContact;
    private SimpleStringProperty stdEmail;
    private SimpleStringProperty stdFaculty;
    private SimpleStringProperty stdBatch;
    private SimpleIntegerProperty stdParentId;

    public int getStdID() {
        return stdID.get();
    }

    public SimpleIntegerProperty stdIDProperty() {
        return stdID;
    }

    public String getStdName() {
        return stdName.get();
    }

    public SimpleStringProperty stdNameProperty() {
        return stdName;
    }

    public String getStdAddress() {
        return stdAddress.get();
    }

    public SimpleStringProperty stdAddressProperty() {
        return stdAddress;
    }

    public int getStdContact() {
        return stdContact.get();
    }

    public SimpleIntegerProperty stdContactProperty() {
        return stdContact;
    }

    public String getStdEmail() {
        return stdEmail.get();
    }

    public SimpleStringProperty stdEmailProperty() {
        return stdEmail;
    }

    public String getStdFaculty() {
        return stdFaculty.get();
    }

    public SimpleStringProperty stdFacultyProperty() {
        return stdFaculty;
    }

    public String getStdBatch() {
        return stdBatch.get();
    }

    public SimpleStringProperty stdBatchProperty() {
        return stdBatch;
    }

    public int getStdParentId() {
        return stdParentId.get();
    }

    public SimpleIntegerProperty stdParentIdProperty() {
        return stdParentId;
    }

    public void setStdID(int stdID) {
        this.stdID.set(stdID);
    }

    public void setStdName(String stdName) {
        this.stdName.set(stdName);
    }

    public void setStdAddress(String stdAddress) {
        this.stdAddress.set(stdAddress);
    }

    public void setStdContact(int stdContact) {
        this.stdContact.set(stdContact);
    }

    public void setStdEmail(String stdEmail) {
        this.stdEmail.set(stdEmail);
    }

    public void setStdFaculty(String stdFaculty) {
        this.stdFaculty.set(stdFaculty);
    }

    public void setStdBatch(String stdBatch) {
        this.stdBatch.set(stdBatch);
    }

    public void setStdParentId(int stdParentId) {
        this.stdParentId.set(stdParentId);
    }

    public Student(Integer id, String name, String address, Integer contact, String email, String faculty, String batch, Integer parnetID){
        this.stdAddress =new SimpleStringProperty(address);
        this.stdBatch =new SimpleStringProperty(batch);
        this.stdFaculty = new SimpleStringProperty(faculty);
        this.stdName = new SimpleStringProperty(name);
        this.stdID =new SimpleIntegerProperty(id);
        this.stdContact = new SimpleIntegerProperty(contact);
        this.stdParentId = new SimpleIntegerProperty(parnetID);
        this.stdEmail = new SimpleStringProperty(email);


    }
}
