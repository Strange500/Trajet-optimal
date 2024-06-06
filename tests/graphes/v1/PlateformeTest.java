package tests.graphes.v1;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.CheminImpl;
import src.LieuImpl;
import src.Plateforme;
import src.Tools;
import src.TranconImpl;
import src.TypeCout;


public class PlateformeTest {
    public Plateforme g;

    String[] arg = new String[]{
        "villeA;villeB;Train;60;1.7;80",
        "villeB;villeD;Train;22;2.4;40",
        "villeA;villeC;Train;42;1.4;50",
        "villeB;villeC;Train;14;1.4;60",
        "villeC;villeD;Avion;110;150;22",
        "villeC;villeD;Train;65;1.2;90"
    };


    @BeforeEach
    public void avantTest() {
        // initialise les chemin avec les sommet A B C D, construit les arrete et utilise des modalité permettant de tester plusieur cas
        
        g = Tools.initPlateforme(arg);


    }

    @Test
    void testInit() {
        
        
        assertEquals(12, g.getG1().aretes().size());
        assertEquals(4, g.getG1().sommets().size());
        assertEquals(12, g.getG2().aretes().size());
        assertEquals(4, g.getG2().sommets().size());
        assertEquals(12, g.getG3().aretes().size());
        assertEquals(4, g.getG3().sommets().size());


    }

    @Test
    void testHasPathByModalite() {
        assertTrue(g.hasPathByModalite("villeA", "villeD", ModaliteTransport.TRAIN));
        assertTrue(g.hasPathByModalite("villeC", "villeD", ModaliteTransport.AVION));
        assertFalse(g.hasPathByModalite("villeA", "villeD", ModaliteTransport.AVION));
        assertTrue(g.hasPathByModalite("villeC", "villeD", ModaliteTransport.TRAIN));
    }

    @Test
    void testGetPathByModaliteAndTypeCout() {
        Chemin ch = (g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN, TypeCout.PRIX, 1)).get(0);
        assertEquals(78, ch.poids());

