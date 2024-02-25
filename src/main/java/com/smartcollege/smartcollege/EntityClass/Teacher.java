package com.smartcollege.smartcollege.EntityClass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Teacher {
    private SimpleStringProperty tfirst_name;
    private SimpleStringProperty tmiddle_name;
    private SimpleStringProperty tlast_name;
    private SimpleStringProperty tName;
    private SimpleStringProperty tAddress;
    private SimpleIntegerProperty tContact;
    private SimpleStringProperty tEmail;
    private SimpleIntegerProperty tId;
    private  SimpleIntegerProperty tFId;
    private SimpleIntegerProperty tSId;

    public String getTfirst_name() {
        return tfirst_name.get();
    }

    public SimpleStringProperty tfirst_nameProperty() {
        return tfirst_name;
    }

    public void setTfirst_name(String tfirst_name) {
        this.tfirst_name.set(tfirst_name);
    }

    public String getTmiddle_name() {
        return tmiddle_name.get();
    }

    public SimpleStringProperty tmiddle_nameProperty() {
        return tmiddle_name;
    }

    public void setTmiddle_name(String tmiddle_name) {
        this.tmiddle_name.set(tmiddle_name);
    }

    public String getTlast_name() {
        return tlast_name.get();
    }

    public SimpleStringProperty tlast_nameProperty() {
        return tlast_name;
    }

    public void setTlast_name(String tlast_name) {
        this.tlast_name.set(tlast_name);
    }


    public String gettName() {return tName.get();}

    public SimpleStringProperty tNameProperty() {return tName;}

    public void settName(String tName) {this.tName.set(tName);}

    public String gettAddress() {return tAddress.get();}

    public SimpleStringProperty tAddressProperty() {return tAddress;}

    public void settAddress(String tAddress) {this.tAddress.set(tAddress);}

    public int gettContact() {return tContact.get();}

    public SimpleIntegerProperty tContactProperty() {return tContact;}

    public void settContact(int tContact) {this.tContact.set(tContact);}

    public String gettEmail() {return tEmail.get();}

    public SimpleStringProperty tEmailProperty() {return tEmail;}

    public void settEmail(String tEmail) {this.tEmail.set(tEmail);}

    public int gettId() {return tId.get();}

    public SimpleIntegerProperty tIdProperty() {return tId;}

    public void settId(int tId) {this.tId.set(tId);}

    public int gettFId() {return tFId.get();}

    public SimpleIntegerProperty tFIdProperty() {return tFId;}

    public void settFId(int tFId) {this.tFId.set(tFId);}

    public int gettSId() {return tSId.get();}

    public SimpleIntegerProperty tSIdProperty() {return tSId;}

    public void settSId(int tSId) {this.tSId.set(tSId);}

    public Teacher(Integer id, String name, String address, Integer contact, String email, Integer faculty, Integer subject) {
        this.tAddress =new SimpleStringProperty(address);
        this.tName = new SimpleStringProperty(name);
        this.tContact = new SimpleIntegerProperty(contact);
        this.tId = new SimpleIntegerProperty(id);
        this.tEmail = new SimpleStringProperty(email);
        this.tFId = new SimpleIntegerProperty(faculty);
        this.tSId = new SimpleIntegerProperty(subject);

    }
    public Teacher(Integer id, String firstName, String middleName, String lastName, String address, Integer contact, String email, Integer faculty, Integer subject) {
        this.tfirst_name = new SimpleStringProperty(firstName);
        this.tmiddle_name = new SimpleStringProperty(middleName);
        this.tlast_name = new SimpleStringProperty(lastName);
        this.tAddress =new SimpleStringProperty(address);
        this.tContact = new SimpleIntegerProperty(contact);
        this.tId = new SimpleIntegerProperty(id);
        this.tEmail = new SimpleStringProperty(email);
        this.tFId = new SimpleIntegerProperty(faculty);
        this.tSId = new SimpleIntegerProperty(subject);
    }
}
