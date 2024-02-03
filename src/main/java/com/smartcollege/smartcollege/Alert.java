package com.smartcollege.smartcollege;

import javafx.scene.control.Label;

public class Alert{
    public static Label alert;
    public static String message;
    public static void show(Label label,String text){
        alert = label;
        message = text;
        alert.getParent().setVisible(true);
        alert.setText(message);
    }
}
