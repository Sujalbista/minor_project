package com.smartcollege.smartcollege;

import Encryption.Encryption;
import com.smartcollege.smartcollege.EntityClass.AttendenceStudent;
import com.smartcollege.smartcollege.EntityClass.Student;
import com.smartcollege.smartcollege.database.Notice;
import com.smartcollege.smartcollege.tableview.AttendenceView;
import com.smartcollege.smartcollege.tableview.MarksStudentSearch;
import com.smartcollege.smartcollege.tableview.StudentView;
import com.google.zxing.WriterException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.smartcollege.smartcollege.database.Database;
import org.json.simple.*;
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
import java.util.List;
import java.util.Objects;

public class HelloController {
    private String[] semesters = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII" };
    private String[] terminals = { "1st Term", "2ndTerm", "3rd Term", "Pre-board" };
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
    private TextField sid;

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
    void onStudents(ActionEvent event) {
        onMainButton(event);
        studentsTab.setVisible(true);

        StudentView studentView = new StudentView(studentTable, stdID, stdName, stdAddress, stdContact, stdEmail,
                stdFaculty, stdBatch, stdParentId);

    }

    @FXML
    void onAddStudent(ActionEvent event) throws IOException, WriterException, AddressException {
        // if department values are valid try and save it in the database
        if (!s_address.getText().isEmpty() && !s_pid.getText().isEmpty() && !s_bid.getText().isEmpty()
                && !sid.getText().isEmpty() && !s_contact.getText().isEmpty()
                && !s_email.getText().isEmpty() && !s_entrancescore.getText().isEmpty() && !s_fid.getText().isEmpty()
                && !s_firstname.getText().isEmpty() && !s_lastname.getText().isEmpty()) {
            String push = Database.addStudent(s_firstname.getText(), s_middlename.getText(), s_lastname.getText(),
                    s_address.getText(), s_contact.getText(), s_email.getText(), s_entrancescore.getText(),
                    s_fid.getText(), s_bid.getText(), s_pid.getText(), sid.getText());
            if (Objects.equals(push, "Success")) {
                com.smartcollege.smartcollege.Alert.show(alertLabel, "Update Done!");
                dptName.setText("");
                dptId.setText("");
                dptHOD.setText("");
            } else {
                com.smartcollege.smartcollege.Alert.show(alertLabel, push);
            }
        }
    }

    ///////////////////////////////// SUBJECTS////////////////////////////////////////////////////////
    @FXML
    private TextField subjectName;

    @FXML
    private TextField subSemester;
    @FXML
    private TabPane subjectTab;

    @FXML
    void onAddSubject(ActionEvent event) {
        if (!subjectName.getText().isEmpty() && !subSemester.getText().isEmpty()) {
            String push = Database.addSubject(subjectName.getText(), subSemester.getText());
            if (Objects.equals(push, "Success")) {
                com.smartcollege.smartcollege.Alert.show(alertLabel, "Update Done!");
                dptName.setText("");
                dptId.setText("");
                dptHOD.setText("");
            } else {
                com.smartcollege.smartcollege.Alert.show(alertLabel, push);
            }
        } else {
            com.smartcollege.smartcollege.Alert.show(alertLabel, "Fields are empty!");
        }
    }
    @FXML
    void onSubjects(ActionEvent event) {
        onMainButton(event);
        subjectTab.setVisible(true);

    }
    ////////////////////////////////// TeachersTAB//////////////////////////////////
    //
    @FXML
    private TabPane teachersTab;
    @FXML
    private TextField tAddress;

    @FXML
    private TextField tContact;

    @FXML
    private TextField tEmail;

    @FXML
    private TextField tFaculty;

    @FXML
    private TextField tFirstName;

    @FXML
    private TextField tLastName;

    @FXML
    private TextField tMiddleName;

    @FXML
    private TextField tSubject;

    @FXML
    void onAddTeacher(ActionEvent event) {
        if (!tFirstName.getText().isEmpty() && !tMiddleName.getText().isEmpty() && !tLastName.getText().isEmpty()
                && !tAddress.getText().isEmpty() && !tContact.getText().isEmpty()
                && !tEmail.getText().isEmpty() && !tFaculty.getText().isEmpty() && !tSubject.getText().isEmpty()) {
            String push = Database.addTeacher(tFirstName.getText(), tMiddleName.getText(), tLastName.getText(),
                    tAddress.getText(), tContact.getText(), tEmail.getText(), tFaculty.getText(), tSubject.getText());
            if (Objects.equals(push, "Success")) {
                com.smartcollege.smartcollege.Alert.show(alertLabel, "Update Done!");
                dptName.setText("");
                dptId.setText("");
                dptHOD.setText("");
            } else {
                com.smartcollege.smartcollege.Alert.show(alertLabel, push);
            }
        }
    }

    @FXML
    void onTeachers(ActionEvent event) {
        onMainButton(event);
        teachersTab.setVisible(true);
    }

