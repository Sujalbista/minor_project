package com.smartcollege.smartcollege;

import Encryption.Encryption;
import com.smartcollege.smartcollege.database.Database;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Dashboard{

    Dashboard(Stage stage) {
        if(HelloApplication.loggedin){
            try{
                openDashboard(stage);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private void openDashboard(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        scene.getStylesheets().add(getClass().getResource("test.css").toExternalForm());
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        readyDatabaseIfPossible();
        //stage.get

    }
    private void readyDatabaseIfPossible(){
        if(Database.checkDatabaseDetails()){
            JSONParser jsonParser = new JSONParser();
            try(FileReader reader = new FileReader("./src/main/resources/config/config.json")){
                Object obj = jsonParser.parse(reader);
                JSONObject json = (JSONObject) obj;
                String checkConnection = Database.getConnection(json.get("host").toString(),json.get("database").toString(),json.get("username").toString(), Encryption.decrypt(json.get("password").toString()),json.get("port").toString());
                if(Objects.equals(checkConnection, "connected")){
                   System.out.println("Connected to database.");
                }else{
                    System.out.println(checkConnection);
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{

        }
    }
}
