package src.ihm;

import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
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
    ChoiceBox<String> transp;

    

    @FXML
    AnchorPane multiCritPane;

    @FXML
    ToggleButton multiCritBtn;

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
        if (transp.getValue() == null) {
            return;
        }
        if (transp.getValue().equals("Tous")) {
            Search.currentInstance.setPreferredTransport(null);;
        } else {
            Search.currentInstance.setPreferredTransport(ModaliteTransport.valueOf(transp.getValue()));
        }

        Search.currentInstance.search();
        return;
    }

    

    @FXML
    public void initialize() {

        critList = loadCritere();
        transpList = loadTransp();
        transpList.add("Tous");

        critFirst.setValue(Search.podium.getFirst().toString());
        critSecond.setValue(Search.podium.getSecond().toString());
        critThird.setValue(Search.podium.getThird().toString());

        if (Search.currentInstance.getPreferredTransport() == null) {
            transp.setValue("Tous");
        }else {
            transp.setValue(Search.currentInstance.getPreferredTransport().toString());
        }


        critFirst.getItems().addAll(critList);
        critSecond.getItems().addAll(critList);
        critThird.getItems().addAll(critList);

        transp.getItems().addAll(transpList);
        
    }

    public void toggleMulticrit() {
        if (multiCritBtn.isSelected()) {
            multiCritPane.setVisible(true);
            
        } else {
            multiCritPane.setVisible(false);
            critSecond.setValue(critFirst.getValue());
            critThird.setValue(critFirst.getValue());
        }
    }

}