package sn.examen.controller;

import sn.examen.dao.CategorieDAO;
import sn.examen.model.Categorie;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class CategorieController implements Initializable {

    @FXML private TextField libelleField;
    @FXML private TableView<Categorie>          categorieTable;
    @FXML private TableColumn<Categorie, Integer> colId;
    @FXML private TableColumn<Categorie, String>  colLibelle;

    private CategorieDAO dao = new CategorieDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        charger();
        // Clic sur une ligne -> remplit le champ
        categorieTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, sel) -> {
                    if (sel != null) libelleField.setText(sel.getLibelle());
                });
    }

    private void charger() {
        categorieTable.setItems(
                FXCollections.observableArrayList(dao.findAll()));
    }

    @FXML
    public void ajouter() {
        String lib = libelleField.getText().trim();
        if (lib.isEmpty()) { showAlert("Libelle obligatoire."); return; }
        dao.save(new Categorie(lib));
        libelleField.clear();
        charger();
    }

    @FXML
    public void modifier() {
        Categorie sel = categorieTable.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Selectionnez une categorie."); return; }
        sel.setLibelle(libelleField.getText().trim());
        dao.update(sel);
        charger();
    }

    @FXML
    public void supprimer() {
        Categorie sel = categorieTable.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Selectionnez une categorie."); return; }
        dao.delete(sel.getId());
        libelleField.clear();
        charger();
    }

    @FXML
    public void actualiser() { charger(); libelleField.clear(); }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}