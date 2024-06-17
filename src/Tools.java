package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Lieu;

/**
 * Classe contenant des outils pour l'application
 */
public class Tools {
    protected static final String SEPARATOR = ";";

    protected static final int DEPART_IDX = 0;
    protected static final int DESTINATION_IDX = 1;
    protected static final int MODALITE_IDX = 2;
    protected static final int PRIX_IDX = 3;
    protected static final int CO2_IDX = 4;
    protected static final int TEMPS_IDX = 5;

    /**
     * Initialise la plateforme avec les données passées en paramètre
     * 
     * @param args Données
     * @return Plateforme
     */
    public static Plateforme initPlateforme(String[] args) {
        Plateforme g = new Plateforme();
        for (String arg : args) {
            String[] elements = arg.split(SEPARATOR);

            String depart = elements[DEPART_IDX];
            String destination = elements[DESTINATION_IDX];
            ModaliteTransport modalite = ModaliteTransport.valueOf(elements[MODALITE_IDX].toUpperCase());
            double prix = Double.parseDouble(elements[PRIX_IDX]);
            double pollution = Double.parseDouble(elements[CO2_IDX]);
            int Duree = Integer.parseInt(elements[TEMPS_IDX]);

            g.ajouterArrete(depart, destination, Map.of(TypeCout.PRIX, (double) prix,TypeCout.CO2, (double) pollution,TypeCout.TEMPS, (double) Duree), modalite);
            g.ajouterArrete(destination, depart, Map.of(TypeCout.PRIX, (double) prix,TypeCout.CO2, (double) pollution,TypeCout.TEMPS, (double) Duree), modalite);
        }
        return g;

    }

    /**
     * Vérifie si une chaîne de caractères est un nombre
     * 
     * @param chaine Chaîne de caractères
     * @return Vrai si la chaîne est un nombre, faux sinon
     */
    public static boolean estNombre(String chaine) {
        try {
            Double.parseDouble(chaine);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Vérifie si les données passées en paramètre sont valides
     * 
     * @param args Données
     * @return Vrai si les données sont valides, faux sinon
     */
    public static boolean donneesValides(String[] args) {
        if (args.length == 0) {
            return false;
        }
        for (String arg : args) {
            String[] elements = arg.split(";");
            if (elements.length != 6 || !estNombre(elements[3]) || !estNombre(elements[4]) || !estNombre(elements[5]) ||
                    elements[0].isEmpty() || elements[1].isEmpty() || elements[2].isEmpty() || elements[3].isEmpty()
                    || elements[4].isEmpty() || elements[5].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Applique un seuil sur les chemins
     * 
     * @param g         Plateforme
     * @param chemins   Chemins
     * @param critere   Critère
     * @param threshold Seuil
     */
    public static void applyThreshold(Plateforme g, List<Chemin> chemins, TypeCout critere, int threshold) {
        List<Chemin> toRemove = new ArrayList<>();
        for (Chemin che : chemins) {
            double poidsByTypeCout = g.getPoidsByTypeCout(che, critere);
            if (poidsByTypeCout > threshold) {
                toRemove.add(che);

            }
        }
        chemins.removeAll(toRemove);

    }

    /**
     * Applique un seuil sur les chemins
     * 
     * @param g         Plateforme
     * @param chemins   Chemins
     * @param critere   Critère
     * @param threshold Seuil
     */
    public static void applyThreshold(Plateforme g, List<Chemin> chemins, TypeCout critere, double threshold) {
        List<Chemin> toRemove = new ArrayList<>();
        for (Chemin che : chemins) {
            double poidsByTypeCout = g.getPoidsByTypeCout(che, critere);
            if (poidsByTypeCout > threshold) {
                toRemove.add(che);
            }
        }
        chemins.removeAll(toRemove);

    }

    /**
     * Retourne une chaîne de caractères représentant un chemin
     * 
     * @param che     Chemin
     * @param critere Critère
     * @return Chaîne de caractères
     */
    public static String cheminWithCorre(Chemin che, TypeCout critere) {
        String r = "";
        // on enleve les arrete vers alpha et omega
        che.aretes().remove(0);
        che.aretes().remove(che.aretes().size() - 1);

        for (Chemin cheModal : CheminImpl.splitByModalite(che)) {
            if (!r.isEmpty()) {
                r += " puis ";
            }
            r += cheModal.aretes().get(0).getModalite() + " de " +
                    cheModal.aretes().get(0).getDepart() + " à "
                    + cheModal.aretes().get(cheModal.aretes().size() - 1).getArrivee() + " ";
            if (cheModal.aretes().size() > 1) {
                r += "en passant par";
                for (int i = 1; i < cheModal.aretes().size(); i++) {
                    r += " " + cheModal.aretes().get(i).getDepart() + "";
                    if (i < cheModal.aretes().size() - 1) {
                        r += ",";
                    }

                }
            }

        }
        return r + " totale: " + che.poids() + " " + TypeCout.getUnit(critere);
    }
}
