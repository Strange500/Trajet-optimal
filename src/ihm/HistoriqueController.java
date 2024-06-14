package src.ihm;

import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import src.HistoriqueItem;
import src.IhmInterface;
import src.ToolsCorrespondance;
import src.TypeCout;

public class HistoriqueController {

    @FXML
    ScrollPane scrollContainer;

    VBox vbox = new VBox();


    private HBox buildHistoricItem(HistoriqueItem h) {
        HBox pane = new HBox();
        pane.getStyleClass().add("historic-item");
        pane.setPrefHeight(50);
        pane.setPrefWidth(300);
        Text date = new Text(h.getDate().toString()+"\t");
        date.styleProperty().set("-fx-font-weight: bold; -fx-font-size: 13px;");
        pane.getChildren().add(date);
        Text chemin = new Text(h.getChe()+'\t');
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