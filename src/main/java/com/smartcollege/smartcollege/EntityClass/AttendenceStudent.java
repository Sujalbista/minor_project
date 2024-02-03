package com.smartcollege.smartcollege.EntityClass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AttendenceStudent {
    private SimpleIntegerProperty stdID;
    private SimpleStringProperty stdName;
    private SimpleStringProperty stdSemester;
    private SimpleStringProperty date;
    private SimpleStringProperty time;
    private SimpleStringProperty stdFaculty;

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

    public String getStdSemester() {
        return stdSemester.get();
    }

    public SimpleStringProperty stdSemesterProperty() {
        return stdSemester;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public String getStdFaculty() {
        return stdFaculty.get();
    }

    public SimpleStringProperty stdFacultyProperty() {
        return stdFaculty;
    }

    public void setStdID(int stdID) {
        this.stdID.set(stdID);
    }

    public void setStdName(String stdName) {
        this.stdName.set(stdName);
    }

    public void setStdSemester(String stdSemester) {
        this.stdSemester.set(stdSemester);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public void setStdFaculty(String stdFaculty) {
        this.stdFaculty.set(stdFaculty);
    }

    public AttendenceStudent(Integer id, String name, String semester, String date, String faculty, String time){
        this.date = new SimpleStringProperty(date);
        this.stdID = new SimpleIntegerProperty(id);
        this.stdSemester = new SimpleStringProperty(semester);
        this.stdFaculty = new SimpleStringProperty(faculty);
        this.stdName = new SimpleStringProperty(name);
        this.time = new SimpleStringProperty(time);
    }
}
