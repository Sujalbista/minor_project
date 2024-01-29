package com.smartcollege.smartcollege.database;

import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Database{
    static Connection con=null;
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
    public Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url ="jdbc:mysql://localhost:3306/subu";
            String user = "subu";
            String pass="1234";
            con = DriverManager.getConnection(url,user,pass);

        }catch(SQLException |ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.println("Successfully connected!!");
        return con;
    }

//    public static void main(String [] args){
//        Database test = new Database();
//        try{
//            String sql = "Select * from employee";
//
//            PreparedStatement statement = test.con.prepareStatement(sql);
//            ResultSet result = statement.executeQuery();
//            while(result.next()){
//                String name = result.getString("name");
//                System.out.print(name);
//            }
//            test.con.close();
//
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//
//
//    }
}

