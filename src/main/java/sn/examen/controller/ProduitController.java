package sn.examen.controller;

import sn.examen.dao.CategorieDAO;
import sn.examen.dao.ProduitDAO;
import sn.examen.model.Categorie;
import sn.examen.model.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProduitController implements Initializable {

    // Champs du formulaire (lie au FXML via fx:id)
    @FXML private TextField libelleField;
    @FXML private TextField quantiteField;
    @FXML private TextField prixField;
    @FXML private ComboBox<Categorie> categorieCombo;
    @FXML private TextField searchField;

    // Colonnes du tableau
    @FXML private TableView<Produit>       produitTable;
    @FXML private TableColumn<Produit, Integer>    colId;
    @FXML private TableColumn<Produit, String>     colLibelle;
    @FXML private TableColumn<Produit, Integer>    colQte;
    @FXML private TableColumn<Produit, Double>     colPrix;
    @FXML private TableColumn<Produit, String>     colCategorie;

    private ProduitDAO produitDAO = new ProduitDAO();
    private CategorieDAO catDAO   = new CategorieDAO();

    // initialize() est appele automatiquement apres le chargement du FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurerColonnes();
        chargerCategories();
        chargerProduits();
        // Clic sur une ligne du tableau -> remplit le formulaire
        produitTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, selected) -> {
                    if (selected != null) remplirFormulaire(selected);
                });
    }

    private void configurerColonnes() {
        // PropertyValueFactory prend le nom du getter (sans 'get' et en minuscule)
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        // Pour la colonne categorie, on fait une cellule personnalisee
        colCategorie.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCategorie() != null ?
                                data.getValue().getCategorie().getLibelle() : "-"));
    }

    private void chargerCategories() {
        List<Categorie> cats = catDAO.findAll();
        categorieCombo.setItems(FXCollections.observableArrayList(cats));
    }

    private void chargerProduits() {
        List<Produit> produits = produitDAO.findAll();
        produitTable.setItems(FXCollections.observableArrayList(produits));
    }

    private void remplirFormulaire(Produit p) {
        libelleField.setText(p.getLibelle());
        quantiteField.setText(String.valueOf(p.getQuantite()));
        prixField.setText(String.valueOf(p.getPrixUnitaire()));
        categorieCombo.setValue(p.getCategorie());
    }

    @FXML
    public void ajouter() {
        if (!validerFormulaire()) return;
        Produit p = new Produit(
                libelleField.getText(),
                Integer.parseInt(quantiteField.getText()),
                Double.parseDouble(prixField.getText()),
                categorieCombo.getValue());
        produitDAO.save(p);
        showAlert("Produit ajoute avec succes !");
        viderFormulaire();
        chargerProduits();
    }

    @FXML
    public void modifier() {
        Produit selected = produitTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selectionnez un produit a modifier."); return;
        }
        selected.setLibelle(libelleField.getText());
        selected.setQuantite(Integer.parseInt(quantiteField.getText()));
        selected.setPrixUnitaire(Double.parseDouble(prixField.getText()));
        selected.setCategorie(categorieCombo.getValue());
        produitDAO.update(selected);
        showAlert("Produit modifie avec succes !");
        chargerProduits();
    }

    @FXML
    public void supprimer() {
        Produit selected = produitTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selectionnez un produit a supprimer."); return;
        }
        // Confirmation avant suppression
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer \"" + selected.getLibelle() + "\" ?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                produitDAO.delete(selected.getId());
                chargerProduits();
                viderFormulaire();
            }
        });
    }

    @FXML
    public void actualiser() { chargerProduits(); viderFormulaire(); }

    // Declenche a chaque frappe dans la barre de recherche
    @FXML
    public void rechercher() {
        String terme = searchField.getText().trim();
        List<Produit> resultats = terme.isEmpty() ?
                produitDAO.findAll() : produitDAO.findByLibelle(terme);
        produitTable.setItems(FXCollections.observableArrayList(resultats));
    }

    private boolean validerFormulaire() {
        if (libelleField.getText().trim().isEmpty()) {
            showAlert("Le libelle est obligatoire."); return false;
        }
        try { Integer.parseInt(quantiteField.getText()); }
        catch (NumberFormatException e) {
            showAlert("Quantite invalide."); return false;
        }
        try { Double.parseDouble(prixField.getText()); }
        catch (NumberFormatException e) {
            showAlert("Prix invalide."); return false;
        }
        if (categorieCombo.getValue() == null) {
            showAlert("Selectionnez une categorie."); return false;
        }
        return true;
    }

    private void viderFormulaire() {
        libelleField.clear(); quantiteField.clear();
        prixField.clear(); categorieCombo.setValue(null);
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}

