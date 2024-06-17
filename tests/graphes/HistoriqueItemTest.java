package tests.graphes;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.exception.CheminInexistantException;
import src.CheminImpl;
import src.HistoriqueItem;
import src.IhmInterfaceImpl;
import src.LieuImpl;
import src.PlateformeCorrespondance;
import src.Podium;
import src.ToolsCorrespondance;
import src.TranconImpl;
import src.TypeCout;
import src.VoyageurCorrespondance;



public class HistoriqueItemTest {
    

    @BeforeEach
    public void avantTest() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);

    }

    @Test
    void testGetChe() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        assertEquals("test", h.getChe());
    }

    @Test
    void testSetChe() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        h.setChe("test2");
        assertEquals("test2", h.getChe());
    }

    @Test
    void testGetPrix() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        assertEquals(10.0, h.getPrix());
    }

    @Test
    void testSetPrix() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        h.setPrix(20.0);
        assertEquals(20.0, h.getPrix());
    }

    @Test
    void testGetPollution() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        assertEquals(10.0, h.getPollution());
    }

    @Test
    void testSetPollution() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        h.setPollution(20.0);
        assertEquals(20.0, h.getPollution());
    }


    @Test
    void testGetTemps() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        assertEquals(10, h.getTemps());
    }

    @Test
    void testSetTemps() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        h.setTemps(20);
        assertEquals(20, h.getTemps());
    }

    @Test
    void testGetDate() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        assertNotNull(h.getDate());
    }

    @Test
    void testSetDate() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        h.setDate(null);
        assertNull(h.getDate());
    }

    @Test
    void saveExist() {
        File f = new File(HistoriqueItem.FILENAME);
        if (f.exists()) {
            assertTrue(HistoriqueItem.saveExists());
        }
        else {
            assertFalse(HistoriqueItem.saveExists());
        }
    }

    @Test
    void saveAndLoadTest()  {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        List<HistoriqueItem> historique = new ArrayList<>();
        historique.add(h);
        try {
            HistoriqueItem.save(historique);
        } catch (Exception e) {
            fail("Exception inattendue");
        }
        try {
            List<HistoriqueItem> historique2 = HistoriqueItem.load();
            assertEquals(historique, historique2);
        } catch (Exception e) {
            fail("Exception inattendue");
        }
    }

    @Test
    void equalsTest() {
        HistoriqueItem h = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        HistoriqueItem h2 = new HistoriqueItem("test", 10.0, 10.0, 10.0);
        assertTrue(h.equals(h2));
    }
}
