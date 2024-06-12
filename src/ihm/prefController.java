package src.ihm;

import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import src.IhmInterface;
import src.TypeCout;

public class prefController {

    @FXML
    ChoiceBox<String> critFirst;

    @FXML
    ChoiceBox<String> critSecond;

    @FXML
    ChoiceBox<String> critThird;

    @FXML
    ChoiceBox<String> transpFirst;

    @FXML
    ChoiceBox<String> transpSecond;

    @FXML
    ChoiceBox<String> transpThird;

    @FXML
    Button prefSaveBtn;

    List<String> critList ;
    List<String> transpList ;
    String tmp;

    private static List<String> loadCritere() {
        List<String> critList = new ArrayList<>();
        for (TypeCout crit : TypeCout.values()) {
            critList.add(crit.toString());
        }
        return critList;
    }

    private static List<String> loadTransp() {
        List<String> transpList = new ArrayList<>();
        for (ModaliteTransport transp : ModaliteTransport.values()) {
            transpList.add(transp.toString());
        }
        return transpList;
    }


    public void changeCritPref() {
        if (critFirst.getValue() == null || critSecond.getValue() == null || critThird.getValue() == null) {
            return;
        }
        Search.podium.setFirst(TypeCout.valueOf(critFirst.getValue()));
        Search.podium.setSecond(TypeCout.valueOf(critSecond.getValue()));
        Search.podium.setThird(TypeCout.valueOf(critThird.getValue()));
        Search.currentInstance.search();
        return;
    }

    public void changeTranspPref() {
        return;
    }

    

    @FXML
    public void initialize() {

        critList = loadCritere();
        transpList = loadTransp();

        critFirst.setValue(Search.podium.getFirst().toString());
        critSecond.setValue(Search.podium.getSecond().toString());
        critThird.setValue(Search.podium.getThird().toString());

        transpFirst.setValue(transpList.get(0));
        transpSecond.setValue(transpList.get(1));
        transpThird.setValue(transpList.get(2));


        critFirst.getItems().addAll(critList);
        critSecond.getItems().addAll(critList);
        critThird.getItems().addAll(critList);

        transpFirst.getItems().addAll(transpList);
        transpSecond.getItems().addAll(transpList);
        transpThird.getItems().addAll(transpList);
    }

}