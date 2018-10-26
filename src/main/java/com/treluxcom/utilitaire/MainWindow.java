package com.treluxcom.utilitaire;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainWindow extends Application {

    public void start(Stage primaryStage) { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/PageInternaute.fxml"));
            Scene scene = new Scene(root, 1279, 1080);
            
            primaryStage.setTitle("Phase Test");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image("/images/trelux24.jpg"));
            primaryStage.show();
        } catch (Exception exception) {
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
