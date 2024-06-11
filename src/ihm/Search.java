package src.ihm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import src.VoyageurCorrespondance;
import src.exception.CheminInexistantException;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import fr.ulille.but.sae_s2_2024.*;
import src.ToolsCorrespondance;
import src.TypeCout;

public class Search implements Initializable {
    @FXML
    TextField vDepart;

    @FXML
    TextField vArrivee;

    Set<String> villesSet = new HashSet<String>();
    String[] villes = {"Lille", "Paris", "Dunkerque"};
    private AutoCompletionBinding<String> autoCompletionBinding;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Collections.addAll(villesSet, villes);
        autoCompletionBinding = TextFields.bindAutoCompletion(vDepart, villesSet);
        autoCompletionBinding = TextFields.bindAutoCompletion(vArrivee, villesSet);

        vDepart.setOnKeyPressed((KeyEvent e) -> {
            switch (e.getCode()) {
                case ENTER:
                    learnWord(vDepart.getText());
                    break;
                default:
                    break;
            }
        });

        vArrivee.setOnKeyPressed((KeyEvent e) -> {
            switch (e.getCode()) {
                case ENTER:
                    learnWord(vArrivee.getText());
                    break;
                default:
                    break;
            }
        });
    }

    private void learnWord(String word) {
        villesSet.add(word);
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }
        autoCompletionBinding = TextFields.bindAutoCompletion(vDepart, villesSet);
    }

    public void search(ActionEvent event) {
        VoyageurCorrespondance voyageur = new VoyageurCorrespondance("fryson", "adrien", TypeCout.PRIX, ModaliteTransport.TRAIN, 100, 100, 100, vDepart.getText(), vArrivee.getText(), 1, "csv/data2.csv", "csv/correspondance.csv");
        List<Chemin> chemins = null;
        try {
            chemins = voyageur.computeBestPathTrigger();
        } catch (CheminInexistantException e) {
            chemins = null;
        } finally {
            if (chemins != null) {
                System.out.println("Les trajets recommandés de " + vDepart.getText() + " à " + vArrivee.getText() + " sont :");
                for (int i = 0; i < chemins.size(); i++) {
                    System.out.println(i + 1 + ") " + ToolsCorrespondance.cheminWithCorreBis(chemins.get(i), TypeCout.PRIX));
                }
            } else {
                System.out.println("Aucun chemin trouvé pour les critères demandés");
            }
        }
    }
}
