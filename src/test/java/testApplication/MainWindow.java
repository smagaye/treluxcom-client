package testApplication;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainWindow extends Application {

    public void start(Stage primaryStage) throws IOException { 
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TestFenetre.fxml"));
        Scene scene = new Scene(root, 766,563);

        primaryStage.setTitle("Phase Test");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/images/trelux24.jpg"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
