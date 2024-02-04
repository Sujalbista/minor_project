package com.smartcollege.smartcollege;

import Encryption.Encryption;
import com.smartcollege.smartcollege.EntityClass.AttendenceStudent;
import com.smartcollege.smartcollege.EntityClass.Student;
import com.smartcollege.smartcollege.database.Notice;
import com.smartcollege.smartcollege.database.User;
import com.smartcollege.smartcollege.tableview.AttendenceView;
import com.smartcollege.smartcollege.tableview.MarksStudentSearch;
import com.smartcollege.smartcollege.tableview.StudentView;
import com.google.zxing.WriterException;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import com.smartcollege.smartcollege.HelloApplication;
import com.smartcollege.smartcollege.database.Database;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    void showProgress(ActionEvent event){
        Button button = (Button)  event.getSource();
        progressBar.setVisible(true);
        button.setDisable(true);
    }
    void hideProgress(ActionEvent event){
        Button button = (Button)  event.getSource();
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
        if(User.checkUserExistance()){
            if(User.checkLogin(USERNAME.getText(),PASSWORD.getText())){
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
        }else{
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
        if(!userEmail.getText().isEmpty()){
            String feedback = User.updateEmail(userEmail.getText());
            Alert.show(alertLabel,feedback);
        }else{
            Alert.show(alertLabel,"Email is empty!");
        }

    }
    @FXML
    void onUpdateUsername(ActionEvent event) {
        if(!userUsername.getText().isEmpty()){
            String feedback = User.updateUsername(userUsername.getText());
            Alert.show(alertLabel,feedback);
        }else{
            Alert.show(alertLabel,"Tero bau le nam rakhena tero?");
        }

    }
    @FXML
    void onUpdatePassword(ActionEvent event) {
        if(!userOldPass.getText().isEmpty() && ! userNewPass.getText().isEmpty()){
            String feedback = User.updatePassword(userOldPass.getText(), userNewPass.getText());
            Alert.show(alertLabel,feedback);
        }else{
            Alert.show(alertLabel,"Password fields are empty!");
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

    /////////////
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
    void onStudents(ActionEvent event) {
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
    void onSearchStudent1(ActionEvent event) throws SQLException {
        if (!stdnfield1.getText().isEmpty()) {
            System.out.println(stdnfield.getText());
            StudentView studentView = new StudentView(stdnfield1.getText(),studentTable3, stdID11, stdName11, stdAddress11, stdContact11, stdEmail11,
                    stdFaculty11, stdBatch11, stdParentId11);
        }
    }

    @FXML
    void onDelete(ActionEvent event) throws SQLException {
        String push = null;
        if (!stdnfield1.getText().isEmpty()) {
            System.out.println(stdnfield1.getText());
            push = Database.deleteStudent(stdnfield1.getText());
        }
        if (Objects.equals(push, "Success")) {
            Alert.show(alertLabel, "Update Done!");
        } else {
            Alert.show(alertLabel, push);
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
    void onHome(ActionEvent event) throws InterruptedException {
        onMainButton(event);
        homeTab.setVisible(true);
        initDashboard();
//        showResultCompare();
        resultTerminals.getItems().clear();
        resultTerminals.getItems().addAll(terminals);
        resultSemester.getItems().clear();
        resultSemester.getItems().addAll(semesters);
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

    @FXML
    private TextField dptHOD;


    @FXML
    private TextField dptName;

    @FXML
    void onAddDepartment(ActionEvent event) {
        // if department values are valid try and save it in the database
        if (!dptName.getText().isEmpty()) {
            String push = Database.addDepartment(dptName.getText(), dptHOD.getText());
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
    private TextField fDid;

    @FXML
    private TextField fName;

    @FXML
    void onAddFaculty(ActionEvent event) {
        // if Faculty values are valid try and save it in the database
        if (!fName.getText().isEmpty()) {
            String push = Database.addFaculty(fName.getText(), fDid.getText());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
                fDid.setText("");
                fName.setText("");
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
                Alert.show(alertLabel, "Update Done!");
                fDid.setText("");
                fName.setText("");
            } else {
                Alert.show(alertLabel, push);
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
    void onAddSubjectMarks(ActionEvent event) {
        if (!mStdID.getText().isEmpty() && !mStdMarks.getText().isEmpty() && !mSubID.getText().isEmpty()
                && !mSemList.getValue().isEmpty() && !mTermList.getValue().isEmpty()) {
            String push = Database.addMarks(mSubID.getText(), mSemList.getValue(), mStdMarks.getText(),
                    mStdID.getText(), mTermList.getValue());
            if (Objects.equals(push, "Success")) {
                Alert.show(alertLabel, "Update Done!");
            } else {
                Alert.show(alertLabel, push);
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
        String subject = "Notice from Smart College";
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
    void onParents(ActionEvent event){
        onMainButton(event);
        parentsTab.setVisible(true);
    }


    @FXML
    void onAddParent(ActionEvent event) throws IOException, WriterException, AddressException {
        // if department values are valid try and save it in the database
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
