package src.ihm;

import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

    @FXML
    TextField prixMax;

    @FXML
    TextField CO2Max;

    @FXML
    TextField tempsMax;

    @FXML
    Label seuilError;

    List<String> critList;

    List<String> transpList;

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
            Search.currentInstance.setPreferredTransport(null);
            ;
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
        } else {
            transp.setValue(Search.currentInstance.getPreferredTransport().toString());
        }

        critFirst.getItems().addAll(critList);
        critSecond.getItems().addAll(critList);
        critThird.getItems().addAll(critList);

        transp.getItems().addAll(transpList);
        seuilError.setVisible(false);

        if (critFirst.getValue().equals(critSecond.getValue()) && critFirst.getValue().equals(critThird.getValue())) {
            multiCritBtn.setSelected(false);
            multiCritPane.setVisible(false);
        } else {
            multiCritBtn.setSelected(true);
            multiCritPane.setVisible(true);
        }

        this.prixMax.setText(String.valueOf(Search.currentInstance.getSeuilPrix()));
        this.CO2Max.setText(String.valueOf(Search.currentInstance.getSeuilCO2()));
        this.tempsMax.setText(formatToHour(Search.currentInstance.getSeuilTemps()));

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

    public void quit() {
        Stage stage = (Stage) prefSaveBtn.getScene().getWindow();
        stage.close();
    }

    public static int convertToMinutes(String time) {
        int tps_heure = Integer.parseInt(time.split(":")[0]);
        int tps_min = Integer.parseInt(time.split(":")[1]);
        return tps_heure * 60 + tps_min;
    }

    public static String formatToHour(double time) {
        int tps_heure = (int) (time / 60);
        int tps_min = (int) (time % 60);
        return String.format("%02d:%02d", tps_heure, tps_min);
    }

    public void saveSeuil() {
        try {
            Search.currentInstance.setSeuilPrix(Double.parseDouble(prixMax.getText()));
            Search.currentInstance.setSeuilCO2(Double.parseDouble(CO2Max.getText()));
            Search.currentInstance.setSeuilTemps(convertToMinutes(tempsMax.getText()));
            seuilError.setVisible(false);
        } catch (NumberFormatException e) {
            seuilError.setVisible(true);
        }
        Search.currentInstance.search();
    }

}