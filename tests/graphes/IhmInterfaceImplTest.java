package tests.graphes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.exception.CheminInexistantException;
import src.IhmInterfaceImpl;
import src.PlateformeCorrespondance;
import src.Podium;
import src.ToolsCorrespondance;
import src.TypeCout;
import src.VoyageurCorrespondance;

// TODO


public class IhmInterfaceImplTest {
    private static final String path_data = "csv/test_mapV3.csv";
    private static final String path_cor = "csv/test_corV3.csv";

    public PlateformeCorrespondance g;

    ArrayList<String> arg = new ArrayList<String>();
    ArrayList<String> cor = new ArrayList<String>();

    private IhmInterfaceImpl i ;
    private Podium<TypeCout> p ;

    @BeforeEach
    public void avantTest() {
        // un fichier CSV contenant des sommets, arretes et correspondances est fourni
        i = new IhmInterfaceImpl("test", ToolsCorrespondance.getCSV(path_data), ToolsCorrespondance.getCSV(path_cor));
        p = new Podium<>();
        p.setFirst(TypeCout.CO2);
        p.setSecond(TypeCout.PRIX);
        p.setThird(TypeCout.TEMPS); 
    }

    @Test
    void testComputeBestPath() {
        try {
            Map<Double, Chemin> ch1 = i.getBestResults(p, "1", "4");
            List<Double> keys = new ArrayList<Double>(ch1.keySet());
            Collections.sort(keys);

            Chemin c1 = ch1.get(keys.get(0));
            System.err.println(ToolsCorrespondance.cheminWithCorreArrow(c1, null));
            // verification du score
            assertEquals(0.009797916666666667, keys.get(0));

            Chemin c2 = ch1.get(keys.get(2));
            System.err.println(ToolsCorrespondance.cheminWithCorreArrow(c2, null));
            // verification du score
            assertEquals(0.02316666666666667, keys.get(2));


        } catch (CheminInexistantException e) {
            System.out.println("Chemin inexistant");
            assertFalse(true);
        }
    }

}
