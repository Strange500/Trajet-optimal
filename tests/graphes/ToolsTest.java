package tests.graphes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.exception.CheminInexistantException;
import src.CheminImpl;
import src.LieuImpl;
import src.PlateformeCorrespondance;
import src.TypeCout;
import src.Tools;
import src.ToolsCorrespondance;
import src.TranconImpl;

public class ToolsTest {

    ArrayList<String> arg1Ok = new ArrayList<String>();

    ArrayList<String> arg1PasOk1 = new ArrayList<String>();

    ArrayList<String> arg1PasOk2 = new ArrayList<String>();

    ArrayList<String> arg1PasOk3 = new ArrayList<String>();

    ArrayList<String> correspondances = new ArrayList<String>();

    public PlateformeCorrespondance g;

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

        g = ToolsCorrespondance.initPlateforme(arg1Ok, new ArrayList<String>());

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
        assertTrue(ToolsCorrespondance.donneesValides(arg1Ok));
        assertFalse(ToolsCorrespondance.donneesValides(arg1PasOk1));
        assertFalse(ToolsCorrespondance.donneesValides(arg1PasOk2));
        assertFalse(ToolsCorrespondance.donneesValides(arg1PasOk3));
    }

    @Test
    public void testGetCSV() {
        ArrayList<String> res = new ArrayList<String>();
        res = ToolsCorrespondance.getCSV("csv/test.csv");
        assertEquals(5, res.size());
        assertEquals("toto", res.get(0));
        assertEquals("tata", res.get(1));
        assertEquals("tutu", res.get(2));
        assertEquals("ToTo", res.get(3));
        assertEquals("AHH", res.get(4));
    }

    @Test
    public void testInitPlateforme() {
        PlateformeCorrespondance p = ToolsCorrespondance.initPlateforme(arg1Ok, correspondances);
        for (String l : p.getLieuxNames()) {
            System.out.println(l);
        }
        assertEquals(4, p.getLieuxNames().size());
    }

    @Test
    public void testApplyThreshold() {
        try {
            List<Chemin> chPrix = g.getPathByModaliteAndTypeCoutTriggerNoPath("villeA", "villeD",
                    ModaliteTransport.TRAIN,
                    TypeCout.PRIX, 3 * 100);

            chPrix = ToolsCorrespondance.removeDuplicates(chPrix, 3);

            List<Chemin> chCO2 = g.getPathByModaliteAndTypeCoutTriggerNoPath("villeA", "villeD",
                    ModaliteTransport.TRAIN,
                    TypeCout.CO2, 3 * 100);
            chCO2 = ToolsCorrespondance.removeDuplicates(chCO2, 3);

            List<Chemin> chTemps = g.getPathByModaliteAndTypeCoutTriggerNoPath("villeA", "villeD",
                    ModaliteTransport.TRAIN,
                    TypeCout.TEMPS, 3 * 100);
            chTemps = ToolsCorrespondance.removeDuplicates(chTemps, 3);
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
            List<Chemin> chPrix = g.getPathByModaliteAndTypeCoutTriggerNoPath("villeA", "villeD",
                    ModaliteTransport.TRAIN,
                    TypeCout.PRIX, 3 * 100);
            chPrix = ToolsCorrespondance.removeDuplicates(chPrix, 3);

            assertEquals("TRAIN de villeA à villeD en passant par villeC, villeB, total: 78.0 €",
                    ToolsCorrespondance.cheminWithCorre(chPrix.get(0), TypeCout.PRIX));
            assertEquals("TRAIN de villeA à villeD en passant par villeB, total: 82.0 €",
                    ToolsCorrespondance.cheminWithCorre(chPrix.get(1), TypeCout.PRIX));
            assertEquals("TRAIN de villeA à villeD en passant par villeC, total: 107.0 €",
                    ToolsCorrespondance.cheminWithCorre(chPrix.get(2), TypeCout.PRIX));
        } catch (CheminInexistantException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLieuxWithDepModality() {
        ModaliteTransport m = ModaliteTransport.TRAIN;
        List<String> res = ToolsCorrespondance.getLieuxWithDepModality("test", m);
        for (String l : res) {
            assertEquals(l.split("_")[2], m.toString());
        }
    }

    @Test
    public void testGetLieuxWithArrModality() {
        ModaliteTransport m = ModaliteTransport.TRAIN;
        List<String> res = ToolsCorrespondance.getLieuxWithArrModality("test", m);
        for (String l : res) {
            assertEquals(l.split("_")[1], m.toString());
        }
    }

    @Test
    public void testCleanLieux() {
        String res = "test_toto";
        assertEquals(ToolsCorrespondance.cleanLieux(res), "test");
    }

    @Test
    public void testEqualsArrete() {
        Lieu l1 = new LieuImpl("villeA_test_test");
        Lieu l11 = new LieuImpl("villeA_test_toto");
        Lieu l12 = new LieuImpl("villeA_toto_test");
        Lieu l2 = new LieuImpl("villeB_test_test");
        Lieu l21 = new LieuImpl("villeB_test_toto");
        Lieu l22 = new LieuImpl("villeB_toto_test");
        Lieu l3 = new LieuImpl("villeC_test_test");

        Trancon a1 = new TranconImpl(l1, l2, ModaliteTransport.TRAIN);
        Trancon a11 = new TranconImpl(l11, l21, ModaliteTransport.TRAIN);
        Trancon a12 = new TranconImpl(l12, l22, ModaliteTransport.TRAIN);

        Trancon a2 = new TranconImpl(l1, l2, ModaliteTransport.TRAIN);
        Trancon a21 = new TranconImpl(l12, l21, ModaliteTransport.TRAIN);
        Trancon a22 = new TranconImpl(l11, l22, ModaliteTransport.TRAIN);

        Trancon a3 = new TranconImpl(l1, l3, ModaliteTransport.TRAIN);
        

        assertTrue(ToolsCorrespondance.equalsArrete(a1, a2));
        assertTrue(ToolsCorrespondance.equalsArrete(a11, a2));
        assertTrue(ToolsCorrespondance.equalsArrete(a12, a2));
        assertTrue(ToolsCorrespondance.equalsArrete(a1, a21));
        assertTrue(ToolsCorrespondance.equalsArrete(a1, a22));
        assertTrue(ToolsCorrespondance.equalsArrete(a11, a21));
        assertTrue(ToolsCorrespondance.equalsArrete(a12, a22));
        assertFalse(ToolsCorrespondance.equalsArrete(a1, a3));

    }

    @Test
    public void testEqualsChemin() {
        Lieu l1 = new LieuImpl("villeA_test_test");
        Lieu l11 = new LieuImpl("villeA_test_toto");
        Lieu l12 = new LieuImpl("villeA_toto_test");
        Lieu l2 = new LieuImpl("villeB_test_test");
        Lieu l21 = new LieuImpl("villeB_test_toto");
        Lieu l22 = new LieuImpl("villeB_toto_test");
        Lieu l3 = new LieuImpl("villeC_test_test");

        Trancon a1 = new TranconImpl(l1, l2, ModaliteTransport.TRAIN);
        Trancon a11 = new TranconImpl(l11, l21, ModaliteTransport.TRAIN);
        Trancon a12 = new TranconImpl(l12, l22, ModaliteTransport.TRAIN);

        Trancon a2 = new TranconImpl(l1, l2, ModaliteTransport.TRAIN);
        Trancon a21 = new TranconImpl(l12, l21, ModaliteTransport.TRAIN);
        Trancon a22 = new TranconImpl(l11, l22, ModaliteTransport.TRAIN);

        Trancon a3 = new TranconImpl(l1, l3, ModaliteTransport.TRAIN);

        Chemin c1 = new CheminImpl();
        c1.aretes().add(a1);
        c1.aretes().add(a11);
        c1.aretes().add(a12);

        Chemin c2 = new CheminImpl();
        c2.aretes().add(a2);
        c2.aretes().add(a21);
        c2.aretes().add(a22);

        Chemin c3 = new CheminImpl();
        c3.aretes().add(a1);
        c3.aretes().add(a11);
        c3.aretes().add(a12);

        assertTrue(ToolsCorrespondance.equalsChemin(c1, c2));
        assertTrue(ToolsCorrespondance.equalsChemin(c1, c3));
        assertTrue(ToolsCorrespondance.equalsChemin(c2, c3));
        assertFalse(ToolsCorrespondance.equalsChemin(c1, new CheminImpl()));

    }

    @Test
    public void testRemoveDuplicates() {
        Lieu l1 = new LieuImpl("villeA_test_test");
        Lieu l11 = new LieuImpl("villeA_test_toto");
        Lieu l12 = new LieuImpl("villeA_toto_test");
        Lieu l2 = new LieuImpl("villeB_test_test");
        Lieu l21 = new LieuImpl("villeB_test_toto");
        Lieu l22 = new LieuImpl("villeB_toto_test");
        Lieu l3 = new LieuImpl("villeC_test_test");

        Trancon a1 = new TranconImpl(l1, l2, ModaliteTransport.TRAIN);
        Trancon a11 = new TranconImpl(l11, l21, ModaliteTransport.TRAIN);
        Trancon a12 = new TranconImpl(l12, l22, ModaliteTransport.TRAIN);

        Trancon a2 = new TranconImpl(l1, l2, ModaliteTransport.TRAIN);
        Trancon a21 = new TranconImpl(l12, l21, ModaliteTransport.TRAIN);
        Trancon a22 = new TranconImpl(l11, l22, ModaliteTransport.TRAIN);

        Trancon a3 = new TranconImpl(l1, l3, ModaliteTransport.TRAIN);

        Chemin c1 = new CheminImpl();
        c1.aretes().add(a1);
        c1.aretes().add(a11);
        c1.aretes().add(a12);

        Chemin c2 = new CheminImpl();
        c2.aretes().add(a2);
        c2.aretes().add(a21);
        c2.aretes().add(a22);

        Chemin c3 = new CheminImpl();
        c3.aretes().add(a1);
        c3.aretes().add(a11);
        c3.aretes().add(a3);

        List<Chemin> ch = new ArrayList<Chemin>();
        ch.add(c1);
        ch.add(c2);
        ch.add(c3);
        ch.add(c1);
        ch.add(c2);
        ch.add(c3);

        List<Chemin> res = ToolsCorrespondance.removeDuplicates(ch, 3);
        assertEquals(2, res.size());
    }

    // Tools.applyThreshold(p, ch1, TypeCout.PRIX, 20);
    // assertEquals(2, ch1.aretes().size());
    // Tools.applyThreshold(p, ch2, TypeCout.CO2,
}
