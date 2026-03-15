package sn.examen.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

public class MainController {

    @FXML private StackPane contentArea;

    // Charge dynamiquement un fichier FXML dans le contentArea
    private void chargerEcran(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/" + fxmlPath));
            Node ecran = loader.load();
            contentArea.getChildren().setAll(ecran);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void showProduit()     { chargerEcran("produit.fxml"); }
    @FXML public void showCategorie()   { chargerEcran("categorie.fxml"); }
    @FXML public void showStatistique() { chargerEcran("statistique.fxml"); }
    @FXML public void showExtrait()     { chargerEcran("extrait.fxml"); }
}

