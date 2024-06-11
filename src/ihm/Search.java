package src.ihm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import src.VoyageurCorrespondance;
import src.exception.CheminInexistantException;

import java.util.List;

import fr.ulille.but.sae_s2_2024.*;
import src.ToolsCorrespondance;
import src.TypeCout;

public class Search {
    @FXML
    TextField vDepart;
    @FXML
    TextField vArrivee;
    
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
