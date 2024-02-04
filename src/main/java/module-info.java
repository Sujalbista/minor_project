module com.smartcollege.smartcollege {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires javafx.swing;
    requires json.simple;
    requires java.desktop;
    requires webcam.capture;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.mail;
    requires activation;
    requires jdk.httpserver;
    opens com.smartcollege.smartcollege to javafx.fxml;
    opens com.smartcollege.smartcollege.EntityClass to javafx.base;
    exports com.smartcollege.smartcollege;
}