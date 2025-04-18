package com.smartcollege.smartcollege.database;

import Encryption.Encryption;
import com.smartcollege.smartcollege.EmailSender;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class User {
    public static boolean checkUserExistance(){
        try(FileReader reader = new FileReader("./src/main/resources/config/user.json")){
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public static String updateUsername(String username){
        String feedback = "";
        if(checkUserExistance()){
            //update email
            JSONParser jsonParser = new JSONParser();
            try(FileReader reader = new FileReader("./src/main/resources/config/user.json")){
                Object obj = jsonParser.parse(reader);
                JSONObject json = (JSONObject) obj;
                json.replace("username", username);

                FileWriter writer = new FileWriter("./src/main/resources/config/user.json");
                writer.write(json.toJSONString());
                writer.close();
                feedback = "Updated username!";
            }catch (IOException | ParseException e){
                feedback = e.toString();
            }
        }else{
            feedback = "Make sure you have generated password first!";
        }
        return feedback;
    }
    public static String updateEmail(String email){
        String feedback = "";
        if(checkUserExistance()){
            //update email
            JSONParser jsonParser = new JSONParser();
            try(FileReader reader = new FileReader("./src/main/resources/config/user.json")){
                Object obj = jsonParser.parse(reader);
                JSONObject json = (JSONObject) obj;
                json.replace("email", email);

                FileWriter writer = new FileWriter("./src/main/resources/config/user.json");
                writer.write(json.toJSONString());
                writer.close();
                feedback = "Updated email!";
            }catch (IOException | ParseException e){
                feedback = e.toString();
            }
        }else{
            feedback = "Make sure you have generated password first!";
        }
        return feedback;
    }
    public static String onForgetPassword(){
        String feedback = "";
        if(checkUserExistance()){
            JSONParser parser = new JSONParser();
            try(FileReader reader = new FileReader("./src/main/resources/config/user.json")){
                Object obj = parser.parse(reader);
                JSONObject json = (JSONObject) obj;
                String email = json.get("email").toString();
                if(email.isEmpty()){
                    feedback= "Sorry but no recovery email found!";
                }else{
                    String password = Encryption.generateRandomPassword(8);
                    String subject = "Password reset | smartcollege";
                    String text = "We have reset your password for admin dashboard. Make sure not to share this password. It is better if you change this generated password to your own from settings. \n\nNew Password: "+password;

                    Address[] toAddresses = new Address[] { new InternetAddress(email) };
                    EmailSender.sendEmail(toAddresses,subject,text);

                    feedback = "Please check your Email";


                }
            }catch (IOException | ParseException e){
                feedback = e.toString();
            } catch (AddressException e) {
                feedback = e.toString();
            }
        }else{
            feedback="No user found!";
        }

        return feedback;
    }
    public static String updatePassword(String oldPassword,String password){
        String feedback = "";
        if(checkUserExistance()){
            // update password of the user
            JSONParser jsonParser = new JSONParser();
            try(FileReader reader = new FileReader("./src/main/resources/config/user.json")){
                Object obj = jsonParser.parse(reader);
                JSONObject json = (JSONObject) obj;
                //check password validation
                if(Encryption.decrypt(json.get("password").toString()).equals(oldPassword)){
                    json.replace("password", Encryption.encrypt(password));
                    FileWriter writer = new FileWriter("./src/main/resources/config/user.json");
                    writer.write(json.toJSONString());
                    writer.close();
                    feedback = "Updated password!";
                }else{
                    feedback = "Password is incorrect!";
                }

            }catch (IOException | ParseException e){
                e.printStackTrace();
                feedback = e.toString();
            } catch (Exception e) {
                feedback = e.toString();
            }
        }else{
            //create user if it doesn't exist
            try{
                if(oldPassword.equals("admin")){
                    FileWriter writer = new FileWriter("./src/main/resources/config/user.json");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", "admin");
                    jsonObject.put("password", Encryption.encrypt(password));
                    jsonObject.put("email","");
                    writer.write(jsonObject.toJSONString());
                    writer.close();
                    feedback = "Successfully created user and updated password";
                }else{
                    feedback = "Password is incorrect!";
                }
            } catch (Exception e) {
                feedback = e.toString();
            }
        }
        return feedback;
    }

    public static boolean checkLogin(String username, String password){
        boolean check;
        JSONParser jsonParser = new JSONParser();
        try(FileReader reader = new FileReader("./src/main/resources/config/user.json")){
            Object obj = jsonParser.parse(reader);
            JSONObject json = (JSONObject) obj;
            check = Encryption.decrypt(json.get("password").toString()).equals(password) && json.get("username").toString().equals(username);
        } catch (Exception e) {
            check = false;
        }
        return check;
    }
}
