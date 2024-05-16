package tests.graphes.v1;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.v1.IChemin;
import src.v1.ILieu;
import src.v1.ITrancon;
import src.v1.Tp;


public class ICheminTest {
    public Lieu A;
    public Lieu B;
    public Lieu C;
    public Lieu D;


    public IChemin ch1;
    public IChemin ch2;
    public IChemin ch3;
    public IChemin ch4;
    public IChemin ch5;


    @BeforeEach
    public void avantTest() {
        // initialise les chemin avec les sommet A B C D, construit les arrete et utilise des modalité permettant de tester plusieur cas
        
        A = new ILieu("A");
        B = new ILieu("B");
        C = new ILieu("C");
        D = new ILieu("D");
        

        ch1 = new IChemin();
        ch2 = new IChemin();
        ch3 = new IChemin();
        ch4 = new IChemin();
        ch5 = new IChemin();


        ch1.aretes().add(new ITrancon(A, B, ModaliteTransport.AVION));
        ch1.aretes().add(new ITrancon(B, C, ModaliteTransport.AVION));
        ch1.aretes().add(new ITrancon(C, D, ModaliteTransport.AVION));

        ch2.aretes().add(new ITrancon(A, B, ModaliteTransport.TRAIN));
        ch2.aretes().add(new ITrancon(B, C, ModaliteTransport.AVION));
        ch2.aretes().add(new ITrancon(C, D, ModaliteTransport.AVION));

        ch3.aretes().add(new ITrancon(A, B, ModaliteTransport.TRAIN));
        ch3.aretes().add(new ITrancon(B, C, ModaliteTransport.TRAIN));
        ch3.aretes().add(new ITrancon(C, D, ModaliteTransport.AVION));

        ch4.aretes().add(new ITrancon(A, B, ModaliteTransport.TRAIN));
        ch4.aretes().add(new ITrancon(B, C, ModaliteTransport.AVION));
        ch4.aretes().add(new ITrancon(C, D, ModaliteTransport.TRAIN));


    }

    @Test
    void testSplitByModalite() {
        assertEquals(1, IChemin.splitByModalite(ch1).size());
        assertEquals(2, IChemin.splitByModalite(ch2).size());
        assertEquals(2, IChemin.splitByModalite(ch3).size());
        assertEquals(3, IChemin.splitByModalite(ch4).size());
        assertEquals(0, IChemin.splitByModalite(ch5).size());

    }

    @Test
    void testGetNbChangement() {
        assertEquals(0, IChemin.getNbChangement(ch1));
        assertEquals(1, IChemin.getNbChangement(ch2));
        assertEquals(1, IChemin.getNbChangement(ch3));
        assertEquals(2, IChemin.getNbChangement(ch4));
        assertEquals(0, IChemin.getNbChangement(ch5));
    }

    @Test
    void testGetCHangementDuration() {
        assertEquals(Tp.TEMP_CHANGEMENT * 0, IChemin.getCHangementDuration(ch1));
        assertEquals(Tp.TEMP_CHANGEMENT * 1, IChemin.getCHangementDuration(ch2));
        assertEquals(Tp.TEMP_CHANGEMENT * 1, IChemin.getCHangementDuration(ch3));
        assertEquals(Tp.TEMP_CHANGEMENT * 2, IChemin.getCHangementDuration(ch4));
        assertEquals(Tp.TEMP_CHANGEMENT * 0, IChemin.getCHangementDuration(ch5));
    }

   
}
