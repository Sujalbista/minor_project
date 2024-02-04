package com.smartcollege.smartcollege.EntityClass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
public class Parent {
    private SimpleIntegerProperty stdID;
    private SimpleStringProperty pName;
    private SimpleStringProperty pAddress;
    private SimpleIntegerProperty pContact;
    private SimpleStringProperty pEmail;
    private SimpleIntegerProperty pId;


    public int getStdID() {
        return stdID.get();
    }

    public SimpleIntegerProperty stdIDProperty() {
        return stdID;
    }

    public String getStdName() {
        return pName.get();
    }

    public SimpleStringProperty stdNameProperty() {
        return pName;
    }

    public String getStdAddress() {
        return pAddress.get();
    }

    public SimpleStringProperty stdAddressProperty() {
        return pAddress;
    }

    public int getStdContact() {
        return pContact.get();
    }

    public SimpleIntegerProperty stdContactProperty() {
        return pContact;
    }

    public String getStdEmail() {
        return pEmail.get();
    }

    public SimpleStringProperty stdEmailProperty() {
        return pEmail;
    }

    public int getStdParentId() {
        return pId.get();
    }

    public SimpleIntegerProperty stdParentIdProperty() {
        return pId;
    }

    public void setStdID(int stdID) {
        this.stdID.set(stdID);
    }

    public void setStdName(String stdName) {
        this.pName.set(stdName);
    }

    public void setStdAddress(String stdAddress) {
        this.pAddress.set(stdAddress);
    }

    public void setStdContact(int stdContact) {
        this.pContact.set(stdContact);
    }

    public void setStdEmail(String stdEmail) {
        this.pEmail.set(stdEmail);
    }

    public void setStdParentId(int stdParentId) {
        this.pId.set(stdParentId);
    }

    public Parent(Integer id, String name, String address, Integer contact, String email, Integer parnetID){
        this.pAddress =new SimpleStringProperty(address);
        this.pName = new SimpleStringProperty(name);
        this.stdID =new SimpleIntegerProperty(id);
        this.pContact = new SimpleIntegerProperty(contact);
        this.pId = new SimpleIntegerProperty(parnetID);
        this.pEmail = new SimpleStringProperty(email);
    }
}
