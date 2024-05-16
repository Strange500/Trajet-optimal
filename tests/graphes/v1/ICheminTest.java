package tests.graphes.v1;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.v1.IChemin;
import src.v1.ILieu;
import src.v1.ITrancon;


public class ICheminTest {
    public Lieu A;
    public Lieu B;
    public Lieu C;
    public Lieu D;


    public IChemin ch1;
    public IChemin ch2;
    public IChemin ch3;
    public IChemin ch4;


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
    }

    // @Test
    // void testAddTask() {
    //     assertEquals(0, evt1.getNbTasks());
    //     assertNull(evt1.getChores()[0]);
    //     evt1.addTask(t1);
    //     assertEquals(1, evt1.getNbTasks());
    //     assertEquals(t1, evt1.getChores()[0]);
    //     evt1.addTask(t2);
    //     assertEquals(2, evt1.getNbTasks());
    //     assertEquals(t2, evt1.getChores()[1]);
    //     evt1.addTask(t1);
    //     assertEquals(3, evt1.getNbTasks());
    //     assertEquals(t1, evt1.getChores()[0]);
    //     assertEquals(t1, evt1.getChores()[2]);
    // }

    // @Test
    // void testEquals() {
    //     assertTrue(evt1.equals(evt1));
    //     assertFalse(evt1.equals(evt2));
    //     assertFalse(evt1.equals(evt3));
    //     assertFalse(evt1.equals(evt4));
    //     assertFalse(evt1.equals(evt5));
    //     assertTrue(evt1.equals(evt6));
    //     //
    //     assertTrue(evt2.equals(evt2));
    //     assertFalse(evt2.equals(evt3));
    //     assertFalse(evt2.equals(evt4));
    //     assertFalse(evt2.equals(evt5));
    //     assertFalse(evt2.equals(evt6));
    // }

    // @Test
    // void testOverlap() {
    //     assertTrue(evt1.overlap(evt1));
    //     assertFalse(evt1.overlap(evt2));
    //     assertFalse(evt1.overlap(evt3));
    //     assertFalse(evt1.overlap(evt4));
    //     assertTrue(evt1.overlap(evt5));
    //     assertTrue(evt1.overlap(evt6));
    //     assertFalse(evt2.overlap(evt3));
    //     assertTrue(evt2.overlap(evt4));
    //     assertTrue(evt3.overlap(evt4));
    // }
}
