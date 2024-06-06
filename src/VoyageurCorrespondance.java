package src;

import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.*;
import src.exception.CheminInexistantException;

public class VoyageurCorrespondance extends Voyageur {

    public static final String path_default = "csv/data.csv";
    public static final String path_cor_default = "csv/correspondance.csv";

    private final ArrayList<String> DATA;
    private final ArrayList<String> CORRESPONDANCE;

    private static final int DEFAULT_THRESHOLD_PRIX = Integer.MAX_VALUE;
    private static final double DEFAULT_THRESHOLD_CO2 = Double.MAX_VALUE;
    private static final int DEFAULT_THRESHOLD_TEMPS = Integer.MAX_VALUE;

    private static final ModaliteTransport DEFAULT_MODALITE = ModaliteTransport.TRAIN;

    private static final String DEFAULT_DEPART = "villeA";
    private static final String DEFAULT_ARRIVEE = "villeD";

    private static final int DEFAULT_NB_TRAJET = 1;

    public VoyageurCorrespondance(String nom, String prenom, TypeCout critere, ModaliteTransport modalite,
            int thresholdPrix,
            double thresholdCO2, int thresholdTemps, String depart, String arrivee, int nb_trajet, String dataPath,
            String corPath) {
        super(nom, prenom, critere, modalite, thresholdPrix, thresholdCO2, thresholdTemps, depart, arrivee, nb_trajet);
        this.DATA = ToolsCorrespondance.getCSV(dataPath);
        this.CORRESPONDANCE = ToolsCorrespondance.getCSV(corPath);
    }

    public VoyageurCorrespondance(String nom, TypeCout critere) {
        this(nom, "", critere, null, DEFAULT_THRESHOLD_PRIX, DEFAULT_THRESHOLD_CO2, DEFAULT_THRESHOLD_TEMPS,
                DEFAULT_DEPART, DEFAULT_ARRIVEE, DEFAULT_NB_TRAJET, path_default, path_cor_default);
    }

    public ArrayList<String> getDATA() {
        return DATA;
    }

    public ArrayList<String> getCORRESPONDANCE() {
        return CORRESPONDANCE;
    }

    /**
     * @return Liste des chemins recommandés
     */
    public List<Chemin> computeBestPathTrigger() throws CheminInexistantException {
        if (ToolsCorrespondance.donneesValides(this.DATA)) {
            PlateformeCorrespondance g = ToolsCorrespondance.initPlateforme(this.DATA, this.CORRESPONDANCE);
            // System.out.println(g.get);
            if (g.hasPathByModalite(this.depart, this.arrivee, this.modalite)) {

                List<Chemin> chemins = g.getPathByModaliteAndTypeCout(this.depart, this.arrivee, this.modalite,
                        this.critere, this.nb_trajet * 100);
                chemins = ToolsCorrespondance.removeDuplicates(chemins, this.nb_trajet);
                for (TypeCout c : new TypeCout[] { TypeCout.PRIX, TypeCout.CO2, TypeCout.TEMPS }) {
                    switch (c) {
                        case PRIX:
                            Tools.applyThreshold(g, chemins, c, this.thresholdPrix);
                            break;
                        case CO2:
                            Tools.applyThreshold(g, chemins, c, this.thresholdCO2);
                            break;
                        case TEMPS:
                            Tools.applyThreshold(g, chemins, c, this.thresholdTemps);
                            break;
                    }
                }
                if (chemins.isEmpty()) {
                    throw new CheminInexistantException();
                }
                return chemins;
            } else {

                throw new CheminInexistantException();
            }
        } else {
            System.out.println("Données invalides");
            return null;
        }

    }

    public static void main(String[] args) {
        VoyageurCorrespondance u = new VoyageurCorrespondance("Doe", "John", TypeCout.TEMPS, null, 1000, 1000, 1000,
                "Paris", "Lille", 3, path_default, path_cor_default);
        List<Chemin> chemins = null;
        try {
            chemins = u.computeBestPathTrigger();
        } catch (CheminInexistantException e) {
            chemins = null;
        } finally {
            if (chemins != null) {
                System.out.println("Les trajets recommandés de " + u.depart + " à " + u.arrivee + " sont :");
                for (int i = 0; i < chemins.size(); i++) {
                    System.out
                            .println(i + 1 + ") " + ToolsCorrespondance.cheminWithCorreBis(chemins.get(i), u.critere));
                }
            } else {
                System.out.println("Aucun chemin trouvé pour les critères demandés");
            }
        }
    }
}
