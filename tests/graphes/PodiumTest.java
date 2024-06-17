package tests.graphes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.*;
import src.exception.CheminInexistantException;
import src.PlateformeCorrespondance;
import src.Podium;
import src.TypeCout;
import src.Tools;
import src.ToolsCorrespondance;

public class PodiumTest {

    private Podium<TypeCout> p;

    @BeforeEach
    public void avantTest() {
        p = new Podium<>();
        p.setFirst(TypeCout.CO2);
        p.setSecond(TypeCout.PRIX);
        p.setThird(TypeCout.TEMPS);
    }

    @Test
    void testSetFirst() {
        p.setFirst(TypeCout.CO2);
        assertEquals(TypeCout.CO2, p.getFirst());
    }

    @Test
    void testSetSecond() {
        p.setSecond(TypeCout.PRIX);
        assertEquals(TypeCout.PRIX, p.getSecond());
    }

    @Test
    void testSetThird() {
        p.setThird(TypeCout.TEMPS);
        assertEquals(TypeCout.TEMPS, p.getThird());
    }

    @Test
    void testGetFirst() {
        assertEquals(TypeCout.CO2, p.getFirst());
    }

    @Test
    void testGetSecond() {
        assertEquals(TypeCout.PRIX, p.getSecond());
    }

    @Test
    void testGetThird() {
        assertEquals(TypeCout.TEMPS, p.getThird());
    }

    @Test
    void testGetAllString() {
        List<String> list = new ArrayList<>();
        list.add(TypeCout.CO2.toString());
        list.add(TypeCout.PRIX.toString());
        list.add(TypeCout.TEMPS.toString());
        assertEquals(list, p.getAllString());
    }


    

    
}
