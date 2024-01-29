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
import com.smartcollege.smartcollege.database.Database;

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
    private TabPane settingsTab;

    @FXML
    private Button settingsbtn;

    //database variables
    @FXML
    private TextField database;

    @FXML
    private TextField dbpass;

    @FXML
    private TextField dbuser;

    @FXML
    private TextField host;

    @FXML
    private TextField port;

    //database submission
    @FXML
    void onSubmitDB(ActionEvent event) {
        if(!dbpass.getText().isEmpty() && !dbuser.getText().isEmpty() && !database.getText().isEmpty() && !host.getText().isEmpty() && !port.getText().isEmpty() ){
            if(Database.postDatabaseDetails(host.getText(),database.getText(),dbuser.getText(),dbpass.getText(),port.getText())){
                System.out.println("Saved!");
            }else{
                System.out.println(("Couldnt save!"));
            }
        }else{
            System.out.println("Empty fields");
        }
    }

    //
    //Settings TAB Menu
    //



    @FXML
    void onSettings(ActionEvent event) {
        studentsTab.setVisible(false);
        teachersTab.setVisible(false);
        //
        //TRUE
        settingsTab.setVisible(true);

    }

    //
    // Students TAB Menu
    //
    @FXML
    private TabPane studentsTab;

    @FXML
    private Button studentsbtn;

    @FXML
    void onStudents(ActionEvent event) {
        settingsTab.setVisible(false);
        teachersTab.setVisible(false);
        //
        //TRUE
        studentsTab.setVisible(true);

    }

    //
    //Teachers TAB Menu
    //
    @FXML
    private TabPane teachersTab;

    @FXML
    void onTeachers(ActionEvent event) {
        settingsTab.setVisible(false);
        studentsTab.setVisible(false);
        //
        // TRUE
        teachersTab.setVisible(true);

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
