package com.smartcollege.smartcollege.database;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import org.json.simple.*;

public class Database{
    static Connection con=null;
    static boolean connected = false;
    Database(){}

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
        dbObject.put("password", pass);
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
            if(result.next())
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
                    System.out.println("yes");
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
}

