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

// TODO


public class IhmInterfaceImplTest {
    private static final String path_data = "csv/testVoyageur_map.csv";
    private static final String path_cor = "csv/testVoyageur_cor.csv";

    public PlateformeCorrespondance g;

    ArrayList<String> arg = new ArrayList<String>();
    ArrayList<String> cor = new ArrayList<String>();

    private VoyageurCorrespondance v1 = new VoyageurCorrespondance("nom1", "prenom1", TypeCout.PRIX, null, 500, 500,
            500,
            "1", "4", 3, path_data, path_cor);

    @BeforeEach
    public void avantTest() {
        // un fichier CSV contenant des sommets, arretes et correspondances est fourni
        g = ToolsCorrespondance.initPlateforme(v1.getDATA(), v1.getCORRESPONDANCE());

    }

    @Test
    void testComputeBestPath() {
        try {
            List<Chemin> ch1 = v1.computeBestPathTrigger();

            assertEquals(v1.getNb_trajet(), ch1.size());

            Chemin c1 = ch1.get(0);
            System.err.println(ToolsCorrespondance.cheminWithCorreDEBUG(c1, TypeCout.PRIX));
            assertEquals(6, c1.poids());

            Chemin c2 = ch1.get(1);
            System.err.println(ToolsCorrespondance.cheminWithCorreDEBUG(c2, TypeCout.PRIX));
            assertEquals(8, c2.poids());

            Chemin c3 = ch1.get(2);
            System.err.println(ToolsCorrespondance.cheminWithCorreDEBUG(c3, TypeCout.PRIX));
            assertEquals(9, c3.poids());

        } catch (CheminInexistantException e) {
            System.out.println("Chemin inexistant");
            assertFalse(true);
        }
    }

}