        Chemin ch2 = (g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN, TypeCout.TEMPS, 1)).get(0);
        assertEquals(120, ch2.poids());

        Chemin ch3 = (g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN, TypeCout.CO2, 1)).get(0);
        assertEquals(2.5999999999999996, ch3.poids());

        Chemin ch4 = (g.getPathByModaliteAndTypeCout("villeC", "villeD", ModaliteTransport.AVION, TypeCout.TEMPS, 1)).get(0);
        assertEquals(22, ch4.poids());

    }


    @Test
    void testGetPathByTypeCout() {
        Chemin ch = (g.getPathByTypeCout("villeA", "villeD", TypeCout.PRIX, 1)).get(0);
        assertEquals(78, ch.poids());

        Chemin ch2 = (g.getPathByTypeCout("villeA", "villeD", TypeCout.TEMPS, 1)).get(0);
        assertEquals(72, ch2.poids());

        Chemin ch3 = (g.getPathByTypeCout("villeA", "villeD", TypeCout.CO2, 1)).get(0);
        assertEquals(2.5999999999999996, ch3.poids());

        Chemin ch4 = (g.getPathByTypeCout("villeC", "villeD", TypeCout.TEMPS, 1)).get(0);
        assertEquals(22, ch4.poids());

    }

    @Test
    void testClone() {
        Plateforme g2 = g.clone();
        assertEquals(12, g2.getG1().aretes().size());
        assertEquals(4, g2.getG1().sommets().size());
        assertEquals(12, g2.getG2().aretes().size());
        assertEquals(4, g2.getG2().sommets().size());
        assertEquals(12, g2.getG3().aretes().size());
        assertEquals(4, g2.getG3().sommets().size());
    }

    @Test
    void testFilterByModality() {
        Plateforme g2 = g.clone();
        g2.filterByModality(ModaliteTransport.TRAIN);
        for (Lieu l : g2.getG1().sommets()) {
            for (Lieu l2 : g2.getG1().sommets()) {
                for (ModaliteTransport m : ModaliteTransport.values()) {
                    if (m != ModaliteTransport.TRAIN && l.toString() != l2.toString()) {
                        System.err.println(l.toString() + " " + l2.toString() + " " + m.toString());
                        assertFalse(g2.hasPathByModalite(l.toString(), l2.toString(), m));
                    }
                }
            }
        }

        Plateforme g3 = g.clone();
        g3.filterByModality(ModaliteTransport.AVION);
        for (Lieu l : g3.getG1().sommets()) {
            for (Lieu l2 : g3.getG1().sommets()) {
                for (ModaliteTransport m : ModaliteTransport.values()) {
                    if (m != ModaliteTransport.AVION && l.toString() != l2.toString()) {
                        assertFalse(g3.hasPathByModalite(l.toString(), l2.toString(), m));
                    }
                }
            }
        }
    }

    @Test
    void testclone() {
        MultiGrapheOrienteValue g1 = Plateforme.clone(g.getG1());

        assertEquals(g.getG1().aretes().size(), g1.aretes().size());
        assertEquals(g.getG1().sommets().size(), g1.sommets().size());

        MultiGrapheOrienteValue g2 = Plateforme.clone(g.getG2(), ModaliteTransport.TRAIN);

        assertEquals(g.getG2().aretes().size()-2, g2.aretes().size());
        assertEquals(g.getG2().sommets().size(), g2.sommets().size());

    }

    @Test
    void testGetLieux() {
        assertEquals(4, g.getLieux().size());
    }

    @Test
    void testGetLieuxAvoid() {
        assertEquals(3, g.getLieux(new LieuImpl("villeA")).size());
        assertEquals(3, g.getLieux(new LieuImpl("villeB")).size());
        assertEquals(3, g.getLieux(new LieuImpl("villeC")).size());
        assertEquals(3, g.getLieux(new LieuImpl("villeD")).size());
    }


    @Test
    void testAjouterLieus() {
        assertEquals(4, g.getG1().sommets().size());
        assertEquals(4, g.getG2().sommets().size());
        assertEquals(4, g.getG3().sommets().size());
        assertEquals(4, g.getLieux().size());
        g.ajouterLieux("villeE");
        assertEquals(5, g.getLieux().size());
        assertEquals(5, g.getG1().sommets().size());
        assertEquals(5, g.getG2().sommets().size());
        assertEquals(5, g.getG3().sommets().size());
        
    }

    @Test
    void testAjouterArrete() {
        g.ajouterLieux("villeE");
        assertEquals(12, g.getG1().aretes().size());
        assertEquals(12, g.getG2().aretes().size());
        assertEquals(12, g.getG3().aretes().size());
        g.ajouterArrete("villeA", "villeE", ModaliteTransport.TRAIN, 10, 10, 10);
        assertEquals(13, g.getG1().aretes().size());
        assertEquals(13, g.getG2().aretes().size());
        assertEquals(13, g.getG3().aretes().size());
    }

    @Test
    void testGetSommet() {
        assertEquals((new LieuImpl("villeA")).toString(), (g.getSommet("villeA").toString()));
        assertEquals((new LieuImpl("villeB")).toString(), (g.getSommet("villeB").toString()));
    }

    @Test
    void testContientLieux() {
        assertTrue(Plateforme.contientLieux(g.getG1(), "villeA"));
        assertTrue(Plateforme.contientLieux(g.getG2(), "villeB"));
    }
    

    // @Test
    // void testSplitByModalite() {
    //     assertEquals(1, CheminImpl.splitByModalite(ch1).size());
    //     assertEquals(2, CheminImpl.splitByModalite(ch2).size());
    //     assertEquals(2, CheminImpl.splitByModalite(ch3).size());
    //     assertEquals(3, CheminImpl.splitByModalite(ch4).size());
    //     assertEquals(0, CheminImpl.splitByModalite(ch5).size());

    // }

    // @Test
    // void testGetNbChangement() {
    //     assertEquals(0, CheminImpl.getNbChangement(ch1));
    //     assertEquals(1, CheminImpl.getNbChangement(ch2));
    //     assertEquals(1, CheminImpl.getNbChangement(ch3));
    //     assertEquals(2, CheminImpl.getNbChangement(ch4));
    //     assertEquals(0, CheminImpl.getNbChangement(ch5));
    // }

    // @Test
    // void testGetCHangementDuration() {
    //     assertEquals(Plateforme.TEMP_CHANGEMENT * 0, CheminImpl.getCHangementDuration(ch1));
    //     assertEquals(Plateforme.TEMP_CHANGEMENT * 1, CheminImpl.getCHangementDuration(ch2));
    //     assertEquals(Plateforme.TEMP_CHANGEMENT * 1, CheminImpl.getCHangementDuration(ch3));
    //     assertEquals(Plateforme.TEMP_CHANGEMENT * 2, CheminImpl.getCHangementDuration(ch4));
    //     assertEquals(Plateforme.TEMP_CHANGEMENT * 0, CheminImpl.getCHangementDuration(ch5));
    // }

   
}
