package src.ihm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import src.exception.CheminInexistantException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.tools.Tool;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import fr.ulille.but.sae_s2_2024.*;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import src.HistoriqueItem;
import src.IhmInterface;
import src.IhmInterfaceImpl;
import src.PlateformeCorrespondance;
import src.Podium;
import src.Tools;
import src.ToolsCorrespondance;
import src.TypeCout;

public class Search  {
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

    @FXML
    Label t1CO2;

    @FXML
    Label t1PRIX;

    @FXML
    Label t1TEMPS;

    @FXML
    Label t2CO2;

    @FXML
    Label t2PRIX;

    @FXML
    Label t2TEMPS;

    @FXML
    Label t3CO2;

    @FXML
    Label t3PRIX;

    @FXML
    Label t3TEMPS;

    @FXML
    Label t4CO2;

    @FXML
    Label t4PRIX;

    @FXML
    Label t4TEMPS;

    @FXML
    Button openFilterBtn;

    @FXML
    Button addBtn1;

    @FXML
    Button addBtn2;

    @FXML
    Button addBtn3;

    @FXML
    Button addBtn4;

    @FXML
    Button addBtn5;

    @FXML
    MenuItem quit;

    @FXML
    AnchorPane rPane1;

    @FXML
    AnchorPane rPane2;

    @FXML
    AnchorPane rPane3;

    @FXML
    AnchorPane rPane4;

    // @FXML
    // Menu historiqueBtn;

    static IhmInterface ihmInterface;

    static Podium<TypeCout> podium;

    static Search currentInstance;

    private Map<Double, Chemin> bestResults;
    private List<Double> scores;
    private List<HistoriqueItem> historiqueItems = new ArrayList<>();
    private SuggestionProvider<String> suggestionProviderDepart;
    private SuggestionProvider<String> suggestionProviderArrivee;

