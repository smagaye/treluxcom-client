/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.utilitaire;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author ADA-MALICK
 */
public class Notification {
    
        
       public static void errorAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.showAndWait();
    }
    
    public static void warningAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.showAndWait();
    }
    
    public static void infoAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.showAndWait();
    }
    
    public static boolean confirmDialog(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(message);
        Optional result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    public static String inputDialog(String message, String title, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText("");
        dialog.setContentText(message);
        Optional result = dialog.showAndWait();
        if (result.isPresent()){
            return (String) result.get();
        } else {
            return null;
        }
    }

    	public static void notificationErr(String titre , String message)
	{
                Image img=new Image("/icone/fermer.png");
                ImageView imgv =new ImageView(img);
                imgv.setFitHeight(40);
                imgv.setFitWidth(40);
		Notifications notificationBuilder = Notifications.create()
		.title(titre)
		.text(message)
		.graphic(imgv)
		.hideAfter(Duration.seconds(5))
		.position(Pos.TOP_RIGHT)
		.onAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				
			}
		});
		notificationBuilder.show();		
	}
    	public static void notificationSuccess(String titre , String message)
	{
                Image img=new Image("/icone/valider.png");
                ImageView imgv =new ImageView(img);
                imgv.setFitHeight(40);
                imgv.setFitWidth(40);
		Notifications notificationBuilder = Notifications.create()
		.title(titre)
		.text(message)
		.graphic(imgv)
		.hideAfter(Duration.seconds(4))
		.position(Pos.CENTER_LEFT)
		.onAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				
			}
		});
		notificationBuilder.show();		
	}
        
        public static void alert(String titre , StackPane stackpane, String message){
        JFXDialogLayout content= new JFXDialogLayout();
        content.setHeading(new Text(titre));
        content.setBody(new Text(message));
        JFXDialog dialog =new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton ok=new JFXButton("OK");
        ok.setOnAction((e)->{
            dialog.close();
        });
        content.setActions(ok);
        dialog.setOnDialogOpened((e)->{});
        dialog.setOnDialogClosed((e)->{});
        dialog.show();
        }
            
        public static void showErrorMessage(Exception ex, String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur causée");
        alert.setHeaderText(title);
        alert.setContentText(content);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("La pile d'executions:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
