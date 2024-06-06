package tests.graphes.v1;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.CheminImpl;
import src.LieuImpl;
import src.Plateforme;
import src.Tools;
import src.TranconImpl;
import src.TypeCout;


public class ToolsTest {
    
    String[] arg1Ok = new String[]{
        "villeA;villeB;Train;60;1.7;80",
        "villeB;villeD;Train;22;2.4;40",
        "villeA;villeC;Train;42;1.4;50",
        "villeB;villeC;Train;14;1.4;60",
        "villeC;villeD;Avion;110;150;22",
        "villeC;villeD;Train;65;1.2;90"
    };

    String[] arg1PasOk1 = new String[]{
        "villeA;villeB;Train;",
        "villeB;villeD;Train;22;2.4;40",
        "villeA;villeC;Train;42;1.4;50",
        "villeB;villeC;Train;14;1.4;60",
        "villeC;villeD;Avion;110;150;22",
        "villeC;villeD;Train;65;1.2;90",
        "villeC;villeD;Train;65;1.2;90;1"
    };

    String[] arg1PasOk2 = new String[]{
        "villeA;villeB;Train;60;1.7;80",
        "villeB;villeD;Train;22;;40",
        "villeA;villeC;Train;42;1.4;50",
        "villeB;villeC;Train;14;1.4;60",
        "villeC;villeD;Avion;110;150;22"
    };

    String[] arg1PasOk3 = new String[]{
        "villeA;villeB;Train;60;1.7;80",
        "villeB;villeD;Train;22;1.4;40",
        "villeA;villeC;Train;42;1.4;50",
        "villeB;villeC;Train;14;1.4;",
        "villeC;villeD;Avion;110;150;22"
    };

   

    public Plateforme g;

    @BeforeEach
    public void avantTest() {
        // init plateform g et sommets A B C D


        g = Tools.initPlateforme(arg1Ok);
        
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
    public void testInitPlateforme() {
        Plateforme p = Tools.initPlateforme(arg1Ok);
        assertEquals(4, p.getLieux().size());
        // le nombre d'arrete est 12 car les arretes sont ajouté dans les deux sens
        assertEquals(12, p.getG1().aretes().size());
        assertEquals(12, p.getG2().aretes().size());
        assertEquals(12, p.getG3().aretes().size());

        assertEquals(4, p.getG1().sommets().size());
        assertEquals(4, p.getG2().sommets().size());
        assertEquals(4, p.getG3().sommets().size());
    }

    @Test
    public void testApplyThreshold() {
        List<Chemin> chPrix = g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN, TypeCout.PRIX, 3);
        List<Chemin> chCO2 = g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN, TypeCout.CO2, 3);
        List<Chemin> chTemps = g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN, TypeCout.TEMPS, 3);
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

        



    }

    @Test
    public void testCheminWithCorre() {
        List<Chemin> chPrix = g.getPathByModaliteAndTypeCout("villeA", "villeD", ModaliteTransport.TRAIN, TypeCout.PRIX, 3);

        assertEquals("TRAIN de villeA à villeD en passant par villeC, villeB totale: 78.0 €", Tools.cheminWithCorre(chPrix.get(0), TypeCout.PRIX));
        assertEquals("TRAIN de villeA à villeD en passant par villeB totale: 82.0 €", Tools.cheminWithCorre(chPrix.get(1), TypeCout.PRIX));
        assertEquals("TRAIN de villeA à villeD en passant par villeC totale: 107.0 €", Tools.cheminWithCorre(chPrix.get(2), TypeCout.PRIX));
    }

        // Tools.applyThreshold(p, ch1, TypeCout.PRIX, 20);
        // assertEquals(2, ch1.aretes().size());
        // Tools.applyThreshold(p, ch2, TypeCout.CO2,
}
