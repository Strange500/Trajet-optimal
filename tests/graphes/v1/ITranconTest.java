package tests.graphes.v1;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.v1.IChemin;
import src.v1.ILieu;
import src.v1.ITrancon;


public class ITranconTest {
    public Lieu A;
    public Lieu B;
    public Lieu C;
    public Lieu D;

    public ITrancon tr1;
    public ITrancon tr2;
    public ITrancon tr3;
    public ITrancon tr4;
    public ITrancon tr5;



    @BeforeEach
    public void avantTest() {
        
        A = new ILieu("A");
        B = new ILieu("B");
        C = new ILieu("C");
        D = new ILieu("D");
        

        tr1 = new ITrancon(A, B, ModaliteTransport.AVION);
        tr2 = new ITrancon(B, C, ModaliteTransport.TRAIN);
        tr3 = new ITrancon(C, D, ModaliteTransport.AVION);
        tr4 = new ITrancon(A, B, ModaliteTransport.TRAIN);
        tr5 = new ITrancon(B, C, ModaliteTransport.AVION);


    }

   
    @Test
    public void testGetDepart() {
        assertEquals(A, tr1.getDepart());
        assertEquals(B, tr2.getDepart());
        assertEquals(C, tr3.getDepart());
        assertEquals(A, tr4.getDepart());
        assertEquals(B, tr5.getDepart());
    }

    @Test
    public void testGetArrivee() {
        assertEquals(B, tr1.getArrivee());
        assertEquals(C, tr2.getArrivee());
        assertEquals(D, tr3.getArrivee());
        assertEquals(B, tr4.getArrivee());
        assertEquals(C, tr5.getArrivee());
    }

    @Test
    public void testGetModalite() {
        assertEquals(ModaliteTransport.AVION, tr1.getModalite());
        assertEquals(ModaliteTransport.TRAIN, tr2.getModalite());
        assertEquals(ModaliteTransport.AVION, tr3.getModalite());
        assertEquals(ModaliteTransport.TRAIN, tr4.getModalite());
        assertEquals(ModaliteTransport.AVION, tr5.getModalite());
    }

    @Test
    public void testToString() {
        assertEquals("A -> B (AVION)", tr1.toString());
        assertEquals("B -> C (TRAIN)", tr2.toString());
        assertEquals("C -> D (AVION)", tr3.toString());
        assertEquals("A -> B (TRAIN)", tr4.toString());
        assertEquals("B -> C (AVION)", tr5.toString());
    }

   

   
}
