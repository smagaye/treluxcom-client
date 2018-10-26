package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class ListeCommandeController implements Initializable {

    @FXML
    private VBox vboxCommande;

    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        try {
            for (Integer i=0; i<10;i++){
            Pane newPane =((Pane)FXMLLoader.load(getClass().getResource("/fxml/PlaqueCommande.fxml")));
            ((TextField)newPane.getChildren().get(1)).setText(i.toString());
            ((JFXButton)newPane.getChildren().get(0)).setOnAction((e)->{    
                System.out.println(((TextField)newPane.getChildren().get(1)).getText());;
            });
            vboxCommande.getChildren().add(newPane);
            }
           } catch (IOException ex) {
            Logger.getLogger(ListeCommandeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
