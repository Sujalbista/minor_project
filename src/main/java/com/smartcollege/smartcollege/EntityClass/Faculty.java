package com.smartcollege.smartcollege.EntityClass;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
public class Faculty {
    private SimpleIntegerProperty did;
    private SimpleStringProperty faculty;
    private SimpleIntegerProperty fid;

    public int getDid() {
        return did.get();
    }

    public SimpleIntegerProperty didProperty() {
        return did;
    }

    public void setDid(int did) {
        this.did.set(did);
    }

    public String getFaculty() {
        return faculty.get();
    }

    public SimpleStringProperty facultyProperty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty.set(faculty);
    }

    public int getFid() {
        return fid.get();
    }

    public SimpleIntegerProperty fidProperty() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid.set(fid);
    }

    public Faculty(Integer did, Integer fid, String faculty) {

        this.faculty =new SimpleStringProperty(faculty);
        this.did =new SimpleIntegerProperty(did);
        this.fid =new SimpleIntegerProperty(fid);
    }
}
