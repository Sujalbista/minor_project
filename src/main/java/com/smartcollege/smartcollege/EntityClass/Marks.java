package com.smartcollege.smartcollege.EntityClass;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Marks {
    private SimpleIntegerProperty mid;
    private SimpleStringProperty name;
    private SimpleStringProperty semester;
    private SimpleStringProperty terminal;
    private SimpleStringProperty subject;
    private SimpleFloatProperty marks;

    public Marks(int mid, String name,String semester, String terminal, String subject,Float marks) {
        this.mid = new SimpleIntegerProperty(mid);
        this.name = new SimpleStringProperty(name);
        this.semester = new SimpleStringProperty(semester);
        this.terminal = new SimpleStringProperty(terminal);
        this.subject = new SimpleStringProperty(subject);
        this.marks = new SimpleFloatProperty(marks);

    }

    public int getMid() {
        return mid.get();
    }

    public SimpleIntegerProperty midProperty() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid.set(mid);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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

    public String getTerminal() {
        return terminal.get();
    }

    public SimpleStringProperty terminalProperty() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal.set(terminal);
    }

    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public float getMarks() {
        return marks.get();
    }

    public SimpleFloatProperty marksProperty() {
        return marks;
    }

    public void setMarks(float marks) {
        this.marks.set(marks);
    }
}
