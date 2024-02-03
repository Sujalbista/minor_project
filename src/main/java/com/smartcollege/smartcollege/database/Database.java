package com.smartcollege.smartcollege.database;

import Encryption.Encryption;
import com.smartcollege.smartcollege.EmailSender;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.smartcollege.smartcollege.qrGenerator.generator.generateQRCode;

public class Database{
    public static Connection con=null;
    static boolean connected = false;
    Database(){}

    public static String addAttendence(String data){
        try{
        JSONParser jsonParser = new JSONParser();
        Object rawJson = jsonParser.parse(data);
        JSONObject json = (JSONObject) rawJson;
        String sid = json.get("sid").toString();
        String bid = json.get("bid").toString();
        String date = LocalDate.now().toString();
        String semester = json.get("semester").toString();

            String checkAttendence = "SELECT * FROM `"+bid+"attendence` WHERE sid="+sid+" AND date = '"+date+"';";
            PreparedStatement checkStatement = con.prepareStatement(checkAttendence);
            ResultSet result = checkStatement.executeQuery();
            result.next();

            System.out.println(result.getFetchSize());
            if(result.next()){
                return "Your attendence has already been done; ID:" +sid;

            }else{
                String sql = "Insert into "+bid+"attendence(sid,date,remarks,ofSemester)"+" values("+sid+",'"+date+"',1, '"+semester+"');";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.executeUpdate();
                return "Added attendence for student ID:" +sid;

            }


        }catch (SQLException e){
            e.printStackTrace();
            return "Failed to add attendence";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean checkDatabaseDetails(){
        try(FileReader reader = new FileReader("./src/main/resources/config/config.json")){
            System.out.println("file found");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("file not found");
            return false;
        }

    }
    public static boolean postDatabaseDetails(String host, String database,String user,String pass,String port){
        System.out.println("i am here");
        JSONObject dbObject = new JSONObject();
        dbObject.put("host",host);
        dbObject.put("port", Integer.parseInt(port));
        dbObject.put("username",user);
        try {
            dbObject.put("password", Encryption.encrypt(pass));
        }catch (Exception e){
            e.printStackTrace();
        }
        dbObject.put("database",database);
        try{
            FileWriter writer = new FileWriter("./src/main/resources/config/config.json");
            writer.write(dbObject.toJSONString());
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    public static String getConnection(String host, String database,String user,String pass,String port){
        try{
//            Class.forName("com.mysql.jdbc.Driver");
            String url ="jdbc:mysql://localhost:"+port+"/"+database;
            con = DriverManager.getConnection(url,user,pass);
            System.out.println("Successfully connected!!");
            connected=true;
            return "connected";
        }catch(SQLException e){
            e.printStackTrace();
            return e.toString();
        }
    }
    public static boolean checkTable(String table){
        boolean check = false;
        try{
            String sql = "Select * from "+table;
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            if(result.getFetchSize()>=0)
                check = true;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return check;
    }

    //////====================home===================//////////
    public static String addDepartment(String id, String name, String hod){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("department")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO department VALUES("+Integer.parseInt(id)+",'"+name+"',"+Integer.parseInt(hod)+")";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE department(did int, name varchar(20), hod int, CONSTRAINT pk_did PRIMARY KEY (did))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO department VALUES("+Integer.parseInt(id)+",'"+name+"',"+Integer.parseInt(hod)+")";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");
                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }
    public static String addFaculty(String id, String name, String did){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("faculty")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO faculty VALUES("+Integer.parseInt(id)+",'"+name+"',"+Integer.parseInt(did)+")";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE faculty(fid int, faculty_name varchar(20), did int, CONSTRAINT pk_fid PRIMARY KEY (fid))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO faculty VALUES("+Integer.parseInt(id)+",'"+name+"',"+Integer.parseInt(did)+")";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");
                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }

    public static String addBatch( String year, String fid, String semester){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("batch")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO batch(year, fid, semester) VALUES('"+year+"',"+Integer.parseInt(fid)+",'"+semester+"')";
                    PreparedStatement statement = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                    statement.executeUpdate();

                    //create attendence
                    ResultSet keys = statement.getGeneratedKeys();
                    keys.next();
                    System.out.println(keys.getInt(1));
                    String sql2 = "CREATE TABLE "+keys.getInt(1)+"attendence(aid int AUTO_INCREMENT PRIMARY KEY, sid int, date date, remarks boolean, ofSemester varchar(10))";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();

                    //create marksheet
                    String sql4 = "CREATE TABLE "+keys.getInt(1)+"marksheet(mid int AUTO_INCREMENT PRIMARY KEY, sid int, semester varchar(10),term varchar(15), subId int, marks float)";
                    PreparedStatement statement4 = con.prepareStatement(sql4);
                    statement4.executeUpdate();

                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE batch(bid int AUTO_INCREMENT PRIMARY KEY, year varchar(20), fid int, semester varchar(10))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");



                    //now insert data!
                    String sql2 = "INSERT INTO batch(year, fid, semester) VALUES('"+year+"',"+Integer.parseInt(fid)+",'"+semester+"')";
                    PreparedStatement statement2 = con.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");

                    //create attendence
                    ResultSet keys = statement2.getGeneratedKeys();
                    keys.next();
                    String sql3 = "CREATE TABLE "+keys.getInt(1)+"attendence(aid int AUTO_INCREMENT PRIMARY KEY, sid int, date date, remarks boolean, ofSemester varchar(10))";
                    PreparedStatement statement3 = con.prepareStatement(sql3);
                    statement3.executeUpdate();

                    //create marksheet
                    String sql4 = "CREATE TABLE "+keys.getInt(1)+"marksheet(mid int AUTO_INCREMENT PRIMARY KEY, sid int, semester varchar(10),term varchar(15), subId int, marks float)";
                    PreparedStatement statement4 = con.prepareStatement(sql4);
                    statement4.executeUpdate();

                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }
    public static String addStudent(String firstname,String middlename, String lastname, String adderss,
                                    String contact, String email, String entrance,
                                    String fid,String bid, String pid, String sid) throws IOException, WriterException {
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("students")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO students VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+adderss+"',"+contact+",'"+email+"',"+entrance+","+fid+","+bid+","+pid+","+sid+")";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
                ////////////////////QR//////////////////////////

                // The data that the QR code will contain
                String sem = "";
                try{
                    String sql = "SELECT semester from batch where bid ="+bid;
                    PreparedStatement statement = con.prepareStatement(sql);
                    ResultSet result = statement.executeQuery();
                    result.next();
                    sem = result.getString("semester");
                }catch (SQLException e){
                    e.printStackTrace();
                }
                String data = "{\"sid\":"+sid+"\"bid\":"+bid+",\"semester\":\""+sem+"\"}";

                // The path where the image will get saved
                String path = "./src/main/resources/images/QR.png";

                // Encoding charset
                String charset = "UTF-8";

                Map<EncodeHintType, ErrorCorrectionLevel> hashMap
                        = new HashMap<EncodeHintType,
                                                ErrorCorrectionLevel>();

                hashMap.put(EncodeHintType.ERROR_CORRECTION,
                        ErrorCorrectionLevel.L);

                generateQRCode(data, path, charset, hashMap, 200, 200);
                System.out.println("QR Code Generated!!! ");

                //////////////Email Qr code/////////////////////
                EmailSender sendmail  = new EmailSender();
                String to= email;
                String subject= "welcome from College";
                String text="This is your QR code for Attendance";
                sendmail.sendEmail(to,subject, text,path);

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE students (first_name varchar(20),middle_name varchar(20),last_name varchar(20),address varchar(50),contact long, email varchar(50),entrance_score float, fid int, bid int, pid int, sid int, CONSTRAINT pk_sid PRIMARY KEY (sid))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO students VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+adderss+"',"+contact+",'"+email+"',"+entrance+","+fid+","+bid+","+pid+","+sid+")";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");
                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }

    public static String addTeacher(String firstname,String middlename, String lastname, String adderss,
                                    String contact, String email,
                                    String fid, String sid){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("teachers")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO teachers(first_name, middle_name, last_name, address, contact, email, fid, subjectId) VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+adderss+"',"+contact+",'"+email+"',"+fid+","+sid+")";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE teachers (tid int AUTO_INCREMENT PRIMARY KEY , first_name varchar(20),middle_name varchar(20),last_name varchar(20),address varchar(50),contact long, email varchar(50),fid int, subjectId int)";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO teachers(first_name, middle_name, last_name, address, contact, email, fid, subjectId) VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+adderss+"',"+contact+",'"+email+"',"+fid+","+sid+")";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");
                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }
    public static String addSubject(String name, String semester){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("subjects")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO subjects(sub_name, semester) VALUES('"+name+"','"+semester+"')";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE subjects (subId int AUTO_INCREMENT PRIMARY KEY , sub_name varchar(20), semester varchar(10))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO subjects(sub_name, semester) VALUES('"+name+"','"+semester+"')";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");
                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }

    public static String addMarks(String subID, String semester, String marks, String sid, String terminal){
        String feedback= "";
        if(connected){
            try{
                //get batch number
                String sql = "SELECT bid from students where sid ="+sid+";";
                PreparedStatement statement = con.prepareStatement(sql);
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    Integer bid = result.getInt("bid");

                    //add marks to batch marksheet
                    String checkMarks = "SELECT * FROM `"+bid+"marksheet` WHERE sid="+sid+" AND semester = '"+semester+"';";
                    PreparedStatement checkStatement = con.prepareStatement(checkMarks);
                    ResultSet result2 = checkStatement.executeQuery();
                    result2.next();

                    System.out.println(result.getFetchSize());
                    if(result2.next()){
                        return "Marks has already been added of this terminal of student ID:" +sid;
                    }else{
                        String sql3 = "Insert into "+bid+"marksheet(subId,semester,marks,sid, term)"+" values("+subID+",'"+semester+"',"+marks+", "+sid+",'"+terminal+"');";
                        PreparedStatement statement3 = con.prepareStatement(sql3);
                        statement3.executeUpdate();
                        return "Added marks for student ID:" +sid;
                    }
                }
            }catch (SQLException e){
                feedback = e.toString();
            }
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }


}

