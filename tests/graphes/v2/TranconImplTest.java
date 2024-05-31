package tests.graphes.v2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.v1.CheminImpl;
import src.v1.LieuImpl;
import src.v1.TranconImpl;

public class TranconImplTest {
    public Lieu A;
    public Lieu B;
    public Lieu C;
    public Lieu D;

    public TranconImpl tr1;
    public TranconImpl tr2;
    public TranconImpl tr3;
    public TranconImpl tr4;
    public TranconImpl tr5;

    @BeforeEach
    public void avantTest() {

        A = new LieuImpl("A");
        B = new LieuImpl("B");
        C = new LieuImpl("C");
        D = new LieuImpl("D");

        tr1 = new TranconImpl(A, B, ModaliteTransport.AVION);
        tr2 = new TranconImpl(B, C, ModaliteTransport.TRAIN);
        tr3 = new TranconImpl(C, D, ModaliteTransport.AVION);
        tr4 = new TranconImpl(A, B, ModaliteTransport.TRAIN);
        tr5 = new TranconImpl(B, C, ModaliteTransport.AVION);

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
