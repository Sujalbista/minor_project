package com.smartcollege.smartcollege;

import Encryption.Encryption;
import com.smartcollege.smartcollege.EntityClass.*;
import com.smartcollege.smartcollege.database.Database;
import com.smartcollege.smartcollege.database.Notice;
import com.smartcollege.smartcollege.database.User;
import com.smartcollege.smartcollege.tableview.*;
import com.google.zxing.WriterException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class HelloController {
    private String[] semesters = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII"};
    private String[] terminals = {"1st Term", "2ndTerm", "3rd Term", "Pre-board"};
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
    private ListView<String> searchResultList;

    @FXML
    private Pane searchResults;

    @FXML
    void onCloseAlert(ActionEvent event) {
        alertLabel.getParent().setVisible(false);
    }

    @FXML
    void onCloseSearch(ActionEvent event) {
        searchResults.setVisible(false);
    }

    @FXML
    private ProgressBar progressBar;

    void showProgress(ActionEvent event) {
        Button button = (Button) event.getSource();
        progressBar.setVisible(true);
        button.setDisable(true);
    }

    void hideProgress(ActionEvent event) {
        Button button = (Button) event.getSource();
        progressBar.setVisible(false);
        button.setDisable(false);
    }

    @FXML
    void onLogin(ActionEvent event) throws InterruptedException {
        BackgroundFill greenFill = new BackgroundFill(Color.GREEN, null, null);
        Background green = new Background(greenFill);
        BackgroundFill redFIll = new BackgroundFill(Color.GREEN, null, null);
        Background red = new Background(redFIll);
        loginbutton.setVisible(false);
        Stage mainwindow = (Stage) loginbutton.getScene().getWindow();
        if (User.checkUserExistance()) {
            if (User.checkLogin(USERNAME.getText(), PASSWORD.getText())) {
                feedback.setText("Success! Opening Dashboard...");
                feedback.setTextFill(Color.GREEN);
                HelloApplication.loggedin = true;
                Dashboard dashboard = new Dashboard(mainwindow);
            } else {
                feedback.setText("Failed! Try again!");
                feedback.setTextFill(Color.RED);
                USERNAME.setText("");
                PASSWORD.setText("");
                loginbutton.setVisible(true);
            }
        } else {
            if (USERNAME.getText().equals("admin") && PASSWORD.getText().equals(("admin"))) {
                feedback.setText("Success! Opening Dashboard...");
                feedback.setTextFill(Color.GREEN);
                HelloApplication.loggedin = true;
                Dashboard dashboard = new Dashboard(mainwindow);

            } else {
                feedback.setText("Failed! Try again!");
                feedback.setTextFill(Color.RED);
                USERNAME.setText("");
                PASSWORD.setText("");
                loginbutton.setVisible(true);
            }
        }

    }

    @FXML
    void onForgotPassword(MouseEvent event) {
        Label label = (Label) event.getSource();
        label.setVisible(false);
        feedback.setText("Please wait..Sending Email");
        feedback.setTextFill(Color.WHITE);
        Task<String> forgotpasswordTask = new Task<String>() {
            @Override
            protected String call() throws Exception {

                return User.onForgetPassword();
            }
        };
        forgotpasswordTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            feedback.setText(newValue);
        });
        Thread fpthread = new Thread(forgotpasswordTask);
        fpthread.setDaemon(true);
        fpthread.start();