    @FXML
    public void initialize() {
        currentInstance = this;
        ihmInterface = new IhmInterfaceImpl("test");
        suggestionProviderDepart = SuggestionProvider.create(ihmInterface.getStartCity());
        suggestionProviderArrivee = SuggestionProvider.create(ihmInterface.getDestinationCity());
        new AutoCompletionTextFieldBinding<>(vDepart, suggestionProviderDepart);
        new AutoCompletionTextFieldBinding<>(vArrivee, suggestionProviderArrivee);
        // TextFields.bindAutoCompletion(vDepart, ihmInterface.getStartCity());
        // TextFields.bindAutoCompletion(vArrivee, ihmInterface.getDestinationCity());
        podium = new Podium<>();
        podium.setFirst(TypeCout.CO2);
        podium.setSecond(TypeCout.PRIX);
        podium.setThird(TypeCout.TEMPS);

        recomendedPath.setVisible(false);
        resultContainer.setVisible(false);
        noResultLabel.setText("Aucun chemin trouvé pour les critères demandés");

        if (!HistoriqueItem.saveExists()) {
            HistoriqueItem.createSave();
        }
        try {
            historiqueItems = HistoriqueItem.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void reload() {
        suggestionProviderDepart.clearSuggestions();
        suggestionProviderArrivee.clearSuggestions();

        suggestionProviderDepart.addPossibleSuggestions(ihmInterface.getStartCity());
        suggestionProviderArrivee.addPossibleSuggestions(ihmInterface.getDestinationCity());
        // TextFields.bindAutoCompletion(vDepart, ihmInterface.getStartCity());
        // TextFields.bindAutoCompletion(vArrivee, ihmInterface.getDestinationCity());

        recomendedPath.setVisible(false);
        resultContainer.setVisible(false);
        

        if (!HistoriqueItem.saveExists()) {
            HistoriqueItem.createSave();
        }
        try {
            historiqueItems = HistoriqueItem.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public List<HistoriqueItem> getHistoriqueItems() {
        return historiqueItems;
    }

    public void setPreferredTransport(ModaliteTransport preferredTransport) {
        Search.ihmInterface.setPreferredTransport(preferredTransport);
    }

    public ModaliteTransport getPreferredTransport() {
        return Search.ihmInterface.getPreferredTransport();
    }

    private String convertToTempsTarjet(Double temps) {
        return LocalTime.MIN.plus(Duration.ofMinutes(temps.longValue())).toString().replace(":", "h");
    }

    private void buildRecommendedPath(Map<Double, Chemin> bestResults) {
        List<Double> scores = new ArrayList<>(bestResults.keySet());
        Collections.sort(scores);
        cheminRecoLabel
                .setText(ToolsCorrespondance.cheminWithCorreArrow(bestResults.get(scores.get(0)), podium.getSecond()));
        Map<TypeCout, Double> poids = ihmInterface.getCheminPoids(bestResults.get(scores.get(0)));
        PollutionReco.setText(formatDouble(poids.get(TypeCout.CO2)) + "Kg de CO2");
        PrixReco.setText(formatDouble(poids.get(TypeCout.PRIX)) + "€");
        TempsReco.setText(convertToTempsTarjet(poids.get(TypeCout.TEMPS)));
    }

    String formatDouble(Double d) {
        return String.format("%.2f", d);
    }

    private void buildOtherPaths(Map<Double, Chemin> bestResults) {
        List<Double> scores = new ArrayList<>(bestResults.keySet());
        Collections.sort(scores);
        int cpt = 1;
        List<Integer> notDone = new ArrayList<>();
        notDone.addAll(List.of(1, 2, 3, 4));
        while (cpt < 5 && cpt < scores.size()) {
            Chemin chemin = bestResults.get(scores.get(cpt));
            Map<TypeCout, Double> poids = ihmInterface.getCheminPoids(chemin);
            switch (cpt) {
                case 1:
                    t1CO2.setText(formatDouble(poids.get(TypeCout.CO2)) + "Kg CO2");
                    t1PRIX.setText(formatDouble(poids.get(TypeCout.PRIX)) + "€");
                    t1TEMPS.setText(convertToTempsTarjet(poids.get(TypeCout.TEMPS)));
                    rPane1.setVisible(true);
                    notDone.remove(Integer.valueOf(1));
                    break;
                case 2:
                    t2CO2.setText(formatDouble(poids.get(TypeCout.CO2)) + "Kg CO2");
                    t2PRIX.setText(formatDouble(poids.get(TypeCout.PRIX)) + "€");
                    t2TEMPS.setText(convertToTempsTarjet(poids.get(TypeCout.TEMPS)));
                    rPane2.setVisible(true);
                    notDone.remove(Integer.valueOf(2));
                    break;
                case 3:
                    t3CO2.setText(formatDouble(poids.get(TypeCout.CO2)) + "Kg CO2");
                    t3PRIX.setText(formatDouble(poids.get(TypeCout.PRIX)) + "€");
                    t3TEMPS.setText(convertToTempsTarjet(poids.get(TypeCout.TEMPS)));
                    rPane3.setVisible(true);
                    notDone.remove(Integer.valueOf(3));
                    break;
                case 4:
                    t4CO2.setText(formatDouble(poids.get(TypeCout.CO2)) + "Kg CO2");
                    t4PRIX.setText(formatDouble(poids.get(TypeCout.PRIX)) + "€");
                    t4TEMPS.setText(convertToTempsTarjet(poids.get(TypeCout.TEMPS)));
                    rPane4.setVisible(true);
                    notDone.remove(Integer.valueOf(4));
                    break;
            }
            cpt++;
        }
        for (Integer i : notDone) {
            switch (i) {
                case 1:
                    rPane1.setVisible(false);
                    break;
                case 2:
                    rPane2.setVisible(false);
                    break;
                case 3:
                    rPane3.setVisible(false);
                    break;
                case 4:
                    rPane4.setVisible(false);
                    break;
            }
        }
    }

    public void search() {
        try {
            Map<Double, Chemin> bestResults = ihmInterface.getBestResults(podium, vDepart.getText(),
                    vArrivee.getText());
            if (bestResults == null) {
                noResultLabel.setText("Aucun chemin trouvé pour les critères demandés");
                recomendedPath.setVisible(false);
                resultContainer.setVisible(false);
                return;
            } else {
                this.scores = new ArrayList<>(bestResults.keySet());
                this.bestResults = bestResults;

                noResultLabel.setText("");
                recomendedPath.setVisible(true);
                resultContainer.setVisible(true);
                buildRecommendedPath(bestResults);
                buildOtherPaths(bestResults);
            }
        } catch (CheminInexistantException e) {
            noResultLabel.setText("Aucun chemin trouvé pour les critères demandés");
            recomendedPath.setVisible(false);
            resultContainer.setVisible(false);
            return;
        }

    }

    public void openPref() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            URL fxmlFileUrl = getClass().getResource("pref.fxml");
            if (fxmlFileUrl == null) {
                System.out.println("Impossible de charger le fichier fxml");
                System.exit(-1);
            }
            loader.setLocation(fxmlFileUrl);
            root = loader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Préferences");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(vDepart.getScene().getWindow());
            stage.setScene(new Scene(root, 645, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openHistorique() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            URL fxmlFileUrl = getClass().getResource("historique.fxml");
            if (fxmlFileUrl == null) {
                System.out.println("Impossible de charger le fichier fxml");
                System.exit(-1);
            }
            loader.setLocation(fxmlFileUrl);
            root = loader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Historique");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(vDepart.getScene().getWindow());
            stage.setScene(new Scene(root, 1315, 715));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetHistorique() {
        Alert comfirm = new Alert(Alert.AlertType.CONFIRMATION);
        comfirm.setContentText("Comfirmer la réinitialisation");
        Optional<ButtonType> result = comfirm.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        this.historiqueItems.clear();
        if (!HistoriqueItem.saveExists()) {
            HistoriqueItem.createSave();
            return;
        }
        try {
            HistoriqueItem.save(new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openFilter() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            URL fxmlFileUrl = getClass().getResource("filter.fxml");
            if (fxmlFileUrl == null) {
                System.out.println("Impossible de charger le fichier fxml");
                System.exit(-1);
            }
            loader.setLocation(fxmlFileUrl);
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Filtres");
            stage.setScene(new Scene(root, 645, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getSeuilPrix() {
        return ihmInterface.getSeuilPrix();
    }

    public double getSeuilCO2() {
        return ihmInterface.getSeuilCO2();
    }

    public double getSeuilTemps() {
        return ihmInterface.getSeuilTemps();
    }

    public void setSeuilPrix(double seuilPrix) {
        ihmInterface.setSeuilPrix(seuilPrix);
    }

    public void setSeuilCO2(double seuilCO2) {
        ihmInterface.setSeuilCO2(seuilCO2);
    }

    public void setSeuilTemps(double seuilTemps) {
        ihmInterface.setSeuilTemps(seuilTemps);
    }

    private HistoriqueItem createHistoriqueItem(Chemin chemin) {
        Map<TypeCout, Double> poids = ihmInterface.getCheminPoids(chemin);
        return new HistoriqueItem(chemin, poids.get(TypeCout.CO2), poids.get(TypeCout.PRIX), poids.get(TypeCout.TEMPS));
    }

    private double parseDouble(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter(",");
        double res = sc.nextDouble();
        String tmp = sc.next();
        res += Double.parseDouble(tmp) / 100;
        return res;
    }

    public void showPopupCheminAdded() {
        Popup popup = new Popup();
        Label label = new Label("Chemin ajouté à l'historique");
        label.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-font-color: green; -fx-font-size: 20px;");
        popup.getContent().add(label);
        popup.setAutoHide(true);
        popup.setX(vDepart.getScene().getWindow().getX() + vDepart.getScene().getWindow().getWidth() -287);
        popup.setY(vDepart.getScene().getWindow().getY() + 60);
        popup.show(vDepart.getScene().getWindow());
    }

    public void addToHistorique(ActionEvent e) {
        Button addBtn = (Button) e.getSource();

        if (addBtn == addBtn1) {
            if (bestResults.size() < 1) {
                return;
            }
            historiqueItems.add(
                    new HistoriqueItem(cheminRecoLabel.getText(), parseDouble(PrixReco.getText().replace("€", "")),
                            parseDouble(PollutionReco.getText().split("Kg")[0]),
                            (double) prefController.convertToMinutes(TempsReco.getText().replace("h", ":"))));
        } else if (addBtn == addBtn2) {
            if (bestResults.size() < 2) {
                return;
            }
            historiqueItems.add(createHistoriqueItem(bestResults.get(scores.get(1))));
        } else if (addBtn == addBtn3) {
            if (bestResults.size() < 3) {
                return;
            }
            historiqueItems.add(createHistoriqueItem(bestResults.get(scores.get(2))));
        } else if (addBtn == addBtn4) {
            if (bestResults.size() < 4) {
                return;
            }
            historiqueItems.add(createHistoriqueItem(bestResults.get(scores.get(3))));
        } else if (addBtn == addBtn5) {
            if (bestResults.size() < 5) {
                return;
            }
            historiqueItems.add(createHistoriqueItem(bestResults.get(scores.get(4))));
        }
        try {
            HistoriqueItem.save(historiqueItems);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        showPopupCheminAdded();
    }
    private ArrayList<String> askCorrespondance() {
        Alert comfirm = new Alert(Alert.AlertType.CONFIRMATION);
        comfirm.setContentText("Voulez-vous charger un fichier de correspondance ?");
        Optional<ButtonType> result = comfirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ouvrir un fichier CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File file = fileChooser.showOpenDialog(vDepart.getScene().getWindow());
            if (file == null) {
                return new ArrayList<>();
            }

            ArrayList<String> data = ToolsCorrespondance.getCSV(file.getAbsolutePath());

            if (ToolsCorrespondance.donneesValides(data)) {
                return data;
            } else {
                System.out.println("Données invalides");
            }
        }
        return new ArrayList<>();
    }

    public void openCSV() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ouvrir un fichier CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File file = fileChooser.showOpenDialog(vDepart.getScene().getWindow());
            if (file == null) {
                return;
            }

            ArrayList<String> data = ToolsCorrespondance.getCSV(file.getAbsolutePath());

            if (ToolsCorrespondance.donneesValides(data)) {
                ArrayList<String> correspondance = askCorrespondance();
                // if (!ToolsCorrespondance.donneesValides(correspondance)) {
                //     return;
                // }
                ihmInterface = new IhmInterfaceImpl("test", data, correspondance);
                reload();
            } else {
                System.out.println("Données invalides");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        System.exit(0);
    }

    public static void main(String[] args) throws Exception{
        // create multiple fake historiqueItems

        if (!HistoriqueItem.saveExists()) {
            HistoriqueItem.createSave();
        }
        List<HistoriqueItem> historiqueItems = HistoriqueItem.load();
        for (int i = 0; i < 10; i++) {
            historiqueItems.add(new HistoriqueItem("Chemin " + i, i * 10, i * 20, i * 30));
        }
        // mets differente date pour simuler en utilisant des localdate
        for (int i = 0; i < 10; i++) {
            // format like 2021-01-01
            historiqueItems.get(i).setDate(LocalDate.parse("2021-01-" + String.format("%02d", i + 1) , DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }


        try {
            HistoriqueItem.save(historiqueItems);
        } catch (IOException e) {
            e.printStackTrace();
    }
}
}
