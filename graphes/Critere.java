package graphes;

public enum Critere {
    PRIX, POLLUTION, DUREE;

    public static String getUnit(Critere critere) {
        switch (critere) {
            case PRIX:
                return "€";
            case POLLUTION:
                return "kgCO2";
            case DUREE:
                return "min";
        }
        return "";
    }
}
