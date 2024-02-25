package com.smartcollege.smartcollege.EntityClass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Batch {
    private SimpleIntegerProperty bid;
    private SimpleIntegerProperty year;
    private SimpleIntegerProperty fid;
    private SimpleStringProperty semester;

    public int getBid() {
        return bid.get();
    }

    public SimpleIntegerProperty bidProperty() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid.set(bid);
    }

    public int getYear() {
        return year.get();
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
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

    public String getSemester() {
        return semester.get();
    }

    public SimpleStringProperty semesterProperty() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    public Batch(Integer bid, String semester, Integer fid,Integer year) {

        this.bid =new SimpleIntegerProperty(bid);
        this.fid =new SimpleIntegerProperty(fid);
        this.semester =new SimpleStringProperty(semester);
        this.year =new SimpleIntegerProperty(year);
    }
}
