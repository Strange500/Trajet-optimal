package src.v2;

import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.*;
import src.v2.exception.CheminInexistantException;

/**
 * Classe représentant un voyageur
 */
public class Voyageur {

    public static final String path = "src/v2/csv/data.csv";
    public static final String path2 = "src/v2/csv/correspondance.csv";

    private static final ArrayList<String> DATA = Tools.getCSV(path);
    private static final ArrayList<String> CORRESPONDANCE = Tools.getCSV(path2);
    private static final int DEFAULT_THRESHOLD_PRIX = Integer.MAX_VALUE;
    private static final double DEFAULT_THRESHOLD_CO2 = Double.MAX_VALUE;
    private static final int DEFAULT_THRESHOLD_TEMPS = Integer.MAX_VALUE;

    private static final ModaliteTransport DEFAULT_MODALITE = ModaliteTransport.TRAIN;

    private static final String DEFAULT_DEPART = "villeA";
    private static final String DEFAULT_ARRIVEE = "villeD";

    private static final int DEFAULT_NB_TRAJET = 1;

    private String nom;
    private String prenom;
    private TypeCout critere;
    private ModaliteTransport modalite;
    private int thresholdPrix;
    private double thresholdCO2;
    private int thresholdTemps;
    private String depart;
    private String arrivee;
    private int nb_trajet;
    private boolean needCorrespondance = true;

    /**
     * @constructor Voyageur
     * @param nom            Nom du voyageur
     * @param prenom         Prénom du voyageur
     * @param critere        Critère de choix
     * @param modalite       Modalité de transport
     * @param thresholdPrix  Seuil de prix
     * @param thresholdCO2   Seuil de CO2
     * @param thresholdTemps Seuil de temps
     * @param depart         Lieu de départ
     * @param arrivee        Lieu d'arrivée
     * @param nb_trajet      Nombre de trajets à recommander
     *                       Crée un voyageur avec des critères de choix
     */
    public Voyageur(String nom, String prenom, TypeCout critere, ModaliteTransport modalite, int thresholdPrix,
            double thresholdCO2, int thresholdTemps, String depart, String arrivee, int nb_trajet) {
        this.nom = nom;
        this.prenom = prenom;
        this.critere = critere;
        this.modalite = modalite;
        this.thresholdPrix = thresholdPrix;
        this.thresholdCO2 = thresholdCO2;
        this.thresholdTemps = thresholdTemps;
        this.depart = depart;
        this.arrivee = arrivee;
        this.nb_trajet = nb_trajet;
    }

    /**
     * @constructor Voyageur
     * @param nom      Nom du voyageur
     * @param prenom   Prénom du voyageur
     * @param critere  Critère de choix
     * @param modalite Modalité de transport
     *                 Crée un voyageur avec des critères de choix par défaut
     */
    public Voyageur(String nom, String prenom, TypeCout critere, ModaliteTransport modalite) {
        this(nom, prenom, critere, modalite, DEFAULT_THRESHOLD_PRIX, DEFAULT_THRESHOLD_CO2, DEFAULT_THRESHOLD_TEMPS,
                DEFAULT_DEPART, DEFAULT_ARRIVEE, DEFAULT_NB_TRAJET);
    }

    /**
     * @constructor Voyageur
     * @param nom     Nom du voyageur
     * @param prenom  Prénom du voyageur
     * @param critere Critère de choix
     *                Crée un voyageur avec des critères de choix par défaut
     */
    public Voyageur(String nom, String prenom, TypeCout critere) {
        this(nom, prenom, critere, DEFAULT_MODALITE);
    }

    public void showCorrespondance() {
        this.needCorrespondance = true;
    }

    public void hideCorrespondance() {
        this.needCorrespondance = false;
    }

    /**
     * @return Nom du voyageur
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return Prénom du voyageur
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return Critère de choix
     */
    public TypeCout getCritere() {
        return critere;
    }

    /**
     * @return Modalité de transport
     */
    public ModaliteTransport getModalite() {
        return modalite;
    }

    /**
     * @return Seuil de prix
     */
    public int getThresholdPrix() {
        return thresholdPrix;
    }

    /**
     * @return Seuil de CO2
     */
    public double getThresholdCO2() {
        return thresholdCO2;
    }

    /**
     * @return Seuil de temps
     */
    public int getThresholdTemps() {
        return thresholdTemps;
    }

    /**
     * @return Lieu de départ
     */
    public String getDepart() {
        return depart;
    }

    /**
     * @return Lieu d'arrivée
     */
    public String getArrivee() {
        return arrivee;
    }

    /**
     * @return Nombre de trajets à recommander
     */
    public int getNb_trajet() {
        return nb_trajet;
    }

    /**
     * @return Liste des chemins recommandés
     */
    public List<Chemin> computeBestPath() throws CheminInexistantException {
        if (Tools.donneesValides(DATA)) {
            Plateforme g = Tools.initPlateforme(DATA, CORRESPONDANCE);
            // System.out.println(g.get);
            if (g.hasPathByModalite(this.depart, this.arrivee, this.modalite)) {

                List<Chemin> chemins = g.getPathByModaliteAndTypeCout(this.depart, this.arrivee, this.modalite,
                        this.critere, this.nb_trajet * 100);
                chemins = Tools.removeDuplicates(chemins, this.nb_trajet);
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
                if (chemins.size() == 0) {
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
        Voyageur u = new Voyageur("Doe", "John", TypeCout.TEMPS, null, 1000, 1000, 1000, "Paris", "Lille", 20);
        List<Chemin> chemins = null;
        try {
            chemins = u.computeBestPath();
        } catch (CheminInexistantException e) {
            chemins = null;
        } finally {
            if (chemins != null) {
                System.out.println("Les trajets recommandés de " + u.depart + " à " + u.arrivee + " sont :");
                for (int i = 0; i < chemins.size(); i++) {
                    System.out.println(i + 1 + ") " + Tools.cheminWithCorreBis(chemins.get(i), u.critere));
                }
            } else {
                System.out.println("Aucun chemin trouvé pour les critères demandés");
            }
        }
    }

    // public static void main(String[] args) {
    // Plateforme g = Tools.initPlateforme(DATA, Tools.getCSV(path2));
    // System.out.println(g);
    // }
}
