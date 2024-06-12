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

    public void search(ActionEvent event) {
        
        System.out.println(ihmInterface.getBestResults(podium, vDepart.getText(), vArrivee.getText(), null));
    }
}
