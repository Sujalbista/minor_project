package com.smartcollege.smartcollege.EntityClass;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Department {
    private SimpleIntegerProperty hod;
    private SimpleIntegerProperty did;
    private SimpleStringProperty department;

    public int getDid() {
        return did.get();
    }

    public SimpleIntegerProperty didProperty() {
        return did;
    }

    public void setDid(int did) {
        this.did.set(did);
    }

    public int getHod() {
        return hod.get();
    }

    public SimpleIntegerProperty hodProperty() {
        return hod;
    }

    public void setHod(int hod) {
        this.hod.set(hod);
    }

    public String getDepartment() {
        return department.get();
    }

    public SimpleStringProperty departmentProperty() {
        return department;
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }
    public Department(Integer did,Integer hod, String department) {

        this.department =new SimpleStringProperty(department);
        this.hod =new SimpleIntegerProperty(hod);
        this.did =new SimpleIntegerProperty(did);
    }
}
