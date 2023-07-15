package com.illoivan.duplicatefiles.model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class UtilsAlert {
    
	private UtilsAlert() {}
	
    public static void error(String body) {
    	show(AlertType.ERROR,"ERROR",body); 
    }
    
    
    public static void info(String body) {
    	show(AlertType.INFORMATION,"INFORMATION",body);
    }
    
    public static boolean showErrorAndWait(String body) {
    	return showAndWait(AlertType.ERROR,"ERROR",body);
    }
    
    public static boolean showAndWaitConfirmation(String title,String body) {
    	return showAndWait(AlertType.CONFIRMATION,title,body);
    }
    
    private static void show(AlertType type, String title, String body) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(body);
        alert.show();
    }
    
    private static boolean showAndWait(AlertType type, String title, String body) {
    	Alert alert = new Alert(type);
    	alert.setHeaderText(title);
    	alert.setContentText(body);
    	return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}