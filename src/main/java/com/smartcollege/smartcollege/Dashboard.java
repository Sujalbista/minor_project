package com.smartcollege.smartcollege;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("test.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        scene.getStylesheets().add(getClass().getResource("test.css").toExternalForm());
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        //stage.get

    }
}
