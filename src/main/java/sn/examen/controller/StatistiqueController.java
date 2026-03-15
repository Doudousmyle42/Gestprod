package sn.examen.controller;

import sn.examen.dao.CategorieDAO;
import sn.examen.dao.ProduitDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class StatistiqueController implements Initializable {

    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;

    private CategorieDAO catDAO   = new CategorieDAO();
    private ProduitDAO   prodDAO  = new ProduitDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chargerPieChart();
        chargerBarChart();
    }

    // GRAPHIQUE CERCLE : nombre de produits par categorie
    private void chargerPieChart() {
        Map<String, Long> data = catDAO.countProduitsParCategorie();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            pieData.add(new PieChart.Data(
                    entry.getKey() + " (" + entry.getValue() + ")",
                    entry.getValue()));
        }

        pieChart.setData(pieData);
        pieChart.setTitle("Repartition par Categorie");
        pieChart.setLegendVisible(true);
    }

    // GRAPHIQUE BARRES : produits ajoutes par mois en 2024
    private void chargerBarChart() {
        Map<Integer, Long> data = prodDAO.countByMonthIn2024();

        // Noms des mois
        String[] mois = {"Jan","Fev","Mar","Avr","Mai","Jun",
                "Jul","Aou","Sep","Oct","Nov","Dec"};

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Produits ajoutes en 2024");

        for (int moisNum = 1; moisNum <= 12; moisNum++) {
            long count = data.getOrDefault(moisNum, 0L);
            series.getData().add(
                    new XYChart.Data<>(mois[moisNum - 1], count));
        }

        barChart.getData().clear();
        barChart.getData().add(series);
        barChart.setTitle("Ajouts par mois");
    }
}

