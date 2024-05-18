package tests.graphes.v1;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.v1.CheminImpl;
import src.v1.LieuImpl;
import src.v1.TranconImpl;
import src.v1.Plateforme;
import src.v1.Tools;
import src.v1.TypeCout;
import src.v1.Voyageur;


public class VoyageurTest {
    public Plateforme g;


    String[] arg = new String[]{
        "villeA;villeB;Train;60;1.7;80",
        "villeB;villeD;Train;22;2.4;40",
        "villeA;villeC;Train;42;1.4;50",
        "villeB;villeC;Train;14;1.4;60",
        "villeC;villeD;Avion;110;150;22",
        "villeC;villeD;Train;65;1.2;90"
    };

    private Voyageur v1 = new Voyageur("nom1", "prenom1", TypeCout.PRIX, ModaliteTransport.TRAIN, 100, 500, 500, "villeA", "villeD", 1);
    private Voyageur v2 = new Voyageur("nom2", "prenom2", TypeCout.TEMPS, ModaliteTransport.TRAIN, 500, 500, 120, "villeA", "villeD", 1);
    private Voyageur v3 = new Voyageur("nom3", "prenom3", TypeCout.CO2, ModaliteTransport.TRAIN, 500, 3, 500, "villeA", "villeD", 1);


    @BeforeEach
    public void avantTest() {
        // initialise les chemin avec les sommet A B C D, construit les arrete et utilise des modalité permettant de tester plusieur cas
        
        g = Tools.initPlateforme(arg);


    }


    @Test
    void testVoyageur() {
        assertEquals("nom1", v1.getNom());
        assertEquals("prenom1", v1.getPrenom());
        assertEquals(TypeCout.PRIX, v1.getCritere());
        assertEquals(ModaliteTransport.TRAIN, v1.getModalite());
        assertEquals(100, v1.getThresholdPrix());
        assertEquals(100, v1.getThresholdCO2());
        assertEquals(100, v1.getThresholdTemps());
        assertEquals("villeA", v1.getDepart());
        assertEquals("villeD", v1.getArrivee());
        assertEquals(1, v1.getNb_trajet());
    }

    @Test
    void testComputeBestPath() {
        List<Chemin> ch1 = v1.computeBestPath();
        List<Chemin> ch2 = v2.computeBestPath();
        List<Chemin> ch3 = v3.computeBestPath();

        assertEquals(v1.getNb_trajet(), ch1.size());
        assertEquals(v2.getNb_trajet(), ch2.size());
        assertEquals(v3.getNb_trajet(), ch3.size());

        Chemin c1 = ch1.get(0);
        Chemin c2 = ch2.get(0);
        Chemin c3 = ch3.get(0);

        assertEquals(78, c1.poids());
        assertEquals(120, c2.poids());
        assertEquals(2.5999999999999996, c3.poids());

        
    }

   
}
