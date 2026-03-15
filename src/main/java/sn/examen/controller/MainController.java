package sn.examen.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML private StackPane contentArea;

    private void chargerEcran(String fxmlFile) {
        try {
            // Chemin correct : /sn/examen/ et non /fxml/
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/sn/examen/" + fxmlFile));

            if (loader.getLocation() == null) {
                System.err.println("FXML introuvable : /sn/examen/" + fxmlFile);
                return;
            }

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