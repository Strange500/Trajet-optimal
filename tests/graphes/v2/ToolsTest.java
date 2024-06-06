package tests.graphes.v2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.v1.exception.CheminInexistantException;
import src.v2.CheminImpl;
import src.v2.LieuImpl;
import src.v2.TranconImpl;
import src.v2.Plateforme;
import src.v2.TypeCout;
import src.v2.Tools;

public class ToolsTest {

    ArrayList<String> arg1Ok = new ArrayList<String>();

    ArrayList<String> arg1PasOk1 = new ArrayList<String>();

    ArrayList<String> arg1PasOk2 = new ArrayList<String>();

    ArrayList<String> arg1PasOk3 = new ArrayList<String>();

    ArrayList<String> correspondances = new ArrayList<String>();

    public Plateforme g;

    @BeforeEach
    public void avantTest() {
        arg1Ok.add("villeA;villeB;Train;60;1.7;80");
        arg1Ok.add("villeB;villeD;Train;22;2.4;40");
        arg1Ok.add("villeA;villeC;Train;42;1.4;50");
        arg1Ok.add("villeB;villeC;Train;14;1.4;60");
        arg1Ok.add("villeC;villeD;Avion;110;150;22");
        arg1Ok.add("villeC;villeD;Train;65;1.2;90");

        arg1PasOk1.add("villeA;villeB;Train;");
        arg1PasOk1.add("villeB;villeD;Train;22;2.4;40");
        arg1PasOk1.add("villeA;villeC;Train;42;1.4;50");
        arg1PasOk1.add("villeB;villeC;Train;14;1.4;60");
        arg1PasOk1.add("villeC;villeD;Avion;110;150;22");
        arg1PasOk1.add("villeC;villeD;Train;65;1.2;90");
        arg1PasOk1.add("villeC;villeD;Train;65;1.2;90");

        arg1PasOk2.add("villeA;villeB;Train;60;1.7;80");
        arg1PasOk2.add("villeB;villeD;Train;22;;40");
        arg1PasOk2.add("villeA;villeC;Train;42;1.4;50");
        arg1PasOk2.add("villeB;villeC;Train;14;1.4;60");
        arg1PasOk2.add("villeC;villeD;Avion;110;150;22");

        arg1PasOk3.add("villeA;villeB;Train;60;1.7;80");
        arg1PasOk3.add("villeB;villeD;Train;22;1.4;40");
        arg1PasOk3.add("villeA;villeC;Train;42;1.4;50");
        arg1PasOk3.add("villeB;villeC;Train;14;1.4;");
        arg1PasOk3.add("villeC;villeD;Avion;110;150;22");

        correspondances.add("villaA;Train;Avion;130;0.1;20");
        correspondances.add("villaB;Train;Avion;130;0.1;20");
        correspondances.add("villaC;Train;Avion;130;0.1;20");
        correspondances.add("villaD;Train;Avion;130;0.1;20");

        // init plateform g et sommets A B C D

        g = Tools.initPlateforme(arg1Ok, new ArrayList<String>());

    }

    @Test
    public void testestNombre() {
        assertTrue(Tools.estNombre("123"));
        assertTrue(Tools.estNombre("123.456"));
        assertFalse(Tools.estNombre("123.456.789"));
        assertFalse(Tools.estNombre("123.456.789,123"));
        assertFalse(Tools.estNombre("bonjour moi pas nombre"));
    }

    @Test
    public void testDonneesValide() {
        assertTrue(Tools.donneesValides(arg1Ok));
        assertFalse(Tools.donneesValides(arg1PasOk1));
        assertFalse(Tools.donneesValides(arg1PasOk2));
        assertFalse(Tools.donneesValides(arg1PasOk3));
    }

    @Test
    public void testGetCSV() {
        ArrayList<String> res = new ArrayList<String>();
        res = Tools.getCSV("src/v2/csv/test.csv");
        assertEquals(5, res.size());
        assertEquals("toto", res.get(0));
        assertEquals("tata", res.get(1));
        assertEquals("tutu", res.get(2));
        assertEquals("ToTo", res.get(3));
        assertEquals("AHH", res.get(4));
    }

    @Test
    public void testInitPlateforme() {
        Plateforme p = Tools.initPlateforme(arg1Ok, correspondances);
        for (String l : p.getLieux()) {
            System.out.println(l);
        }
        assertEquals(4, p.getLieux().size());
    }

    @Test
    public void testApplyThreshold() {
        try {
            List<Chemin> chPrix = g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN,
                    TypeCout.PRIX, 3 *100);

            chPrix = Tools.removeDuplicates(chPrix, 3);
            
            List<Chemin> chCO2 = g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN,
                    TypeCout.CO2, 3 *100);
            chCO2 = Tools.removeDuplicates(chCO2, 3);

            List<Chemin> chTemps = g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN,
                    TypeCout.TEMPS, 3 *100);
            chTemps = Tools.removeDuplicates(chTemps, 3);
            // nous avons ici le premier trajet a 78€ puis 82€ et enfin 107€
            assertEquals(3, chPrix.size());
            assertEquals(78, chPrix.get(0).poids());
            assertEquals(82, chPrix.get(1).poids());
            assertEquals(107, chPrix.get(2).poids());

            // nous avons ici le premier trajet a 2,6 kgCO2 puis 4,1kgCO2 et enfin 4,3kgCO2
            assertEquals(3, chCO2.size());
            assertEquals(2.5999999999999996, chCO2.get(0).poids());
            assertEquals(4.1, chCO2.get(1).poids());
            assertEquals(4.3, chCO2.get(2).poids());

            // nous avons ici le premier trajet a 120min puis 140min et enfin 150min
            assertEquals(3, chTemps.size());
            assertEquals(120, chTemps.get(0).poids());
            assertEquals(140, chTemps.get(1).poids());
            assertEquals(150, chTemps.get(2).poids());

            Tools.applyThreshold(g, chPrix, TypeCout.PRIX, 100);
            assertEquals(2, chPrix.size());

            Tools.applyThreshold(g, chPrix, TypeCout.PRIX, 80);
            assertEquals(1, chPrix.size());

            Tools.applyThreshold(g, chCO2, TypeCout.CO2, 4);
            assertEquals(1, chCO2.size());

            Tools.applyThreshold(g, chCO2, TypeCout.CO2, 3);
            assertEquals(1, chCO2.size());

            Tools.applyThreshold(g, chTemps, TypeCout.TEMPS, 140);
            assertEquals(2, chTemps.size());
        } catch (CheminInexistantException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCheminWithCorre() {
        try {
            List<Chemin> chPrix = g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN,
                    TypeCout.PRIX, 3*100);
            chPrix = Tools.removeDuplicates(chPrix, 3);

            assertEquals("TRAIN de villeA à villeD en passant par villeC, villeB, total: 78.0 €",
                    Tools.cheminWithCorre(chPrix.get(0), TypeCout.PRIX));
            assertEquals("TRAIN de villeA à villeD en passant par villeB, total: 82.0 €",
                    Tools.cheminWithCorre(chPrix.get(1), TypeCout.PRIX));
            assertEquals("TRAIN de villeA à villeD en passant par villeC, total: 107.0 €",
                    Tools.cheminWithCorre(chPrix.get(2), TypeCout.PRIX));
        } catch (CheminInexistantException e) {
            e.printStackTrace();
        }
    }

    // Tools.applyThreshold(p, ch1, TypeCout.PRIX, 20);
    // assertEquals(2, ch1.aretes().size());
    // Tools.applyThreshold(p, ch2, TypeCout.CO2,
}
