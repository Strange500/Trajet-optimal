package src.ihm;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import src.HistoriqueItem;

public class HistoriqueController {

    @FXML
    ScrollPane scrollContainer;

    @FXML
    LineChart<String, Number> chartCo2;

    @FXML
    LineChart<String, Number> chartPrix;

    @FXML
    LineChart<String, Number> chartTemps;

    @FXML
    Label labelRemainingCo2;

    


    VBox vbox = new VBox();

    private HBox buildHistoricItem(HistoriqueItem h) {
        HBox pane = new HBox();
        pane.getStyleClass().add("historic-item");
        pane.setPrefHeight(50);
        pane.setPrefWidth(300);
        Text date = new Text(h.getDate().toString() + "  |  ");
        date.styleProperty().set("-fx-font-weight: bold; -fx-font-size: 13px;");
        pane.getChildren().add(date);
        Text chemin = new Text(h.getChe() + '\t');
        chemin.styleProperty().set("-fx-font-weight: bold; -fx-font-size: 13px;");
        pane.getChildren().add(chemin);
        Text prix = new Text(h.getPrix() + "€ ");
        prix.styleProperty().set("-fx-font-size: 13px;");
        pane.getChildren().add(prix);
        Text pollution = new Text(h.getPollution() + "kgCO2 ");
        pollution.styleProperty().set("-fx-font-size: 13px;");
        pane.getChildren().add(pollution);
        Text temps = new Text(h.getTemps() + "min");
        temps.styleProperty().set("-fx-font-size: 13px;");
        pane.getChildren().add(temps);
        return pane;
    }

    public Series<String, Number> getCo2Series() {
        Series<String, Number> series = new Series<>();
        series.setName("CO2");
        for (HistoriqueItem h : Search.currentInstance.getHistoriqueItems()) {
            series.getData().add(new XYChart.Data<>(h.getDate().toString(), h.getPollution()));
        }
        return series;
    }

    public Series<String, Number> getPrixSeries() {
        Series<String, Number> series = new Series<>();
        series.setName("Prix");
        for (HistoriqueItem h : Search.currentInstance.getHistoriqueItems()) {
            series.getData().add(new XYChart.Data<>(h.getDate().toString(), h.getPrix()));
        }
        return series;
    }

    public Series<String, Number> getTempsSeries() {
        Series<String, Number> series = new Series<>();
        series.setName("Temps");
        for (HistoriqueItem h : Search.currentInstance.getHistoriqueItems()) {
            series.getData().add(new XYChart.Data<>(h.getDate().toString(), h.getTemps()));
        }
        return series;
    }

    public int getRemainingCo2() {
        int totalCo2 = 0;
        for (HistoriqueItem h : Search.currentInstance.getHistoriqueItems()) {
            totalCo2 += h.getPollution();
        }
        return 2000 - totalCo2;
    }

    @FXML
    public void initialize() {
        scrollContainer.setFitToWidth(false);
        scrollContainer.setFitToHeight(true);
        scrollContainer.setContent(vbox);

        // fill the scroll pane for test
        for (HistoriqueItem h : Search.currentInstance.getHistoriqueItems()) {
            vbox.getChildren().add(buildHistoricItem(h));
        }
        chartCo2.getData().add(getCo2Series());
        chartPrix.getData().add(getPrixSeries());
        chartTemps.getData().add(getTempsSeries());
        // on prend en compte le "pass carbonne" fixée a 2tonne de CO2 par an
        labelRemainingCo2.setText("Il vous reste " + getRemainingCo2() + "kg de CO2 à émettre pour atteindre votre seuil de 2 tonnes par an");


    }

    

}