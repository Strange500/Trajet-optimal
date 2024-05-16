package src.v1;

public enum TypeCout {
    PRIX, CO2, TEMPS;

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
