package src.ihm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import src.VoyageurCorrespondance;
import src.exception.CheminInexistantException;

import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import fr.ulille.but.sae_s2_2024.*;
import src.IhmInterface;
import src.IhmInterfaceImpl;
import src.Podium;
import src.ToolsCorrespondance;
import src.TypeCout;


public class Search implements Initializable {
    @FXML
    TextField vDepart;

    @FXML
    TextField vArrivee;

    @FXML
    Pane recomendedPath;

    @FXML
    Pane resultContainer;

    @FXML
    Label noResultLabel;

    @FXML 
    Label cheminRecoLabel;

    @FXML
    Label PollutionReco;

    @FXML
    Label PrixReco;

    @FXML
    Label TempsReco;

    private AutoCompletionBinding<String> autoCompletionBinding;

    private IhmInterface ihmInterface;

    private Podium<TypeCout> podium;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        ihmInterface = new IhmInterfaceImpl("test");

        TextFields.bindAutoCompletion(vDepart, ihmInterface.getStartCity());
        TextFields.bindAutoCompletion(vArrivee, ihmInterface.getDestinationCity());
        podium = new Podium<>();
        podium.setFirst(TypeCout.CO2);
        podium.setSecond(TypeCout.PRIX);
        podium.setThird(TypeCout.TEMPS);

        recomendedPath.setVisible(false);
        resultContainer.setVisible(false);

        // vDepart.setOnKeyPressed((KeyEvent e) -> {
        //     switch (e.getCode()) {
        //         case ENTER:
        //             learnWord(vDepart.getText());
        //             break;
        //         default:
        //             break;
        //     }
        // });

        // vArrivee.setOnKeyPressed((KeyEvent e) -> {
        //     switch (e.getCode()) {
        //         case ENTER:
        //             learnWord(vArrivee.getText());
        //             break;
        //         default:
        //             break;
        //     }
        // });
    }

    // private void learnWord(String word) {
    //     villesSet.add(word);
    //     if (autoCompletionBinding != null) {
    //         autoCompletionBinding.dispose();
    //     }
    //     autoCompletionBinding = TextFields.bindAutoCompletion(vDepart, villesSet);
    // }

    private String convertToTempsTarjet(Double temps) {
        return LocalTime.MIN.plus(Duration.ofMinutes(temps.longValue())).toString();
    }

    private void buildRecommendedPath(Map<Double, Chemin> bestResults) {
        List<Double> scores = new ArrayList<>(bestResults.keySet());
        Collections.sort(scores);
        cheminRecoLabel.setText(ToolsCorrespondance.cheminWithCorreBis(bestResults.get(scores.get(0)), podium.getSecond()));
        Map<TypeCout, Double> poids = ihmInterface.getCheminPoids(bestResults.get(scores.get(0)));
        PollutionReco.setText(poids.get(TypeCout.CO2)+ "Kg de CO2");
        PrixReco.setText(poids.get(TypeCout.PRIX) + "€");
        TempsReco.setText(convertToTempsTarjet(poids.get(TypeCout.TEMPS)));
    }

    public void search(ActionEvent event) {
        try {
            Map<Double, Chemin> bestResults = ihmInterface.getBestResults(podium, vDepart.getText(), vArrivee.getText(), null);
            if (bestResults == null) {
                noResultLabel.setText("Aucun chemin trouvé pour les critères demandés");
                return;
            }else {
                noResultLabel.setText("");
                recomendedPath.setVisible(true);
                resultContainer.setVisible(true);
                buildRecommendedPath(bestResults);
            }
        } catch (CheminInexistantException e) {
            noResultLabel.setText("Aucun chemin trouvé pour les critères demandés");
            return;
        }
        
    }
}
