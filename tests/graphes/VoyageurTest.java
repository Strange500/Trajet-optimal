package tests.graphes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.exception.CheminInexistantException;
import src.PlateformeCorrespondance;
import src.ToolsCorrespondance;
import src.TypeCout;
import src.VoyageurCorrespondance;

public class VoyageurTest {
    private static final String path_data = "csv/test2.csv";
    private static final String path_cor = "csv/test_cor.csv";

    public PlateformeCorrespondance g;

    ArrayList<String> arg = new ArrayList<String>();
    ArrayList<String> cor = new ArrayList<String>();

    private VoyageurCorrespondance v1 = new VoyageurCorrespondance("nom1", "prenom1", TypeCout.PRIX,
            ModaliteTransport.TRAIN, 500, 500, 500,
            "villeA", "villeD", 1, path_data, path_cor);
    private VoyageurCorrespondance v2 = new VoyageurCorrespondance("nom2", "prenom2", TypeCout.TEMPS,
            ModaliteTransport.TRAIN, 500, 500, 500,
            "villeA", "villeD", 1, path_data, path_cor);

    // mettre modalite a null permet d'emprunter n'importe quelle moyen de transport
    private VoyageurCorrespondance v3 = new VoyageurCorrespondance("nom3", "prenom3", TypeCout.CO2, null, 500, 500, 500,
            "villeA",
            "villeD", 1, path_data, path_cor);

    @BeforeEach
    public void avantTest() {
        // un fichier CSV contenant des sommets, arrete et corresposnance est fournit
        g = ToolsCorrespondance.initPlateforme(v1.getDATA(), v1.getCORRESPONDANCE());

    }

    @Test
    void testVoyageur() {
        assertEquals("nom1", v1.getNom());
        assertEquals("prenom1", v1.getPrenom());
        assertEquals(TypeCout.PRIX, v1.getCritere());
        assertEquals(ModaliteTransport.TRAIN, v1.getModalite());
        assertEquals(500, v1.getThresholdPrix());
        assertEquals(500, v1.getThresholdCO2());
        assertEquals(500, v1.getThresholdTemps());
        assertEquals("villeA", v1.getDepart());
        assertEquals("villeD", v1.getArrivee());
        assertEquals(1, v1.getNb_trajet());
    }

    @Test
    void testComputeBestPath() {
        try {
            List<Chemin> ch1 = v1.computeBestPathTrigger();
            List<Chemin> ch2 = v2.computeBestPathTrigger();
            List<Chemin> ch3 = v3.computeBestPathTrigger();

            assertEquals(v1.getNb_trajet(), ch1.size());
            assertEquals(v2.getNb_trajet(), ch2.size());
            // assertEquals(v3.getNb_trajet(), ch3.size());

            Chemin c1 = ch1.get(0);
            Chemin c2 = ch2.get(0);
            Chemin c3 = ch3.get(0);
            System.err.println(ch1);
            // 78 + 20 de correspondance
            assertEquals(98, c1.poids());
            // l'autre chemin passant par B est egal car il y a une correspondance
            assertEquals(140, c2.poids());

            assertEquals(2.5999999999999996, c3.poids());
        } catch (CheminInexistantException e) {
            System.out.println("Chemin inexistant");
            assertFalse(true);
        }
    }

}
