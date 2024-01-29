module com.smartcollege.smartcollege {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires json.simple;


    opens com.smartcollege.smartcollege to javafx.fxml;
    exports com.smartcollege.smartcollege;
}