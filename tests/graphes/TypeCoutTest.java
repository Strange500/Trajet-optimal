package tests.graphes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import src.TypeCout;

public class TypeCoutTest {

    @Test
    void testGetUnit() {

        assertEquals("€", TypeCout.getUnit(TypeCout.PRIX));
        assertEquals("min", TypeCout.getUnit(TypeCout.TEMPS));
        assertEquals("kgCO2", TypeCout.getUnit(TypeCout.CO2));

    }

}
