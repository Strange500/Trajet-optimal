package tests.graphes.v1;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.v1.IChemin;
import src.v1.ILieu;
import src.v1.ITrancon;
import src.v1.Plateforme;
import src.v1.Tp;
import src.v1.TypeCout;
import src.v1.Tools;


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

    // @Test
    // public void testApplyThreshold() {
    //     List<Chemin> ch1 = g.getPathByModaliteAndTypeCout("A", "D", ModaliteTransport.TRAIN, TypeCout.PRIX, 3);
    //     for (Chemin c : ch1) {
    //         System.err.println(c);
    //     }
    //     // nous avons ici le premier trajet a 78€ puis 82€ et enfin 107€
    //     assertEquals(3, ch1.size());
    //     assertEquals(78, ch1.get(0).poids());
    //     assertEquals(82, ch1.get(1).poids());
    //     assertEquals(107, ch1.get(2).poids());

    // }

        // Tools.applyThreshold(p, ch1, TypeCout.PRIX, 20);
        // assertEquals(2, ch1.aretes().size());
        // Tools.applyThreshold(p, ch2, TypeCout.CO2,
}