//        String callback = User.onForgetPassword();

    }

    ///////////////////////////////// SettingsTABMenu//////////////////////////////////
    @FXML
    private TabPane settingsTab;

    @FXML
    private Label databaseAlert;

    @FXML
    private Button settingsbtn;

    // database variables
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
    @FXML
    private TextField userEmail;
    @FXML
    private TextField userNewPass;

    @FXML
    private TextField userNewPassConfirm;

    @FXML
    private TextField userOldPass;

    @FXML
    private TextField userUsername;

    @FXML
    void onUpdateEmail(ActionEvent event) {
        if (!userEmail.getText().isEmpty()) {
            String feedback = User.updateEmail(userEmail.getText());
            Alert.show(alertLabel, feedback);
        } else {
            Alert.show(alertLabel, "Email is empty!");
        }

    }

    @FXML
    void onUpdateUsername(ActionEvent event) {
        if (!userUsername.getText().isEmpty()) {
            String feedback = User.updateUsername(userUsername.getText());
            Alert.show(alertLabel, feedback);
        } else {
            Alert.show(alertLabel, "Tero bau le nam rakhena tero?");
        }

    }

    @FXML
    void onUpdatePassword(ActionEvent event) {
        if (!userOldPass.getText().isEmpty() && !userNewPass.getText().isEmpty()) {
            String feedback = User.updatePassword(userOldPass.getText(), userNewPass.getText());
            Alert.show(alertLabel, feedback);
        } else {
            Alert.show(alertLabel, "Password fields are empty!");
        }
    }

    // database submission
    @FXML
    void onSubmitDB(ActionEvent event) {
        if (!dbpass.getText().isEmpty() && !dbuser.getText().isEmpty() && !database.getText().isEmpty()
                && !host.getText().isEmpty() && !port.getText().isEmpty()) {
            if (Database.postDatabaseDetails(host.getText(), database.getText(), dbuser.getText(), dbpass.getText(),
                    port.getText())) {
                databaseAlert.setText("Successfully saved database!");
                requestConnection();
            } else {
                databaseAlert.setText("Error occurred when saving file!");
            }
        } else {
            databaseAlert.setText("Fields cant be empty!");
        }
    }


    @FXML
    void onSettings(ActionEvent event) {
        onMainButton(event);
        settingsTab.setVisible(true);

        //
        // TRUE
        // check if there is database info stored in the config file.
        checkAndConnect();

    }

    ///////////////////////////////////// DATABASE///////////////////////////////////////
    private void checkAndConnect() {
        if (Database.checkDatabaseDetails()) {
            JSONParser jsonParser = new JSONParser();
            try (FileReader reader = new FileReader("./src/main/resources/config/config.json")) {
                Object obj = jsonParser.parse(reader);
                JSONObject json = (JSONObject) obj;

                host.setText((String) json.get("host"));
                port.setText((String) json.get("port").toString());
                database.setText((String) json.get("database"));
                dbuser.setText((String) json.get("username"));
                dbpass.setText((String) Encryption.decrypt((String) json.get("password")));
                databaseSubmitBtn.setText("Update");

                databaseAlert.setText("Above information is being used for database connection!");
                requestConnection();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            databaseAlert.setText("Database info not found!");
        }
    }

    private void requestConnection() {
        String checkConnection = Database.getConnection(host.getText(), database.getText(), dbuser.getText(),
                dbpass.getText(), port.getText());
        if (Objects.equals(checkConnection, "connected")) {
            databaseAlert.setText("Connected to database.");
        } else {
            databaseAlert.setText(checkConnection);
        }
    }

    ///////////////////////////////////////// StudentsTABMenu///////////////////////////////////
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
    private TableColumn<Student, String> stdAddress;

    @FXML
    private TableColumn<Student, String> stdBatch;

    @FXML
    private TableColumn<Student, Integer> stdContact;

    @FXML
    private TableColumn<Student, String> stdEmail;

    @FXML
    private TableColumn<Student, String> stdFaculty;

    @FXML
    private TableColumn<Student, Integer> stdID;

    @FXML
    private TableColumn<Student, String> stdName;

    @FXML
    private TableColumn<Student, Integer> stdParentId;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> stdAddress1;

    @FXML
    private TableColumn<Student, String> stdBatch1;

    @FXML
    private TableColumn<Student, Integer> stdContact1;

    @FXML
    private TableColumn<Student, String> stdEmail1;

    @FXML
    private TableColumn<Student, String> stdFaculty1;

    @FXML
    private TableColumn<Student, Integer> stdID1;

    @FXML
    private TableColumn<Student, String> stdName1;

    @FXML
    private TableColumn<Student, Integer> stdParentId1;

    @FXML
    private TableView<Student> studentTable2;

    /////////////Student Search ////////////////////////
    @FXML
    private TableColumn<Student, String> stdAddress11;

    @FXML
    private TableColumn<Student, String> stdBatch11;

    @FXML
    private TableColumn<Student, Integer> stdContact11;

    @FXML
    private TableColumn<Student, String> stdEmail11;

    @FXML
    private TableColumn<Student, String> stdFaculty11;

    @FXML
    private TableColumn<Student, Integer> stdID11;

    @FXML
    private TableColumn<Student, String> stdName11;

    @FXML
    private TableColumn<Student, Integer> stdParentId11;

    @FXML
    private TableView<Student> studentTable3;

    @FXML
    private ProgressIndicator Loading;


    //////////////// update///////////////
    @FXML
    private Button upddateStudentbtn;

    @FXML
    private TextField us_address;

    @FXML
    private TextField us_bid;

    @FXML
    private TextField us_contact;

    @FXML
    private TextField us_email;

    @FXML
    private TextField us_entrancescore;

    @FXML
    private TextField us_fid;

    @FXML
    private TextField us_firstname;

    @FXML
    private TextField us_lastname;

    @FXML
    private TextField us_middlename;

    @FXML
    private TextField us_pid;

    @FXML
    private TextField studentUpdateField;

    @FXML
    void onUpdateStudent(ActionEvent event) throws AddressException, SQLException {
        String push = null;
        if (!studentUpdateField.getText().isEmpty()) {
            if (!us_address.getText().isEmpty() && !us_pid.getText().isEmpty() && !us_bid.getText().isEmpty()
                    && !us_contact.getText().isEmpty() && !us_email.getText().isEmpty()
                    && !us_entrancescore.getText().isEmpty() && !us_fid.getText().isEmpty()
                    && !us_firstname.getText().isEmpty() && !us_lastname.getText().isEmpty()) {

                String firstName = us_firstname.getText();
                String middleName = us_middlename.getText();
                String lastName = us_lastname.getText();
                String address = us_address.getText();
                String contact = us_contact.getText();
                String email = us_email.getText();
                String entranceScore = us_entrancescore.getText();
                String faculty = us_fid.getText();
                String batch = us_bid.getText();
                String parentId = us_pid.getText();

                push = Database.updateStudent(studentUpdateField.getText(), firstName, middleName, lastName, address, contact, email,
                        entranceScore, faculty, batch, parentId);
                if (Objects.equals(push, "Success")) {
                    Alert.show(alertLabel, "Update Done!");
                } else {
                    Alert.show(alertLabel, push);
                }

            }
        }
        else{
            Alert.show(alertLabel,"Empty ID");
        }
}

    @FXML
    void onUpdateSearch(ActionEvent event){
        if (!studentUpdateField.getText().isEmpty()) {
            String search = studentUpdateField.getText();
            StudentView studentView = new StudentView(search);
            Student student = studentView.getStudent();
            us_firstname.setText(student.getStdFirstname());
            us_middlename.setText(student.getStdMiddlemnane());
            us_lastname.setText(student.getStdLastname());
            us_address.setText(student.getStdAddress());
            us_contact.setText(String.valueOf(student.getStdContact()));
            us_email.setText(student.getStdEmail());
            us_entrancescore.setText(String.valueOf(student.getStdEntranceScore()));
            us_fid.setText(student.getStdFaculty());
            us_bid.setText(student.getStdBatch());
            us_pid.setText(String.valueOf(student.getStdParentId()));
        }
    }

    @FXML
    void onStudents(ActionEvent event) throws SQLException {
        onMainButton(event);
        studentsTab.setVisible(true);

        StudentView studentView = new StudentView(studentTable, stdID, stdName, stdAddress, stdContact, stdEmail,
                stdFaculty, stdBatch, stdParentId);

    }
    @FXML
    void onStudentSearchFaculty(KeyEvent event ){
        searchResultList.getItems().clear();

        ArrayList<String> result = Database.searchFaculty(s_fid.getText().toString());
        searchResultList.getItems().addAll(result);
        searchResults.setVisible(true);
    }
    @FXML
    void onStudentSearchBatch(KeyEvent event ){
        searchResultList.getItems().clear();

        ArrayList<String> result = Database.searchBatch(s_bid.getText().toString());
        searchResultList.getItems().addAll(result);
        searchResults.setVisible(true);
    }
    @FXML
    void onStudentSearchParent(KeyEvent event ){
        searchResultList.getItems().clear();

        ArrayList<String> result = Database.searchParent(s_pid.getText().toString());
        searchResultList.getItems().addAll(result);
        searchResults.setVisible(true);
    }
    @FXML
    void onAddStudent(ActionEvent event) throws IOException, WriterException, AddressException {

        // if department values are valid try and save it in the database
        if (!s_address.getText().isEmpty() && !s_pid.getText().isEmpty() && !s_bid.getText().isEmpty()
                &&  !s_contact.getText().isEmpty()
                && !s_email.getText().isEmpty() && !s_entrancescore.getText().isEmpty() && !s_fid.getText().isEmpty()
                && !s_firstname.getText().isEmpty() && !s_lastname.getText().isEmpty()) {
            showProgress(event);
            Task<String> addStudentTask = new Task<String>() {
                @Override
                protected String call() throws Exception {

                    return Database.addStudent(s_firstname.getText(), s_middlename.getText(), s_lastname.getText(),
                            s_address.getText(), s_contact.getText(), s_email.getText(), s_entrancescore.getText(),
                            s_fid.getText(), s_bid.getText(), s_pid.getText());
                }
            };
            addStudentTask.valueProperty().addListener((observable, oldValue, newValue)->{
                if (Objects.equals(newValue, "Success")) {
                    Alert.show(alertLabel, "Update Done!");
                    s_firstname.setText("");
                    s_middlename.setText("");
                    s_lastname.setText("");
                    s_address.setText("");
                    s_contact.setText("");
                    s_email.setText("");
                    s_entrancescore.setText("");
                    s_fid.setText("");
                    s_bid.setText("");
                    s_pid.setText("");
                    searchResults.setVisible(false);
                    hideProgress(event);
                } else {
                    Alert.show(alertLabel, newValue);
                    hideProgress(event);
                }
            });

            Thread addStdThread = new Thread(addStudentTask);
            addStdThread.setDaemon(true);
            addStdThread.start();
        }
    }
    @FXML
    private TextField stdnfield;

    @FXML
    private TextField stdnfield1;

    @FXML
    void onSearchStudent(ActionEvent event) throws SQLException {
        if (!stdnfield.getText().isEmpty()) {
            System.out.println(stdnfield.getText());
            StudentView studentView = new StudentView(stdnfield.getText(),studentTable2, stdID1, stdName1, stdAddress1, stdContact1, stdEmail1,
                    stdFaculty1, stdBatch1, stdParentId1);
        }
    }

    @FXML
    void onSearchDeleteStudent(ActionEvent event) throws SQLException {
        if (!stdnfield1.getText().isEmpty()) {
            System.out.println(stdnfield.getText());
            StudentView studentView = new StudentView(stdnfield1.getText(),studentTable3, stdID11, stdName11, stdAddress11, stdContact11, stdEmail11,
                    stdFaculty11, stdBatch11, stdParentId11);
        }
    }

    @FXML
    void onDeleteStudent(ActionEvent event) throws SQLException {
        String push = null;
        if (!stdnfield1.getText().isEmpty()) {

            System.out.println(stdnfield1.getText());
            push = Database.deleteStudent(stdnfield1.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                studentTable3.getItems().clear();
            } else {
                Alert.show(alertLabel, push);
            }

        }else{
            Alert.show(alertLabel,"Empty ID");
        }


    }





    ///////////////////////////////// SUBJECTS////////////////////////////////////////////////////////
    @FXML
    private TextField subjectName;

    @FXML
    private TextField subSemester;
    @FXML
    private TextField s_Id;
    @FXML
    private TextField subjectName1;

    @FXML
    private TextField subSemester1;
    @FXML
    private TabPane subjectTab;

    @FXML
    private TableColumn<Subject, String> semester;
    @FXML
    private TableColumn<Subject, String> subject;
    @FXML
    private TableColumn<Subject, Integer> sid;
    @FXML
    private TableView<Subject> subjectTable;




    @FXML
    void onAddSubject(ActionEvent event) {
        if (!subjectName.getText().isEmpty() && !subSemester.getText().isEmpty()) {
            String push = Database.addSubject(subjectName.getText(), subSemester.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                dptName.setText("");
                dptHOD.setText("");
            } else {
                Alert.show(alertLabel, push);
            }
        } else {
            Alert.show(alertLabel, "Fields are empty!");
        }
    }

    @FXML
    void onSubjects(ActionEvent event) throws SQLException {
        onMainButton(event);
        subjectTab.setVisible(true);
        subjectName.setText("");
        subSemester.setText("");
        s_Id.setText("");
        subjectName1.setText("");
        subSemester1.setText("");
        SubjectView subjectView= new SubjectView(s_Id,subjectName1,subSemester1,subjectTable,sid,subject,semester);
    }

    @FXML
    void onEditSubject(ActionEvent event){

        if (!subjectName1.getText().isEmpty() && !subSemester1.getText().isEmpty()) {
            String push = Database.editSubject(s_Id.getText(),subjectName1.getText(), subSemester1.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                dptName.setText("");
                dptHOD.setText("");
            } else {
                Alert.show(alertLabel, push);
            }
        } else {
            Alert.show(alertLabel, "Fields are empty!");
        }
    }

    @FXML
    void onDeleteSubject(ActionEvent event) throws SQLException {
        String push = null;
        if (!s_Id.getText().isEmpty()) {
            push = Database.deleteSubject(s_Id.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
            } else {
                Alert.show(alertLabel, push);
            }
        }else{
            Alert.show(alertLabel,"Empty is id!");
        }
    }
        ////////////////////////////////// TeachersTAB//////////////////////////////////

    @FXML
    private TabPane teachersTab;
    @FXML
    private TextField t_Address;

    @FXML
    private TextField t_Contact;

    @FXML
    private TextField t_Email;

    @FXML
    private TextField t_Faculty;

    @FXML
    private TextField t_FirstName;

    @FXML
    private TextField t_LastName;

    @FXML
    private TextField t_MiddleName;

    @FXML
    private TextField t_Subject;

    ///////////////teacher View//////////
    @FXML
    private TableView<Teacher> TeacherTable;

    @FXML
    private TableColumn<Teacher, Integer> tId;

    @FXML
    private TableColumn<Teacher, String> tAddress;

    @FXML
    private TableColumn<Teacher, Long> tContact;

    @FXML
    private TableColumn<Teacher, String> tEmail;

    @FXML
    private TableColumn<Teacher, String> tName;

    @FXML
    private TableColumn<Teacher, Integer> tFId;

    @FXML
    private TableColumn<Teacher, Integer> tSId;

    @FXML
    private TableView<Teacher> TeacherTable1;
    @FXML
    private TableColumn<Teacher, Integer> tId1;

    @FXML
    private TableColumn<Teacher, String> tAddress1;

    @FXML
    private TableColumn<Teacher, Long> tContact1;

    @FXML
    private TableColumn<Teacher, String> tEmail1;

    @FXML
    private TableColumn<Teacher, String> tName1;

    @FXML
    private TableColumn<Teacher, Integer> tFId1;

    @FXML
    private TableColumn<Teacher, Integer> tSId1;

    @FXML
    private TableView<Teacher> TeacherTable2;
    @FXML
    private TableColumn<Teacher, Integer> tId2;

    @FXML
    private TableColumn<Teacher, String> tAddress2;

    @FXML
    private TableColumn<Teacher, Long> tContact2;

    @FXML
    private TableColumn<Teacher, String> tEmail2;

    @FXML
    private TableColumn<Teacher, String> tName2;

    @FXML
    private TableColumn<Teacher, Integer> tFId2;

    @FXML
    private TableColumn<Teacher, Integer> tSId2;

    @FXML
    private TextField teacherField1;

    @FXML
    private TextField teacherField2;

    ///////update
    @FXML
    private TextField ut_address;

    @FXML
    private TextField ut_contact;

    @FXML
    private TextField ut_email;

    @FXML
    private TextField ut_firstname;

    @FXML
    private TextField ut_lastname;

    @FXML
    private TextField ut_middlename;
    @FXML
    private TextField ut_faculty;
    @FXML
    private TextField ut_subject;

    @FXML
    private TextField teacherUpdateField;

    @FXML
    void onTeacherUs(ActionEvent event) throws SQLException {
        if (!teacherUpdateField.getText().isEmpty()) {
            String search = teacherUpdateField.getText();
            TeacherView teacherView = new TeacherView(search);
            Teacher teacher = teacherView.getTeacher();
            ut_firstname.setText(teacher.getTfirst_name());
            ut_middlename.setText(teacher.getTmiddle_name());
            ut_lastname.setText(teacher.getTlast_name());
            ut_address.setText(teacher.gettAddress());
            ut_contact.setText(String.valueOf(teacher.gettContact()));
            ut_email.setText(teacher.gettEmail());
            ut_faculty.setText(String.valueOf(teacher.gettFId()));
            ut_subject.setText(String.valueOf(teacher.gettSId()));
        }
    }
    @FXML
    void onUpdateTeacher(ActionEvent event) throws AddressException, SQLException {
        String push = null;
        System.out.println("hi");
        if (!teacherUpdateField.getText().isEmpty()) {
            System.out.println("lamo");
            if (!ut_address.getText().isEmpty() && !ut_contact.getText().isEmpty() && !ut_email.getText().isEmpty()
                    && !ut_firstname.getText().isEmpty() && !ut_lastname.getText().isEmpty()
                    && !ut_faculty.getText().isEmpty()&& !ut_subject.getText().isEmpty()) {

                String firstName = ut_firstname.getText();
                String middleName = ut_middlename.getText();
                String lastName = ut_lastname.getText();
                String address = ut_address.getText();
                String contact = ut_contact.getText();
                String email = ut_email.getText();
                String fid = ut_faculty.getText();
                String sid = ut_subject.getText();

                System.out.println("k xa");
                push = Database.updateTeacher(teacherUpdateField.getText(), firstName, middleName, lastName, address, contact, email,fid,sid);
                if (Objects.equals(push, "Success")) {
                    Alert.show(alertLabel, "Update Done!");
                } else {
                    Alert.show(alertLabel, push);
                }
            }
        }
        else{
            Alert.show(alertLabel,"Empty ID");
        }
    }



    @FXML
    void onAddTeacher(ActionEvent event) {
        if (!t_FirstName.getText().isEmpty() && !t_LastName.getText().isEmpty()
                && !t_Address.getText().isEmpty() && !t_Contact.getText().isEmpty()
                && !t_Email.getText().isEmpty() && !t_Faculty.getText().isEmpty() && !t_Subject.getText().isEmpty()) {
            String push = Database.addTeacher(t_FirstName.getText(), t_MiddleName.getText(), t_LastName.getText(),
                    t_Address.getText(), t_Contact.getText(), t_Email.getText(), t_Faculty.getText(), t_Subject.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                dptName.setText("");
                dptHOD.setText("");
            } else {
                Alert.show(alertLabel, push);
            }
        }
    }

    @FXML
    void onTeachers(ActionEvent event) {
        onMainButton(event);
        teachersTab.setVisible(true);
        TeacherView newTeacherView = new TeacherView(TeacherTable,tId,tName,tAddress,tContact,tEmail,tFId,tSId);

    }
    @FXML
    void onTeacherSearch1(ActionEvent event) throws SQLException {
        handleTSearch(teacherField1,TeacherTable1,tId1,tName1,tAddress1,tContact1,tEmail1,tFId1,tSId1);
    }

    @FXML
    void onTeacherSearch2(ActionEvent event) throws SQLException {
        handleTSearch(teacherField2,TeacherTable2,tId2,tName2,tAddress2,tContact2,tEmail2,tFId2,tSId2);
    }

    private void handleTSearch(TextField textField, TableView tableView,
                              TableColumn idColumn, TableColumn nameColumn, TableColumn addressColumn,
                              TableColumn contactColumn, TableColumn emailColumn,TableColumn facultyColumn, TableColumn subjectColumn) throws SQLException {
        if (!textField.getText().isEmpty()) {
            System.out.println(textField.getText());
            TeacherView teacherView = new TeacherView(textField.getText(), tableView, idColumn, nameColumn, addressColumn,
                    contactColumn, emailColumn,facultyColumn,subjectColumn);
        }
    }

    @FXML
    void onDeleteTeacher(ActionEvent event) throws SQLException {
        String push = null;
        if (!teacherField2.getText().isEmpty()) {
            System.out.println(teacherField2.getText());
            push = Database.deleteTeacher(teacherField2.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                TeacherTable2.getItems().clear();
            } else {
                Alert.show(alertLabel, push);
            }
        }else{
            Alert.show(alertLabel,"Empty is id!");
        }
    }




    ////// =================== Home TAB =======================//
    @FXML
    private TabPane homeTab;

    @FXML
    private Label studentCount;
    @FXML
    private Label teacherCount;

    @FXML
    private Label departmentCount;
    @FXML
    private Label facultyCount;

    @FXML
    private BarChart<String, Integer> testGraph;
    @FXML
    private AreaChart<String, Integer> attendenceGraph;
    @FXML
    private ChoiceBox<String> resultTerminals;
    @FXML
    private ChoiceBox<String> resultSemester;
    @FXML
    private TextField aGraphBatchId;
    void showResultCompare(){
//        XYChart.Series<String,Integer> series1 = new XYChart.Series();
//        series1.setName("BIT");
//        series1.getData().add(new XYChart.Data<>("2020",20));
//        series1.getData().add(new XYChart.Data<>("2021",90));
//        series1.getData().add(new XYChart.Data<>("2022",90));
//        series1.getData().add(new XYChart.Data<>("2023",90));
//        XYChart.Series<String,Integer> series2 = new XYChart.Series();
//        series2.setName("BCA");
//        series2.getData().add(new XYChart.Data<>("2020",40));
//        series2.getData().add(new XYChart.Data<>("2021",70));
//        series2.getData().add(new XYChart.Data<>("2022",70));
//        series2.getData().add(new XYChart.Data<>("2023",70));
//        XYChart.Series<String,Integer> series3 = new XYChart.Series();
//        series3.setName("BE");
//        series3.getData().add(new XYChart.Data<>("2020",40));
//        series3.getData().add(new XYChart.Data<>("2021",70));
//        series3.getData().add(new XYChart.Data<>("2022",70));
//        series3.getData().add(new XYChart.Data<>("2023",70));
//        testGraph.getData().addAll(series1,series2,series3);

    }

    @FXML
    void onHome(ActionEvent event) throws InterruptedException, SQLException {
        onMainButton(event);
        homeTab.setVisible(true);
        initDashboard();
//        showResultCompare();
        resultTerminals.getItems().clear();
        resultTerminals.getItems().addAll(terminals);
        resultSemester.getItems().clear();
        resultSemester.getItems().addAll(semesters);
        deptHOD.setText("");
        deptName.setText("");
        deptId.setText("");
        dptHOD.setText("");
        dptName.setText("");
        fid.setText("");
        fDid.setText("");
        fName.setText("");
        deptId.setText("");
        DepartmentView departmentView= new DepartmentView(deptId,dptHOD,dptName,departmentTable,d_Id,d_name,d_hod);
        FacultyView facultyView = new FacultyView(fid,fName,fDid,facultyTable,f_did,f_Id,f_name);
    }
    @FXML
    void onUpdateAttendenceraph(ActionEvent event) throws InterruptedException, SQLException {
        attendenceGraph.getData().clear();
        String batch = aGraphBatchId.getText().toString();
        if(!batch.isEmpty()){

            XYChart.Series<String,Integer> series = new XYChart.Series();
            String getBatch = "SELECT semester from batch where bid="+batch+";";
            PreparedStatement getBatchStatement = Database.con.prepareStatement(getBatch);
            ResultSet semester = getBatchStatement.executeQuery();
            semester.next();
            String sem = semester.getString("semester");

            for (int i = 5; i >=0 ; i--) {
                String sql = "SELECT COUNT(*) as present FROM `"+batch+"attendence` WHERE date = '"+LocalDate.now().minus(i,ChronoUnit.DAYS)+"' and ofSemester='"+sem+"'";
                System.out.println(sql);
                PreparedStatement statement = Database.con.prepareStatement(sql);
                ResultSet result = statement.executeQuery();
                result.next();
                int count = result.getInt("present");
                series.getData().add(new XYChart.Data<>(String.valueOf(LocalDate.now().minus(i, ChronoUnit.DAYS)),count));
            }
            attendenceGraph.getData().add(series);
        }else{
            Alert.show(alertLabel,"Empty ID");
        }
    }
    @FXML
    void onUpdateResultGraph(ActionEvent event) throws InterruptedException, SQLException{
        String term = resultTerminals.getValue();
        String semester = resultSemester.getValue();
        Vector<XYChart.Series<String,Integer>> seriesCollection = new Vector<>();
        testGraph.getData().clear();
        if(!term.isEmpty() && !semester.isEmpty()){
            Integer date = LocalDate.now().getYear();
            for (int i = date-1; i <= date; i++) {
                String getBatches = "SELECT bid,year,semester,faculty_name from batch Inner join faculty on batch.fid=faculty.fid where year = '"+i+"';";
                PreparedStatement statement = Database.con.prepareStatement(getBatches);
                ResultSet batches = statement.executeQuery();

                XYChart.Series<String,Integer> series = new XYChart.Series();
                series.setName(String.valueOf(i));

                while (batches.next()){

                    String getPassed= "SELECT COUNT(*) as passedStudents from(SELECT sid,COUNT(*) as passedSubjects\n" +
                            "from 8marksheet WHERE marks>=30\n" +
                            "and semester = '"+semester+"' and term ='"+term+"'\n" +
                            "GROUP BY sid) \n" +
                            "AS pass_count where passedSubjects = 3;";
                    PreparedStatement passedStatement = Database.con.prepareStatement(getPassed);
                    ResultSet count = passedStatement.executeQuery();
                    count.next();

                    Integer passedCount = count.getInt("passedStudents");
                    series.getData().add(new XYChart.Data<>(batches.getString("faculty_name"),passedCount));
                }
                seriesCollection.add(series);
            }
            testGraph.getData().addAll(seriesCollection);
        }
    }

    private void initDashboard() {
        try {
            String sql = "SELECT COUNT(*) from students";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            result.next();
            String count = result.getString("COUNT(*)");
            studentCount.setText(count);

            String sql2 = "SELECT COUNT(*) from teachers";
            PreparedStatement statement2 = Database.con.prepareStatement(sql2);
            ResultSet result2 = statement2.executeQuery();
            result2.next();
            String count2 = result2.getString("COUNT(*)");
            teacherCount.setText(count2);

            String sql3 = "SELECT COUNT(*) from department";
            PreparedStatement statement3 = Database.con.prepareStatement(sql3);
            ResultSet result3 = statement3.executeQuery();
            result3.next();
            String count3 = result3.getString("COUNT(*)");
            departmentCount.setText(count3);

            String sql4 = "SELECT COUNT(*) from faculty";
            PreparedStatement statement4 = Database.con.prepareStatement(sql4);
            ResultSet result4 = statement4.executeQuery();
            result4.next();
            String count4 = result4.getString("COUNT(*)");
            facultyCount.setText(count4);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /////////////////////////////////Department//////////////////////////
    @FXML
    private TableColumn<Department, Integer> d_Id;
    @FXML
    private TableColumn<Department, String> d_name;
    @FXML
    private TableColumn<Department, Integer> d_hod;

    @FXML
    private TableView<Department> departmentTable;

    @FXML
    private TableColumn<Faculty, Integer> f_Id;
    @FXML
    private TableColumn<Faculty, String> f_name;
    @FXML
    private TableColumn<Faculty, Integer> f_did;

    @FXML
    private TableView<Faculty> facultyTable;
    @FXML
    private TextField dptHOD;
    @FXML
    private TextField deptHOD;
    @FXML
    private TextField deptName;


    @FXML
    private TextField dptName;
    @FXML
    private TextField deptId;

    @FXML
    void onAddDepartment(ActionEvent event) {
        // if department values are valid try and save it in the database
            if (!deptName.getText().isEmpty()) {
                String push = Database.addDepartment(deptName.getText(), deptHOD.getText());
                if (Objects.equals(push, "Success")) {
                    Alert.show(alertLabel, "Update Done!");
                    deptName.setText("");
                    deptHOD.setText("");
                } else {
                    Alert.show(alertLabel, push);
                }
            }
    }
    @FXML
    void onUpdateDepartment(ActionEvent event) {
        if (!dptName.getText().isEmpty()) {
            String push = Database.updateDepartment(deptId.getText(),dptName.getText(), dptHOD.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                dptName.setText("");
                dptHOD.setText("");
                deptId.setText("");

            } else {
                Alert.show(alertLabel, push);
            }
        }
    }

    @FXML
    void onDeleteDepartment(ActionEvent event) throws SQLException {
        if (!dptName.getText().isEmpty()) {
            String push = Database.deleteDepartment(deptId.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                dptName.setText("");
                dptHOD.setText("");
                deptId.setText("");
            } else {
                Alert.show(alertLabel, push);
            }
        }
    }



    //////////////////////////Faaculty////////////////////////////////

    @FXML
    private TextField fDid;
    @FXML
    private TextField fid;

    @FXML
    private TextField fName;
    @FXML
    private TextField facDid;

    @FXML
    private TextField facName;

    @FXML
    void onAddFaculty(ActionEvent event) {
        // if Faculty values are valid try and save it in the database
        if (!facName.getText().isEmpty()) {
            String push = Database.addFaculty(facName.getText(), facDid.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                facName.setText("");
                facDid.setText("");
            } else {
                Alert.show(alertLabel, push);
            }
        }
    }
    @FXML
    void onUpdateFaculty(ActionEvent event) throws SQLException {
        if (!fName.getText().isEmpty()) {
            String push = Database.updateFaculty(fid.getText(),fName.getText(), fDid.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                fName.setText("");
                fDid.setText("");
                fid.setText("");
            } else {
                Alert.show(alertLabel, push);
            }
        }
    }
    @FXML
    void onDeleteFaculty(ActionEvent event) throws SQLException {
        if (!dptName.getText().isEmpty()) {
            String push = Database.deleteFaculty(deptId.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                deptName.setText("");
                deptHOD.setText("");
            } else {
                Alert.show(alertLabel, push);
            }
        }
    }

    //////////////////////////////////// BATCH
    //////////////////////////////////// ///////////////////////////////////////
    @FXML
    private TabPane batchTab;

    @FXML
    private TextField b_year;
    @FXML
    private TextField b_fId;
    @FXML
    private TextField b_semester;
    @FXML
    private TextField b_year1;
    @FXML
    private TextField b_fId1;
    @FXML
    private TextField b_semester1;
    @FXML
    private TextField b_Id;

    /////
    @FXML
    private TableColumn<Batch,Integer> batch_Id;
    @FXML
    private TableColumn<Batch,Integer> b_Faculty;

    @FXML
    private TableColumn<Batch, String> b_Semester;

    @FXML
    private TableColumn<Batch, String> b_Year;
    @FXML
    private TableView<?> batchTable;

    @FXML
    void onBatch(ActionEvent event) throws InterruptedException, SQLException {

        onMainButton(event);
        batchTab.setVisible(true);
        b_year1.setText("");
        b_fId1.setText("");
        b_semester1.setText("");

        BatchView batchView= new BatchView(b_Id,b_year1,b_fId1,b_semester1, (TableView<Batch>) batchTable,batch_Id,b_Year,b_Faculty,b_Semester);
    }

    @FXML
    void onAddBatch(ActionEvent event) {
        // if Faculty values are valid try and save it in the database
        if (!b_year.getText().isEmpty() && !b_fId.getText().isEmpty() && !b_semester.getText().isEmpty()) {
            String push = Database.addBatch(b_year.getText(), b_fId.getText(), b_semester.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                fDid.setText("");
                fName.setText("");
            } else {
                Alert.show(alertLabel, push);
            }
        }
    }

    @FXML
    void onEditBatch(ActionEvent event) {
        if (!b_year1.getText().isEmpty() && !b_fId1.getText().isEmpty() && !b_semester1.getText().isEmpty()) {
            String push = Database.editBatch(b_Id.getText(),b_year1.getText(), b_fId1.getText(), b_semester1.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                b_year1.setText("");
                b_fId1.setText("");
                b_semester1.setText("");
                b_Id.setText("");
            } else {
                Alert.show(alertLabel, push);
            }
        }
    }
    @FXML
    void onDeleteBatch(ActionEvent event) throws SQLException {
        String push = null;
        if (!s_Id.getText().isEmpty()) {
            push = Database.deleteBatch(s_Id.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
            } else {
                Alert.show(alertLabel, push);
            }
        }else{
            Alert.show(alertLabel,"Empty is id!");
        }
    }
    ///////////////////////////// ATTENDENCE /////////////////////////
    @FXML
    private TableColumn<AttendenceStudent, String> aDateComumn;

    @FXML
    private TableColumn<AttendenceStudent, String> aFacultyColumn;

    @FXML
    private TableColumn<AttendenceStudent, String> aNameColumn;

    @FXML
    private TableColumn<AttendenceStudent, String> aSemester;

    @FXML
    private TableColumn<AttendenceStudent, Integer> aSidColumn;

    @FXML
    private TableColumn<AttendenceStudent, String> aTimeColumn;
    @FXML
    private TableView<AttendenceStudent> attendenceTable;
    @FXML
    private TabPane attendenceTab;
    @FXML
    private DatePicker aDatePicker;
    @FXML
    private TextField aBatchID;
    AttendenceView attendenceView;
    @FXML
    void onAttendence(ActionEvent event) throws IOException {

        onMainButton(event);
        attendenceTab.setVisible(true);
        attendenceView = new AttendenceView(attendenceTable,aDateComumn,aFacultyColumn,aNameColumn,aSemester,aSidColumn,aTimeColumn);

    }
    @FXML
    void startAttendence(ActionEvent event) throws IOException{
        Attendence attendence = new Attendence();
        attendence.run();
    }
    @FXML
    void onChangeAttendenceDate(ActionEvent event) {
        if(!aDatePicker.getValue().toString().isEmpty() && !aBatchID.getText().isEmpty()){
            attendenceView.updateTable(aBatchID.getText(),aDatePicker.getValue().toString());
        }
    }



    ///////////////////////////////////// MARKS
    ///////////////////////////////////// ///////////////////////////////////////
    @FXML
    private TextField mStdID;

    @FXML
    private TextField mStdMarks;

    @FXML
    private TableColumn<Student, String> mStudentBatch;

    @FXML
    private TableColumn<Student, Integer> mStudentID;

    @FXML
    private TableColumn<Student, String> mStudentName;

    @FXML
    private TableView<Student> mStudentSearchTable;

    @FXML
    private TableColumn<Student, String> mStudentSemester;

    @FXML
    private TextField mSubID;

    @FXML
    private TabPane marksheetTab;
    @FXML
    private ChoiceBox<String> mTermList;

    @FXML
    private ChoiceBox<String> mSemList;
    @FXML
    private TableView<Marks> mvTable;

    @FXML
    private TableColumn<Marks, Integer> mvtMID;

    @FXML
    private TableColumn<Marks, Float> mvtMarks;

    @FXML
    private TableColumn<Marks, String> mvtName;

    @FXML
    private TableColumn<Marks, String> mvtSemester;

    @FXML
    private TableColumn<Marks, String> mvtSubject;

    @FXML
    private TableColumn<Marks, String> mvtTerminal;
    @FXML
    private TextField mViewBID;

    @FXML
    private ChoiceBox<String> mViewSem;

    @FXML
    private ChoiceBox<String> mViewTerminal;
    @FXML
    void onMViewSearch(ActionEvent event) {
        if(!mViewBID.getText().isEmpty() && !mViewTerminal.getValue().isEmpty() && !mViewSem.getValue().isEmpty()){
            MarksView marksView = new MarksView(mvTable,mvtMID,mvtMarks,mvtName,mvtSemester,mvtSubject,mvtTerminal,mViewBID.getText(),mViewTerminal.getValue(),mViewSem.getValue());

        }else{
            Alert.show(alertLabel,"Empty fields!");
        }

    }
    @FXML
    void onAddSubjectMarks(ActionEvent event) {
        if (!mStdID.getText().isEmpty() && !mStdMarks.getText().isEmpty() && !mSubID.getText().isEmpty()
                && !mSemList.getValue().isEmpty() && !mTermList.getValue().isEmpty()) {
            String push = Database.addMarks(mSubID.getText(), mSemList.getValue(), mStdMarks.getText(),
                    mStdID.getText(), mTermList.getValue());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                mStdID.setText("");
                mStdMarks.setText("");
                mSubID.setText("");
                mSemList.setValue("");
                mTermList.setValue("");
            } else {
                Alert.show(alertLabel, push);
                mStdID.setText("");
                mStdMarks.setText("");
                mSubID.setText("");
                mSemList.setValue("");
                mTermList.setValue("");
            }
        } else {
            Alert.show(alertLabel, "Please fill the form properly!");
        }
    }

    @FXML
    void onMarksSearchSubject(KeyEvent event ){
        searchResultList.getItems().clear();
        ArrayList<String> result = Database.searchSubject(mSubID.getText());
        searchResultList.getItems().addAll(result);
        searchResults.setVisible(true);
    }
    @FXML
    void onMarks(ActionEvent event) throws InterruptedException {
        onMainButton(event);
        marksheetTab.setVisible(true);
        mSemList.getItems().clear();
        mViewSem.getItems().clear();
        mViewTerminal.getItems().clear();
        mTermList.getItems().clear();
        mSemList.getItems().addAll(semesters);
        mTermList.getItems().addAll(terminals);

        mViewSem.getItems().addAll(semesters);
        mViewTerminal.getItems().addAll(terminals);
    }

    @FXML
    void onMSearchStudent(KeyEvent event) {
        System.out.println("hmm");
        if (!mStdID.getText().isEmpty()) {
            MarksStudentSearch studentSearch = new MarksStudentSearch(mStdID, mStudentSearchTable, mStudentID,
                    mStudentName, mStudentBatch, mStudentSemester);

        }
    }

    ///////////////////////////// NOTICE /////////////////////////
    @FXML
    private TextArea announcementField;

    @FXML
    private Label announcementLabel;

    @FXML
    private Button announcementbtn;
    @FXML
    private TabPane noticeTab;

    @FXML
    private Button noticebtn;
    @FXML
    private TextField selectiveBatch;

    @FXML
    private TextArea selectiveField;

    @FXML
    private Label selectiveNotice;

    @FXML
    private TextField selectiveSemester;

    @FXML
    private Button selectivebtn;
    @FXML
    private TextField noticeMBatch;

    @FXML
    private ChoiceBox<String> noticeMSem;

    @FXML
    private ChoiceBox<String> noticeMTerm;
    //on marksheet email!!
    @FXML
    void onMarksheetEmail(ActionEvent event) {
        if(!noticeMTerm.getValue().isEmpty() && !noticeMSem.getValue().isEmpty() && !noticeMBatch.getText().isEmpty()){
            showProgress(event);
            Task<String> marksEmail = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return Notice.sendMarksheetToParents(noticeMBatch.getText(),noticeMTerm.getValue(),noticeMSem.getValue());
                }
            };
            marksEmail.valueProperty().addListener((observable,newvalue,oldvalue) ->{
                if(Objects.equals(newvalue, "Done")){
                    hideProgress(event);
                    Alert.show(alertLabel,"Sent marksheet of <term> to all students in <batch> batchID of <sem>".replace("<batch>",noticeMBatch.getText()).replace("<sem>",noticeMSem.getValue()).replace("<term>",noticeMTerm.getValue()));
                }else{
                    hideProgress(event);
                    Alert.show(alertLabel,newvalue);
                }
            });
            Alert.show(alertLabel,"Sending email... please be patient while email is being sent in BACKGROUND!");
            Thread addStdThread = new Thread(marksEmail);
            addStdThread.setDaemon(true);
            addStdThread.start();
        }else{
            Alert.show(alertLabel,"Please fill the form properly");
        }
    }

    @FXML
    void onNotice(ActionEvent event) {

        onMainButton(event);
        noticeTab.setVisible(true);
        noticeMTerm.getItems().clear();
        noticeMSem.getItems().clear();
        noticeMTerm.getItems().addAll(terminals);
        noticeMSem.getItems().addAll(semesters);
    }

    @FXML
    void onSelectiveannouncement(ActionEvent event) {
        String text = selectiveField.getText();
        String subject = "Notice from Campus Flow";
        int batchId;
        batchId = Integer.parseInt(selectiveBatch.getText());

        List<String> batchSemesterStudentEmails = Notice.getStudentEmailsByBatchAndSemester(Integer.parseInt(String.valueOf(batchId)));

        System.out.println("All Student Emails:");
        for (String email : batchSemesterStudentEmails) {
            System.out.println(email);
        }
        Address[] toAddresses = new InternetAddress[batchSemesterStudentEmails.size()];
        for (int i = 0; i < batchSemesterStudentEmails.size(); i++) {
            try {
                toAddresses[i] = new InternetAddress(batchSemesterStudentEmails.get(i));
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }

        showProgress(event);
        Task<String> emailTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return EmailSender.sendEmail(toAddresses, subject, text);
            }
        };
        emailTask.valueProperty().addListener((oberver,oldvalue,newvalue)->{
            if(newvalue.equals("Done")){
                hideProgress(event);
                Alert.show(alertLabel,"Success");
            }
        });
        Thread emailThread = new Thread(emailTask);
        emailThread.setDaemon(true);
        emailThread.start();
    }

    @FXML
    void onAnnouncement(ActionEvent event) {
        String text = announcementField.getText();
        String subject = "Notice from Campus Flow";
        List<String> studentEmails = Notice.getStudentEmails();

        Address[] toAddresses = new InternetAddress[studentEmails.size()];
        for (int i = 0; i < studentEmails.size(); i++) {
            try {
                toAddresses[i] = new InternetAddress(studentEmails.get(i));
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }

        EmailSender.sendEmail(toAddresses, subject, text);
    }

    //////////////////////////// Parents ///////////////////////////

    @FXML
    private TabPane parentsTab;

    @FXML
    private TextField p_address;

    @FXML
    private TextField p_contact;

    @FXML
    private TextField p_email;

    @FXML
    private TextField p_firstname;

    @FXML
    private TextField p_lastname;

    @FXML
    private TextField p_middlename;

    @FXML
    private TableColumn<Parent, Integer> pID;
    @FXML
    private TableColumn<Parent, String> pName;
    @FXML
    private TableColumn<Parent, String> pAddress;
    @FXML
    private TableColumn<Parent, String> pEmail;
    @FXML
    private TableColumn<Parent, Long> pContact;

    @FXML
    private TableView<Parent> parentTable;

    //////////
    @FXML
    private Button searchbutton1;

    @FXML
    private TableColumn<Parent, Integer> pID1;
    @FXML
    private TableColumn<Parent, String> pName1;
    @FXML
    private TableColumn<Parent, String> pAddress1;
    @FXML
    private TableColumn<Parent, String> pEmail1;
    @FXML
    private TableColumn<Parent, Long> pContact1;

    @FXML
    private TableView<Parent> parentTable1;


    ///////////////
    @FXML
    private TextField parentField1;

    @FXML
    private TableView<Parent> parentTable2;

    @FXML
    private Button deleteSearchParentbtn;

    @FXML
    private TextField parentField2;
    @FXML
    private TableColumn<Parent, Integer> pID2;
    @FXML
    private TableColumn<Parent, String> pName2;
    @FXML
    private TableColumn<Parent, String> pAddress2;
    @FXML
    private TableColumn<Parent, String> pEmail2;
    @FXML
    private TableColumn<Parent, Long> pContact2;


    ///////update
    @FXML
    private TextField up_address;

    @FXML
    private TextField up_contact;

    @FXML
    private TextField up_email;

    @FXML
    private TextField up_firstname;

    @FXML
    private TextField up_lastname;

    @FXML
    private TextField up_middlename;

    @FXML
    private TextField up_pid;

    @FXML
    private TextField parentUpdateField;

    @FXML
    void onParentUs(ActionEvent event){
        if (!parentUpdateField.getText().isEmpty()) {
            String search = parentUpdateField.getText();
            ParentView parentView = new ParentView(search);
            Parent parent = parentView.getParent();
            up_firstname.setText(parent.getPfirst_name());
            up_middlename.setText(parent.getPmiddle_name());
            up_lastname.setText(parent.getPlast_name());
            up_address.setText(parent.getpAddress());
            up_contact.setText(String.valueOf(parent.getpContact()));
            up_email.setText(parent.getpEmail());
        }
    }
    @FXML
    void onUpdatParent(ActionEvent event) throws AddressException, SQLException {
        String push = null;
        if (!parentUpdateField.getText().isEmpty()) {
            if (!up_address.getText().isEmpty() && !up_contact.getText().isEmpty() && !up_email.getText().isEmpty()
                    && !up_firstname.getText().isEmpty() && !up_lastname.getText().isEmpty()) {

                String firstName = up_firstname.getText();
                String middleName = up_middlename.getText();
                String lastName = up_lastname.getText();
                String address = up_address.getText();
                String contact = up_contact.getText();
                String email = up_email.getText();

                push = Database.updateParent(parentUpdateField.getText(), firstName, middleName, lastName, address, contact, email);
                if (Objects.equals(push, "Success")) {
                    Alert.show(alertLabel, "Update Done!");
                } else {
                    Alert.show(alertLabel, push);
                }
            }
        }
        else{
            Alert.show(alertLabel,"Empty ID");
        }
    }

    @FXML
    void onParents(ActionEvent event){
        onMainButton(event);
        parentsTab.setVisible(true);
        ParentView newParent = new ParentView(parentTable,pID,pName,pAddress,pContact,pEmail);
    }


    @FXML
    void onAddParent(ActionEvent event) throws IOException, WriterException, AddressException {
        if (!p_address.getText().isEmpty()
                && !p_contact.getText().isEmpty()
                && !p_email.getText().isEmpty() && !p_firstname.getText().isEmpty() && !p_lastname.getText().isEmpty()) {
            String push = Database.addParent(p_firstname.getText(), p_middlename.getText(), p_lastname.getText(),
                    p_address.getText(), p_contact.getText(), p_email.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
            } else {
                Alert.show(alertLabel, push);
            }
        }
    }
    @FXML
    void onSearchParent(ActionEvent event) throws SQLException {
        handleSearch(parentField1, parentTable1, pID1, pName1, pAddress1, pContact1, pEmail1);
    }

    @FXML
    void onSearchParent1(ActionEvent event) throws SQLException {
        handleSearch(parentField2, parentTable2, pID2, pName2, pAddress2, pContact2, pEmail2);
    }

    private void handleSearch(TextField textField, TableView tableView,
                              TableColumn idColumn, TableColumn nameColumn, TableColumn addressColumn,
                              TableColumn contactColumn, TableColumn emailColumn) throws SQLException {
        if (!textField.getText().isEmpty()) {
            System.out.println(textField.getText());
            ParentView parentView = new ParentView(textField.getText(), tableView, idColumn, nameColumn, addressColumn,
                    contactColumn, emailColumn);
        }
    }

    @FXML
    void onDeleteParent(ActionEvent event) throws SQLException {
        String push = null;
        if (!parentField2.getText().isEmpty()) {
            System.out.println(parentField2.getText());
            push = Database.deleteParent(parentField2.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                parentTable2.getItems().clear();
            } else {
                Alert.show(alertLabel, push);
            }
        }else{
            Alert.show(alertLabel,"Empty is id!");
        }


    }

    ///////////////////////////////////////////// select
    ///////////////////////////////////////////// tab////////////////////////////////
    @FXML
    private Button homebtn;
    @FXML
    private Button teachersbtn;
    @FXML
    private Button attendencebtn;
    @FXML
    private Button parentsbtn;
    @FXML
    private Button batchbtn;
    @FXML
    private Button subjectsbtn;
    @FXML
    private Button marksbtn;

    void onMainButton(ActionEvent event) {
        teachersTab.setVisible(false);
        teachersbtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        attendencebtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        studentsTab.setVisible(false);
        studentsbtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        settingsTab.setVisible(false);
        settingsbtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        homeTab.setVisible(false);
        homebtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        subjectTab.setVisible(false);
        subjectsbtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        batchTab.setVisible(false);
        batchbtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        marksheetTab.setVisible(false);
        marksbtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        attendenceTab.setVisible(false);
        attendencebtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        noticeTab.setVisible(false);
        noticebtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

        parentsTab.setVisible(false);
        parentsbtn.setStyle("-fx-background-color: none;-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");


        Button clickedButton = (Button) event.getSource();
        clickedButton.setStyle(
                "-fx-background-color: linear-gradient(to right,#b625d6,#9157ec,#6773f8,#3987fa,#0997f4);-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");
    }

}
