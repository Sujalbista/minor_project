module com.smartcollege.smartcollege {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.smartcollege.smartcollege to javafx.fxml;
    exports com.smartcollege.smartcollege;
}