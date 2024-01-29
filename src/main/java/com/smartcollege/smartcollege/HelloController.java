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
import java.util.Objects;

public class HelloController {

    @FXML
    private Label alertLabel;


    @FXML
    private PasswordField PASSWORD;

    @FXML
    private TextField USERNAME;

    @FXML
    private Button loginbutton;

    @FXML
    private Label feedback;

    @FXML
    void onCloseAlert(ActionEvent event) {
        alertLabel.getParent().setVisible(false);
    }
    //
    /////////////////////////////////Settings TAB Menu //////////////////////////////////////////
    //
    @FXML
    private TabPane settingsTab;

    @FXML
    private Label databaseAlert;

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

    @FXML
    private Button databaseSubmitBtn;
    //database submission
    @FXML
    void onSubmitDB(ActionEvent event) {
        if(!dbpass.getText().isEmpty() && !dbuser.getText().isEmpty() && !database.getText().isEmpty() && !host.getText().isEmpty() && !port.getText().isEmpty() ){
            if(Database.postDatabaseDetails(host.getText(),database.getText(),dbuser.getText(),dbpass.getText(),port.getText())){
                databaseAlert.setText("Successfully saved database!");
                requestConnection();
            }else{
                databaseAlert.setText("Error occurred when saving file!");
            }
        }else{
            databaseAlert.setText("Fields cant be empty!");
        }
    }


    @FXML
    void onSettings(ActionEvent event) {
        studentsTab.setVisible(false);
        teachersTab.setVisible(false);
        homeTab.setVisible(false);
        batchTab.setVisible(false);

        //
        //TRUE
        settingsTab.setVisible(true);
        //check if there is database info stored in the config file.
        checkAndConnect();

    }
    ///////////////////////////////////// DATABASE///////////////////////////////////////
    private void checkAndConnect(){
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
                databaseSubmitBtn.setText("Update");

                databaseAlert.setText("Above information is being used for database connection!");
                requestConnection();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }else{
            databaseAlert.setText("Something went wrong!");
        }
    }
    private void requestConnection(){
        String checkConnection = Database.getConnection(host.getText(),database.getText(),dbuser.getText(),dbpass.getText(),port.getText());
        if(Objects.equals(checkConnection, "connected")){
            databaseAlert.setText("Connected to database.");
        }else{
            databaseAlert.setText(checkConnection);
        }
    }
    //
    ///////////////////////////////////////// Students TAB Menu///////////////////////////////////
    //
    @FXML
    private TabPane studentsTab;

    @FXML
    private Button studentsbtn;

    @FXML
    private TextField s_address;

    @FXML
    private TextField s_bid;

    @FXML
    private TextField s_contact;

    @FXML
    private TextField s_email;

    @FXML
    private TextField s_entrancescore;

    @FXML
    private TextField s_fid;

    @FXML
    private TextField s_firstname;

    @FXML
    private TextField s_lastname;

    @FXML
    private TextField s_middlename;

    @FXML
    private TextField s_pid;

    @FXML
    private TextField sid;
    @FXML
    void onStudents(ActionEvent event) {
        settingsTab.setVisible(false);
        teachersTab.setVisible(false);
        homeTab.setVisible(false);
        batchTab.setVisible(false);

        //
        //TRUE
        studentsTab.setVisible(true);

    }
    @FXML
    void onAddStudent(ActionEvent event) {
        //if department values are valid try and save it in the database
        if(!s_address.getText().isEmpty() && !s_pid.getText().isEmpty() && !s_bid.getText().isEmpty() && !sid.getText().isEmpty() && !s_contact.getText().isEmpty()
                && !s_email.getText().isEmpty() && !s_entrancescore.getText().isEmpty() && !s_fid.getText().isEmpty() && !s_firstname.getText().isEmpty() && !s_lastname.getText().isEmpty()
        ){
            String push = Database.addStudent(s_firstname.getText(),s_middlename.getText(),s_lastname.getText(),s_address.getText(),s_contact.getText()
                    ,s_email.getText(),s_entrancescore.getText(),s_fid.getText(),s_bid.getText(),s_pid.getText(),sid.getText());
            if(Objects.equals(push, "Success")){
                Alert.show(alertLabel,"Update Done!");
                dptName.setText("");
                dptId.setText("");
                dptHOD.setText("");
            }else{
                Alert.show(alertLabel,push);
            }
        }
    }
    //
    //////////////////////////////////Teachers TAB Menu//////////////////////////////////////////////
    //
    @FXML
    private TabPane teachersTab;

    @FXML
    void onTeachers(ActionEvent event) {
        settingsTab.setVisible(false);
        studentsTab.setVisible(false);
        homeTab.setVisible(false);
        batchTab.setVisible(false);

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



    //////=================== Home TAB =======================//
    @FXML
    private TabPane homeTab;

    @FXML
    void onHome(ActionEvent event) throws InterruptedException {
        teachersTab.setVisible(false);
        studentsTab.setVisible(false);
        settingsTab.setVisible(false);
        batchTab.setVisible(false);
        homeTab.setVisible(true);
    }
    @FXML
    private TextField dptHOD;

    @FXML
    private TextField dptId;

    @FXML
    private TextField dptName;

    @FXML
    void onAddDepartment(ActionEvent event) {
        //if department values are valid try and save it in the database
        if(!dptId.getText().isEmpty() && !dptName.getText().isEmpty()){
            String push = Database.addDepartment(dptId.getText(), dptName.getText(), dptHOD.getText());
            if(Objects.equals(push, "Success")){
                Alert.show(alertLabel,"Update Done!");
                dptName.setText("");
                dptId.setText("");
                dptHOD.setText("");
            }else{
                Alert.show(alertLabel,push);
            }
        }
    }
    @FXML
    private TextField fDid;

    @FXML
    private TextField fId;

    @FXML
    private TextField fName;
    @FXML
    void onAddFaculty(ActionEvent event) {
        //if Faculty values are valid try and save it in the database
        if(!fId.getText().isEmpty() && !fName.getText().isEmpty()){
            String push = Database.addFaculty(fId.getText(), fName.getText(), fDid.getText());
            if(Objects.equals(push, "Success")){
                Alert.show(alertLabel,"Update Done!");
                fDid.setText("");
                fId.setText("");
                fName.setText("");
            }else{
                Alert.show(alertLabel,push);
            }
        }
    }



    //////////////////////////////////// BATCH ///////////////////////////////////////
    @FXML
    private TabPane batchTab;
    @FXML
    private TextField bId;
    @FXML
    private TextField b_year;
    @FXML
    private TextField b_fId;
    @FXML
    private TextField b_semester;

    @FXML
    void onBatch(ActionEvent event) throws InterruptedException {
        teachersTab.setVisible(false);
        studentsTab.setVisible(false);
        settingsTab.setVisible(false);
        homeTab.setVisible(false);
        batchTab.setVisible(true);
    }

    @FXML
    void onAddBatch(ActionEvent event) {
        //if Faculty values are valid try and save it in the database
        if(!bId.getText().isEmpty() && !b_year.getText().isEmpty() && !b_fId.getText().isEmpty() && !b_semester.getText().isEmpty()){
            String push = Database.addBatch(bId.getText(), b_year.getText(), b_fId.getText(),b_semester.getText());
            if(Objects.equals(push, "Success")){
                Alert.show(alertLabel,"Update Done!");
                fDid.setText("");
                fId.setText("");
                fName.setText("");
            }else{
                Alert.show(alertLabel,push);
            }
        }
    }

}
