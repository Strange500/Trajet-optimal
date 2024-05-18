package src.v1;

/**
 * Enumération des types de coûts
 */
public enum TypeCout {
    PRIX, CO2, TEMPS;

    /**
     * @param critere Type de coût
     * @return Unité du coût sous forme de chaîne de caractères
     */
    public static String getUnit(TypeCout critere) {
        switch (critere) {
            case PRIX:
                return "€";
            case CO2:
                return "kgCO2";
            case TEMPS:
                return "min";
        }
        return "";
    }
}
