package com.treluxcom.utilitaire;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ADA-MALICK
 */
public class WindowManager {

    public static void closeWindow(ActionEvent e) {
        Stage stageclo = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stageclo.close();
    }

    public static void closeWindow(Parent e) {
        Stage stageclo = (Stage) e.getScene().getWindow();
        stageclo.close();
    }

    public static void closeWindow(Stage e) {
        Stage stageclo = ((Stage)e.getScene().getWindow());
        stageclo.close();
    }

    public void nouvelle(String titre, String url, int largeur, int hauteur) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(url));
            Scene scene = new Scene(root, largeur, hauteur);
            stage.setTitle(titre);
            stage.setScene(scene);
            stage.fullScreenProperty();
            stage.show();
        } catch (IOException err) {
            System.out.println("Erreur " + err.getMessage());
        }
    }

    public void newSceneWithoutPanel(String titre, String url, int largeur, int hauteur) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(url));
            Scene scene = new Scene(root, largeur, hauteur);
            stage.setTitle(titre);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException err) {
            System.out.println("Erreur " + err.getMessage());
        }
    }

    public void fullScreen(String titre, String url) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(url));
            Scene scene = new Scene(root);
            stage.setTitle(titre);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException err) {
            System.out.println("Erreur " + err.getMessage());
        }
    }

    /**
     *
     * @param titre
     * @param url
     * @param e
     */
    public void switchScene(String titre, String url, ActionEvent e) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(url));
            Scene scene = new Scene(root);
            stage.setTitle(titre);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException err) {
            System.out.println("Erreur " + err.getMessage());
        }
        ((Node) e.getSource()).getScene().getWindow().hide();
    }

    public static void changeScene(String titre, FXMLLoader chargeur, ActionEvent e) throws IOException {
        try {
            newStage(titre, newScene(chargeur));
        } catch (IOException err) {
            System.out.println("Erreur " + err.getMessage());
        }
        closeWindow(e);
    }

    public static void newStage(String titre, Scene scene) {
        Stage primaryStage = new Stage();
        primaryStage.setTitle(titre);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Scene newScene(FXMLLoader chargeur) throws IOException {
        Parent root = (Parent) chargeur.load();
        return new Scene(root, 600, 600);
    }

    public static Scene newScene(FXMLLoader chargeur, int largeur, int hauteur) throws IOException {
        Parent root = (Parent) chargeur.load();
        return new Scene(root, largeur, hauteur);
    }

    public Scene newScene(String url) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(url));
        return new Scene(root);
    }

    public Scene newScene(String url, int largeur, int hauteur) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(url));
        return new Scene(root, largeur, hauteur);
    }

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
        alert.setHeaderText("Confirmation");
        alert.setContentText(message);
        Optional result = alert.showAndWait();
        return result.get() == ButtonType.YES;
    }

    public static String inputDialog(String message, String title, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText("");
        dialog.setContentText(message);
        Optional result = dialog.showAndWait();
        if (result.isPresent()) {
            return (String) result.get();
        } else {
            return null;
        }
    }

    public static void HideWindow(ActionEvent e) {
        ((Node) e.getSource()).getScene().getWindow().hide();
    }

    public void quitter(ActionEvent e) {
        System.exit(0);
    }
}
