package com.smartcollege.smartcollege;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class HelloControllerIntializer {
    /////////////HOME////////////////
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
}
