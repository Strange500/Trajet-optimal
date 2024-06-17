package src.ihm;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.io.IOException;

public class Accueil extends Application {
  @FXML
  TextField vDepart;

  @FXML
  TextField vArrivee;

  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL fxmlFileUrl = getClass().getResource("accueil.fxml");

    if (fxmlFileUrl == null) {
      System.out.println("Impossible de charger le fichier fxml");
      System.exit(-1);
    }

    loader.setLocation(fxmlFileUrl);

    Parent root = loader.load();

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("Accueil demo");
    stage.show();
  }

  public static void main(String[] args) {
    Application.launch(args);
  }
}
