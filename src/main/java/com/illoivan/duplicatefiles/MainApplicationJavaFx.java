package com.illoivan.duplicatefiles;

import java.io.File;
import java.io.IOException;

import com.illoivan.duplicatefiles.model.UtilsAlert;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApplicationJavaFx extends Application  {
    
    private static Scene scene;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Main"));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        return getFXML(fxml).load();
    }
    
    public static FXMLLoader getFXML(String fxml) {
    	return new FXMLLoader(MainApplicationJavaFx.class.getResource("/view/"+fxml+".fxml"));
    }

    public static void openNewWindow(FXMLLoader loader){
    	Stage stage = new Stage();
		try {
			Scene scene = new Scene(loader.load());
	        stage.setScene(scene);
		} catch (IOException e) {
	        String fileName = new File(loader.getLocation().getFile()).getName();
			UtilsAlert.error("Cannot open window "+fileName);
			e.printStackTrace();
		}
        stage.setResizable(false);
        stage.initModality(Modality.NONE);
        stage.show();
    }
    
}
