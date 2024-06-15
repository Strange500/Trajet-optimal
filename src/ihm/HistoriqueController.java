package src.ihm;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import src.HistoriqueItem;

public class HistoriqueController {

    @FXML
    ScrollPane scrollContainer;

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

    @FXML
    public void initialize() {
        scrollContainer.setFitToWidth(false);
        scrollContainer.setFitToHeight(true);
        scrollContainer.setContent(vbox);

        // fill the scroll pane for test
        for (HistoriqueItem h : Search.currentInstance.getHistoriqueItems()) {
            vbox.getChildren().add(buildHistoricItem(h));
        }

    }

}