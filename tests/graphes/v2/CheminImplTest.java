package tests.graphes.v2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.v2.CheminImpl;
import src.v2.LieuImpl;
import src.v2.TranconImpl;
import src.v2.Plateforme;

public class CheminImplTest {
    public Lieu A;
    public Lieu B;
    public Lieu C;
    public Lieu D;

    public CheminImpl ch1;
    public CheminImpl ch2;
    public CheminImpl ch3;
    public CheminImpl ch4;
    public CheminImpl ch5;

    @BeforeEach
    public void avantTest() {
        // initialise les chemin avec les sommet A B C D, construit les arrete et
        // utilise des modalité permettant de tester plusieur cas

        A = new LieuImpl("A");
        B = new LieuImpl("B");
        C = new LieuImpl("C");
        D = new LieuImpl("D");

        ch1 = new CheminImpl();
        ch2 = new CheminImpl();
        ch3 = new CheminImpl();
        ch4 = new CheminImpl();
        ch5 = new CheminImpl();

        ch1.aretes().add(new TranconImpl(A, B, ModaliteTransport.AVION));
        ch1.aretes().add(new TranconImpl(B, C, ModaliteTransport.AVION));
        ch1.aretes().add(new TranconImpl(C, D, ModaliteTransport.AVION));

        ch2.aretes().add(new TranconImpl(A, B, ModaliteTransport.TRAIN));
        ch2.aretes().add(new TranconImpl(B, C, ModaliteTransport.AVION));
        ch2.aretes().add(new TranconImpl(C, D, ModaliteTransport.AVION));

        ch3.aretes().add(new TranconImpl(A, B, ModaliteTransport.TRAIN));
        ch3.aretes().add(new TranconImpl(B, C, ModaliteTransport.TRAIN));
        ch3.aretes().add(new TranconImpl(C, D, ModaliteTransport.AVION));

        ch4.aretes().add(new TranconImpl(A, B, ModaliteTransport.TRAIN));
        ch4.aretes().add(new TranconImpl(B, C, ModaliteTransport.AVION));
        ch4.aretes().add(new TranconImpl(C, D, ModaliteTransport.TRAIN));

    }

    @Test
    void testInit() {

        assertEquals(3, ch1.aretes().size());
        assertEquals(3, ch2.aretes().size());
        assertEquals(3, ch3.aretes().size());
        assertEquals(3, ch4.aretes().size());
        assertEquals(0, ch5.aretes().size());

    }

    @Test
    void testSplitByModalite() {
        assertEquals(1, CheminImpl.splitByModalite(ch1).size());
        assertEquals(2, CheminImpl.splitByModalite(ch2).size());
        assertEquals(2, CheminImpl.splitByModalite(ch3).size());
        assertEquals(3, CheminImpl.splitByModalite(ch4).size());
        assertEquals(0, CheminImpl.splitByModalite(ch5).size());

    }

    @Test
    void testGetNbChangement() {
        assertEquals(0, CheminImpl.getNbChangement(ch1));
        assertEquals(1, CheminImpl.getNbChangement(ch2));
        assertEquals(1, CheminImpl.getNbChangement(ch3));
        assertEquals(2, CheminImpl.getNbChangement(ch4));
        assertEquals(0, CheminImpl.getNbChangement(ch5));
    }

    @Test
    void testGetCHangementDuration() {
        assertEquals(Plateforme.TEMP_CHANGEMENT * 0, CheminImpl.getCHangementDuration(ch1));
        assertEquals(Plateforme.TEMP_CHANGEMENT * 1, CheminImpl.getCHangementDuration(ch2));
        assertEquals(Plateforme.TEMP_CHANGEMENT * 1, CheminImpl.getCHangementDuration(ch3));
        assertEquals(Plateforme.TEMP_CHANGEMENT * 2, CheminImpl.getCHangementDuration(ch4));
        assertEquals(Plateforme.TEMP_CHANGEMENT * 0, CheminImpl.getCHangementDuration(ch5));
    }

}
