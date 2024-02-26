package com.smartcollege.smartcollege.EntityClass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
public class Parent {

    private SimpleStringProperty pName;
    private SimpleStringProperty pAddress;
    private SimpleIntegerProperty pContact;
    private SimpleStringProperty pEmail;
    private SimpleIntegerProperty pId;
    private  SimpleStringProperty pfirst_name;
    private  SimpleStringProperty plast_name;
    private SimpleStringProperty pmiddle_name;

    public String getPfirst_name() {
        return pfirst_name.get();
    }

    public SimpleStringProperty pfirst_nameProperty() {
        return pfirst_name;
    }

    public void setPfirst_name(String pfirst_name) {
        this.pfirst_name.set(pfirst_name);
    }

    public String getPlast_name() {
        return plast_name.get();
    }

    public SimpleStringProperty plast_nameProperty() {
        return plast_name;
    }

    public void setPlast_name(String plast_name) {
        this.plast_name.set(plast_name);
    }

    public String getPmiddle_name() {
        return pmiddle_name.get();
    }

    public SimpleStringProperty pmiddle_nameProperty() {
        return pmiddle_name;
    }

    public void setPmiddle_name(String pmiddle_name) {
        this.pmiddle_name.set(pmiddle_name);
    }

    public String getpName() {return pName.get();}

    public SimpleStringProperty pNameProperty() {return pName;}

    public String getpAddress() {return pAddress.get();}

    public SimpleStringProperty pAddressProperty() {return pAddress;}

    public int getpContact() {return pContact.get();}

    public SimpleIntegerProperty pContactProperty() {return pContact;}

    public String getpEmail() {return pEmail.get();}

    public SimpleStringProperty pEmailProperty() {return pEmail;}

    public int getpId() {return pId.get();}

    public SimpleIntegerProperty pIdProperty() {return pId;}

    public void setpName(String pName) {this.pName.set(pName);}

    public void setpAddress(String pAddress) {this.pAddress.set(pAddress);}

    public void setpContact(int pContact) {this.pContact.set(pContact);}

    public void setpEmail(String pEmail) {this.pEmail.set(pEmail);}

    public void setpId(int pId) {this.pId.set(pId);}

    public Parent(Integer id, String name, String address, Integer contact, String email){
        this.pAddress =new SimpleStringProperty(address);
        this.pName = new SimpleStringProperty(name);
        this.pContact = new SimpleIntegerProperty(contact);
        this.pId = new SimpleIntegerProperty(id);
        this.pEmail = new SimpleStringProperty(email);
    }
    public Parent(int id, String firstName, String middleName, String lastName, String address, Integer contact, String email) {
        this.pfirst_name = new SimpleStringProperty(firstName);
        this.pmiddle_name = new SimpleStringProperty(middleName);
        this.plast_name = new SimpleStringProperty(lastName);
        this.pAddress =new SimpleStringProperty(address);
        this.pContact = new SimpleIntegerProperty(contact);
        this.pId = new SimpleIntegerProperty(id);
        this.pEmail = new SimpleStringProperty(email);
    }
}
