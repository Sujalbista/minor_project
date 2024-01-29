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
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class HelloController {

    @FXML
    private PasswordField PASSWORD;

    @FXML
    private TextField USERNAME;

    @FXML
    private Button loginbutton;

    @FXML
    private Label feedback;

    //
    //Settings TAB Menu
    //
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


    @FXML
    void onSettings(ActionEvent event) {
        studentsTab.setVisible(false);
        teachersTab.setVisible(false);
        //
        //TRUE
        settingsTab.setVisible(true);
        //show settings
        settingsTab.setVisible(true);
        //check if there is database info stored in the config file.
        if(Database.checkDatabaseDetails()){
            JSONParser jsonParser = new JSONParser();
            try(FileReader reader = new FileReader("./src/main/resources/config/config.json")){
                Object obj = jsonParser.parse(reader);
                JSONObject json = (JSONObject) obj;
                host.setText((String) json.get("host"));
                port.setText((String) json.get("port").toString());
                database.setText((String) json.get("database"));
                dbuser.setText((String) json.get("username"));
                dbpass.setText((String) json.get("password"));

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }else{

        }

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
        if(USERNAME.getText().equals("subu") && PASSWORD.getText().equals(("1234"))){
            feedback.setText("Success! Opening Dashboard...");
            feedback.setTextFill(Color.GREEN);
            HelloApplication.loggedin = true;
            Dashboard dashboard = new Dashboard(mainwindow);



        }else{
            feedback.setText("Failed! Try again!");
            feedback.setTextFill(Color.RED);
            USERNAME.setText("");
            PASSWORD.setText("");
            loginbutton.setVisible(true);
        }
    }

}
