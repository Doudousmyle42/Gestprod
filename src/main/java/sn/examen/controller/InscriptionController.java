package sn.examen.controller;

import sn.examen.dao.UtilisateurDAO;
import sn.examen.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class InscriptionController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField telephoneField;
    @FXML private TextField emailField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;

    private UtilisateurDAO UtilisateurDAO = new UtilisateurDAO();

    @FXML
    public void sInscrire() {
        String login = loginField.getText().trim();

        // Verifier si login deja utilise
        if (UtilisateurDAO.loginExists(login)) {
            showAlert(Alert.AlertType.ERROR, "Ce login est deja utilise.");
            return;
        }

        Utilisateur Utilisateur = new Utilisateur();
        Utilisateur.setNom(nomField.getText());
        Utilisateur.setPrenom(prenomField.getText());
        Utilisateur.setTelephone(telephoneField.getText());
        Utilisateur.setEmail(emailField.getText());
        Utilisateur.setLogin(login);
        Utilisateur.setPassword(passwordField.getText());

        UtilisateurDAO.save(Utilisateur);
        showAlert(Alert.AlertType.INFORMATION, "Inscription reussie ! Connectez-vous.");

        // Fermer la fenetre d'inscription
        ((Stage) nomField.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
