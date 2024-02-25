package com.smartcollege.smartcollege.EntityClass;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Subject {
    private SimpleIntegerProperty subId;
    private SimpleStringProperty Subject;
    private SimpleStringProperty semester;

    public int getSubId() {
        return subId.get();
    }

    public SimpleIntegerProperty subIdProperty() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId.set(subId);
    }

    public String getSubject() {
        return Subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return Subject;
    }

    public void setSubject(String subject) {
        this.Subject.set(subject);
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

    public Subject(Integer sid, String subject,String semester){
        this.subId = new SimpleIntegerProperty(sid);
        this.Subject = new SimpleStringProperty(subject);
        this.semester = new SimpleStringProperty(semester);
    }
}

