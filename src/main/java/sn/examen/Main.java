package sn.examen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Le chemin doit correspondre exactement à où est le fichier FXML
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/sn/examen/login.fxml"));

        // Verification : si null, le fichier n'est pas trouvé
        if (loader.getLocation() == null) {
            throw new RuntimeException("login.fxml introuvable !");
        }

        Scene scene = new Scene(loader.load(), 500, 400);
        primaryStage.setTitle("Application JavaFX - Examen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}