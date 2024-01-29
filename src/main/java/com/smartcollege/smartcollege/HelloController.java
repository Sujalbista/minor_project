package com.smartcollege.smartcollege;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import com.smartcollege.smartcollege.HelloApplication;

public class HelloController {

    @FXML
    private PasswordField PASSWORD;

    @FXML
    private TextField USERNAME;

    @FXML
    private Button loginbutton;

    @FXML
    private Label feedback;

    @FXML
    private Button settingsbtn;

    @FXML
    private TabPane settingsTab;

    @FXML
    void onSettings(ActionEvent event) {
        settingsTab.setVisible(true);

    }

    @FXML
    void onLogin(ActionEvent event) throws InterruptedException {
        BackgroundFill greenFill = new BackgroundFill(Color.GREEN, null, null);
        Background green = new Background(greenFill);
        BackgroundFill redFIll = new BackgroundFill(Color.GREEN, null, null);
        Background red = new Background(redFIll);
        loginbutton.setVisible(false);
        Stage mainwindow =(Stage) loginbutton.getScene().getWindow();
        if(USERNAME.getText().equals("admin") && PASSWORD.getText().equals(("admin"))){
            feedback.setText("Success! Opening Dashboard...");
            feedback.setTextFill(Color.GREEN);
            HelloApplication.loggedin = true;
            com.smartcollege.smartcollege.Dashboard dashboard = new com.smartcollege.smartcollege.Dashboard(mainwindow);



        }else{
            feedback.setText("Failed! Try again!");
            feedback.setTextFill(Color.RED);
            USERNAME.setText("");
            PASSWORD.setText("");
            loginbutton.setVisible(true);
        }
    }

}
