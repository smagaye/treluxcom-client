package com.treluxcom.controlleur;

import com.treluxcom.utilitaire.FileManager;
import com.treluxcom.utilitaire.WindowManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class TestFenetreController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }    

    @FXML
    private void btnConnexion(ActionEvent event) {
        new WindowManager().nouvelle("Connexion","/fxml/Connexion.fxml",707,414);
    }

    @FXML
    private void btnMainAdmin(ActionEvent event) {
        new WindowManager().fullScreen("Administrateur", "/fxml/Administrateur.fxml");
    }

    @FXML
    private void btnMainGerant(ActionEvent event) {
        new WindowManager().nouvelle("Gérant","/fxml/MainGerant.fxml",600,400);
    }

    @FXML
    private void btnMainCaissier(ActionEvent event) {
        new WindowManager().nouvelle("Caissier","/fxml/MainCaissier.fxml",600,400);
    }

    @FXML
    private void btnMainLivreur(ActionEvent event) {
        new WindowManager().nouvelle("Livreur","/fxml/MainLivreur.fxml",600,400);
    }

    @FXML
    private void btnMainFournisseur(ActionEvent event) {
        new WindowManager().nouvelle("Fournisseur","/fxml/MainFournisseur.fxml",600,600);
    }

    @FXML
    private void btnFormulaireClient(ActionEvent event) {
         new WindowManager().nouvelle("FormulaireClient","/fxml/InscriptionClient.fxml",690,580);
    }
    @FXML
    private void btnCalendrier(ActionEvent event) {
         new WindowManager().nouvelle("FormulaireClient","/fxml/CalendrierService.fxml",690,580);
    }
    @FXML
    private void btnStatistique(ActionEvent event) {
         new WindowManager().nouvelle("FormulaireClient","/fxml/Statistique.fxml",690,580);
    }
    @FXML
    private void btnEnregCommande(ActionEvent event) {
         new WindowManager().nouvelle("FormulaireClient","/fxml/EnregistrerCommande.fxml",690,580);
    }
    @FXML
    private void btnMesEmployes(ActionEvent event) {
         new WindowManager().nouvelle("FormulaireClient","/fxml/EnregistrerEmploye.fxml",690,580);
    }
    @FXML
    private void btnMesBoutiques(ActionEvent event) {
         new WindowManager().nouvelle("FormulaireClient","/fxml/MesBoutiques.fxml",690,580);
    }
    @FXML
    private void btnFamilleBoisson(ActionEvent event) {
         new WindowManager().nouvelle("FormulaireClient","/fxml/GestionFamilleBoisson.fxml",690,580);
    }

    @FXML
    private void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void btnListeCommande(ActionEvent event) {
         new WindowManager().nouvelle("Liste Commandes","/fxml/ListeCommande.fxml",690,580);
    }
    
}
