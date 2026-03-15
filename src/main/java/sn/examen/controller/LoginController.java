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

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;

    private UtilisateurDAO UtilisateurDAO = new UtilisateurDAO();

    // Methode appelee quand on clique 'Se connecter'
    @FXML
    public void seConnecter() {
        String login = loginField.getText().trim();
        String pass  = passwordField.getText().trim();

        // Validation des champs vides
        if (login.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides",
                    "Veuillez remplir tous les champs.");
            return;
        }

        // Verifier les identifiants en BD
        Utilisateur Utilisateur = UtilisateurDAO.findByLoginPassword(login, pass);

        if (Utilisateur == null) {
            // ERREUR : utilisateur non trouve
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion",
                    "Login ou mot de passe incorrect");
        } else {
            // SUCCES : verifier stock avant d'ouvrir l'appli
            verifierStockFaible();
            ouvrirEcranPrincipal();
        }
    }

    // Alerte pour les produits dont quantite < 5
    private void verifierStockFaible() {
        List<Produit> lowStock = new ProduitDAO().findLowStock();
        if (!lowStock.isEmpty()) {
            StringBuilder sb = new StringBuilder(
                    "ATTENTION ! Stock faible pour ces produits :\n\n");
            for (Produit p : lowStock) {
                sb.append("  - ").append(p.getLibelle())
                        .append(" : ").append(p.getQuantite()).append(" unites\n");
            }
            showAlert(Alert.AlertType.WARNING, "Stock Faible !", sb.toString());
        }
    }

    // Ouvrir l'ecran principal
    private void ouvrirEcranPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/main.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load(), 900, 600));
            stage.setTitle("Ecran Principal");
            stage.show();
            // Fermer la fenetre de connexion
            ((Stage) loginField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ouvrir la fenetre d'inscription
    @FXML
    public void ouvrirInscription() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/inscription.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load(), 450, 500));
            stage.setTitle("Inscription");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Methode utilitaire pour afficher les alertes
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

