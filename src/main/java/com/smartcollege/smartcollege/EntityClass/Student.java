package com.smartcollege.smartcollege.EntityClass;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {
    private  SimpleFloatProperty stdEntranceScore;
    private  SimpleStringProperty stdMiddlemnane;
    private  SimpleStringProperty stdLastname;
    private  SimpleStringProperty stdFirstname;
    private SimpleIntegerProperty stdID;
    private SimpleStringProperty stdName;
    private SimpleStringProperty stdAddress;
    private SimpleLongProperty stdContact;
    private SimpleStringProperty stdEmail;
    private SimpleStringProperty stdFaculty;
    private SimpleStringProperty stdBatch;
    private SimpleIntegerProperty stdParentId;
    private SimpleStringProperty stdSemester;

    public float getStdEntranceScore() {
        return stdEntranceScore.get();
    }

    public SimpleFloatProperty stdEntranceScoreProperty() {
        return stdEntranceScore;
    }

    public void setStdEntranceScore(float stdEntranceScore) {
        this.stdEntranceScore.set(stdEntranceScore);
    }

    public String getStdMiddlemnane() {
        return stdMiddlemnane.get();
    }

    public SimpleStringProperty stdMiddlemnaneProperty() {
        return stdMiddlemnane;
    }

    public void setStdMiddlemnane(String stdMiddlemnane) {
        this.stdMiddlemnane.set(stdMiddlemnane);
    }

    public String getStdLastname() {
        return stdLastname.get();
    }

    public SimpleStringProperty stdLastnameProperty() {
        return stdLastname;
    }

    public void setStdLastname(String stdLastname) {
        this.stdLastname.set(stdLastname);
    }

    public String getStdFirstname() {
        return stdFirstname.get();
    }

    public SimpleStringProperty stdFirstnameProperty() {
        return stdFirstname;
    }

    public void setStdFirstname(String stdFirstname) {
        this.stdFirstname.set(stdFirstname);
    }

    public int getStdID() {
        return stdID.get();
    }

    public SimpleIntegerProperty stdIDProperty() {
        return stdID;
    }

    public void setStdID(int stdID) {
        this.stdID.set(stdID);
    }

    public String getStdName() {
        return stdName.get();
    }

    public SimpleStringProperty stdNameProperty() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName.set(stdName);
    }

    public String getStdAddress() {
        return stdAddress.get();
    }

    public SimpleStringProperty stdAddressProperty() {
        return stdAddress;
    }

    public void setStdAddress(String stdAddress) {
        this.stdAddress.set(stdAddress);
    }

    public long getStdContact() {
        return stdContact.get();
    }

    public SimpleLongProperty stdContactProperty() {
        return stdContact;
    }

    public void setStdContact(long stdContact) {
        this.stdContact.set(stdContact);
    }

    public String getStdEmail() {
        return stdEmail.get();
    }

    public SimpleStringProperty stdEmailProperty() {
        return stdEmail;
    }

    public void setStdEmail(String stdEmail) {
        this.stdEmail.set(stdEmail);
    }

    public String getStdFaculty() {
        return stdFaculty.get();
    }

    public SimpleStringProperty stdFacultyProperty() {
        return stdFaculty;
    }

    public void setStdFaculty(String stdFaculty) {
        this.stdFaculty.set(stdFaculty);
    }

    public String getStdBatch() {
        return stdBatch.get();
    }

    public SimpleStringProperty stdBatchProperty() {
        return stdBatch;
    }

    public void setStdBatch(String stdBatch) {
        this.stdBatch.set(stdBatch);
    }

    public int getStdParentId() {
        return stdParentId.get();
    }

    public SimpleIntegerProperty stdParentIdProperty() {
        return stdParentId;
    }

    public void setStdParentId(int stdParentId) {
        this.stdParentId.set(stdParentId);
    }

    public String getStdSemester() {
        return stdSemester.get();
    }

    public SimpleStringProperty stdSemesterProperty() {
        return stdSemester;
    }

    public void setStdSemester(String stdSemester) {
        this.stdSemester.set(stdSemester);
    }

    public Student(Integer id, String name, String address, Long contact, String email, String faculty, String batch, Integer parnetID){
        this.stdAddress =new SimpleStringProperty(address);
        this.stdBatch =new SimpleStringProperty(batch);
        this.stdFaculty = new SimpleStringProperty(faculty);
        this.stdName = new SimpleStringProperty(name);
        this.stdID =new SimpleIntegerProperty(id);
        this.stdContact = new SimpleLongProperty(contact);
        this.stdParentId = new SimpleIntegerProperty(parnetID);
        this.stdEmail = new SimpleStringProperty(email);
    }
    public Student(Integer id, String name, String batch, String semester){
        this.stdBatch =new SimpleStringProperty(batch);
        this.stdName = new SimpleStringProperty(name);
        this.stdID =new SimpleIntegerProperty(id);
        this.stdSemester = new SimpleStringProperty(semester);
    }
    public Student(int id, String firstName, String middleName, String lastName, String address, Long contact, String email, String faculty, String batch, int parentId, float entrancescore) {
        this.stdAddress =new SimpleStringProperty(address);
        this.stdBatch =new SimpleStringProperty(batch);
        this.stdFaculty = new SimpleStringProperty(faculty);
        this.stdFirstname = new SimpleStringProperty(firstName);
        this.stdMiddlemnane = new SimpleStringProperty(middleName);
        this.stdLastname = new SimpleStringProperty(lastName);
        this.stdID =new SimpleIntegerProperty(id);
        this.stdContact = new SimpleLongProperty(contact);
        this.stdParentId = new SimpleIntegerProperty(parentId);
        this.stdEmail = new SimpleStringProperty(email);
        this.stdEntranceScore =new SimpleFloatProperty(entrancescore);
    }
}
