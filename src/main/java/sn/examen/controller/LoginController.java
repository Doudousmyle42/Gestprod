package sn.examen.controller;

import sn.examen.dao.ProduitDAO;
import sn.examen.dao.UtilisateurDAO;
import sn.examen.model.Produit;
import sn.examen.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;

public class LoginController {

    @FXML private TextField     loginField;
    @FXML private PasswordField passwordField;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    public void seConnecter() {
        String login = loginField.getText().trim();
        String pass  = passwordField.getText().trim();

        if (login.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention",
                    "Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur u = utilisateurDAO.findByLoginPassword(login, pass);

        if (u == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Login ou mot de passe incorrect");
        } else {
            verifierStockFaible();
            ouvrirEcranPrincipal();
        }
    }

    private void verifierStockFaible() {
        List<Produit> lowStock = new ProduitDAO().findLowStock();
        if (!lowStock.isEmpty()) {
            StringBuilder sb = new StringBuilder(
                    "ATTENTION ! Stock faible pour :\n\n");
            for (Produit p : lowStock) {
                sb.append("  - ").append(p.getLibelle())
                        .append(" : ").append(p.getQuantite()).append(" unites\n");
            }
            showAlert(Alert.AlertType.WARNING, "Stock Faible !", sb.toString());
        }
    }

    private void ouvrirEcranPrincipal() {
        try {
            // ← Utilise getClass().getResource avec le bon chemin
            java.net.URL url = getClass().getResource("/sn/examen/main.fxml");

            // Affiche le chemin dans la console pour debug
            System.out.println("Chemin main.fxml : " + url);

            if (url == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "main.fxml introuvable ! Verifie qu'il est dans " +
                                "src/main/resources/sn/examen/main.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load(), 900, 600));
            stage.setTitle("Ecran Principal");
            stage.show();

            // Fermer la fenetre de connexion
            ((Stage) loginField.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur ouverture main : " + e.getMessage());
        }
    }

    @FXML
    public void ouvrirInscription() {
        try {
            java.net.URL url = getClass().getResource("/sn/examen/inscription.fxml");

            System.out.println("Chemin inscription.fxml : " + url);

            if (url == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "inscription.fxml introuvable !");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load(), 450, 550));
            stage.setTitle("Inscription");
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur ouverture inscription : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