    @FXML
    void onLogin(ActionEvent event) throws InterruptedException {
        BackgroundFill greenFill = new BackgroundFill(Color.GREEN, null, null);
        Background green = new Background(greenFill);
        BackgroundFill redFIll = new BackgroundFill(Color.GREEN, null, null);
        Background red = new Background(redFIll);
        loginbutton.setVisible(false);
        Stage mainwindow = (Stage) loginbutton.getScene().getWindow();
        if (USERNAME.getText().equals("admin") && PASSWORD.getText().equals(("admin"))) {
            feedback.setText("Success! Opening Dashboard...");
            feedback.setTextFill(Color.GREEN);
            HelloApplication.loggedin = true;
            com.smartcollege.smartcollege.Dashboard dashboard = new com.smartcollege.smartcollege.Dashboard(mainwindow);

        } else {
            feedback.setText("Failed! Try again!");
            feedback.setTextFill(Color.RED);
            USERNAME.setText("");
            PASSWORD.setText("");
            loginbutton.setVisible(true);
        }
    }

    ////// =================== Home TAB =======================//
    @FXML
    private TabPane homeTab;

    @FXML
    private Label studentCount;

    @FXML
    void onHome(ActionEvent event) throws InterruptedException {
        onMainButton(event);
        homeTab.setVisible(true);
        initDashboard();
    }

    private void initDashboard() {
        try {
            String sql = "SELECT COUNT(sid) from students";
            PreparedStatement statement = Database.con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            result.next();
            String count = result.getString("COUNT(sid)");
            studentCount.setText(count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField dptHOD;

    @FXML
    private TextField dptId;

    @FXML
    private TextField dptName;

    @FXML
    void onAddDepartment(ActionEvent event) {
        // if department values are valid try and save it in the database
        if (!dptId.getText().isEmpty() && !dptName.getText().isEmpty()) {
            String push = Database.addDepartment(dptId.getText(), dptName.getText(), dptHOD.getText());
            if (Objects.equals(push, "Success")) {
                com.smartcollege.smartcollege.Alert.show(alertLabel, "Update Done!");
                dptName.setText("");
                dptId.setText("");
                dptHOD.setText("");
            } else {
                com.smartcollege.smartcollege.Alert.show(alertLabel, push);
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
        // if Faculty values are valid try and save it in the database
        if (!fId.getText().isEmpty() && !fName.getText().isEmpty()) {
            String push = Database.addFaculty(fId.getText(), fName.getText(), fDid.getText());
            if (Objects.equals(push, "Success")) {
                com.smartcollege.smartcollege.Alert.show(alertLabel, "Update Done!");
                fDid.setText("");
                fId.setText("");
                fName.setText("");
            } else {
                com.smartcollege.smartcollege.Alert.show(alertLabel, push);
            }
        }
    }

    //////////////////////////////////// BATCH
    //////////////////////////////////// ///////////////////////////////////////
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
        onMainButton(event);
        batchTab.setVisible(true);
    }

    @FXML
    void onAddBatch(ActionEvent event) {
        // if Faculty values are valid try and save it in the database
        if (!b_year.getText().isEmpty() && !b_fId.getText().isEmpty() && !b_semester.getText().isEmpty()) {
            String push = Database.addBatch(b_year.getText(), b_fId.getText(), b_semester.getText());
            if (Objects.equals(push, "Success")) {
                com.smartcollege.smartcollege.Alert.show(alertLabel, "Update Done!");
                fDid.setText("");
                fId.setText("");
                fName.setText("");
            } else {
                com.smartcollege.smartcollege.Alert.show(alertLabel, push);
            }
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
        com.smartcollege.smartcollege.Attendence attendence = new com.smartcollege.smartcollege.Attendence();
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
    void onAddSubjectMarks(ActionEvent event) {
        if (!mStdID.getText().isEmpty() && !mStdMarks.getText().isEmpty() && !mSubID.getText().isEmpty()
                && !mSemList.getValue().isEmpty() && !mTermList.getValue().isEmpty()) {
            String push = Database.addMarks(mSubID.getText(), mSemList.getValue(), mStdMarks.getText(),
                    mStdID.getText(), mTermList.getValue());
            if (Objects.equals(push, "Success")) {
                com.smartcollege.smartcollege.Alert.show(alertLabel, "Update Done!");
            } else {
                com.smartcollege.smartcollege.Alert.show(alertLabel, push);
            }
        } else {
            com.smartcollege.smartcollege.Alert.show(alertLabel, "Please fill the form properly!");
        }
    }

    @FXML
    void onMarks(ActionEvent event) throws InterruptedException {
        onMainButton(event);
        marksheetTab.setVisible(true);
        mSemList.getItems().clear();
        mTermList.getItems().clear();
        mSemList.getItems().addAll(semesters);
        mTermList.getItems().addAll(terminals);
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
    void onNotice(ActionEvent event) {

        onMainButton(event);
        noticeTab.setVisible(true);
    }

    @FXML
    void onSelectiveannouncement(ActionEvent e) {
        int batchId;
        String semester;
        batchId = Integer.parseInt(selectiveBatch.getText());
        // semester= selectiveSemester.getText();

        List<String> batchSemesterStudentEmails = Notice.getStudentEmailsByBatchAndSemester(Integer.parseInt(String.valueOf(batchId)));

        System.out.println("All Student Emails:");
        for (String email : batchSemesterStudentEmails) {
            System.out.println(email);
        }

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

        com.smartcollege.smartcollege.EmailSender.sendEmail(toAddresses, subject, text);
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

        Button clickedButton = (Button) event.getSource();
        clickedButton.setStyle(
                "-fx-background-color: linear-gradient(to right,#b625d6,#9157ec,#6773f8,#3987fa,#0997f4);-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");

    }

}
