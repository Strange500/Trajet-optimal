package src.v1;

import java.util.List;

import fr.ulille.but.sae_s2_2024.*;

public class Voyageur {


    private static final String[] DATA = new String[]{
        "villeA;villeB;Train;60;1.7;80",
        "villeB;villeD;Train;22;2.4;40",
        "villeA;villeC;Train;42;1.4;50",
        "villeB;villeC;Train;14;1.4;60",
        "villeC;villeD;Avion;110;150;22",
        "villeC;villeD;Train;65;1.2;90"
    };
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

    public Voyageur(String nom, String prenom, TypeCout critere, ModaliteTransport modalite, int thresholdPrix, double thresholdCO2, int thresholdTemps, String depart, String arrivee, int nb_trajet) {
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



    public Voyageur(String nom, String prenom, TypeCout critere, ModaliteTransport modalite) {
        this(nom, prenom, critere, modalite, DEFAULT_THRESHOLD_PRIX, DEFAULT_THRESHOLD_CO2, DEFAULT_THRESHOLD_TEMPS, DEFAULT_DEPART, DEFAULT_ARRIVEE, DEFAULT_NB_TRAJET);
    }

    public Voyageur(String nom, String prenom, TypeCout critere) {
        this(nom, prenom, critere, DEFAULT_MODALITE);
    }

    

    public String getNom() {
        return nom;
    }



    public String getPrenom() {
        return prenom;
    }



    public TypeCout getCritere() {
        return critere;
    }



    public ModaliteTransport getModalite() {
        return modalite;
    }



    public int getThresholdPrix() {
        return thresholdPrix;
    }



    public double getThresholdCO2() {
        return thresholdCO2;
    }



    public int getThresholdTemps() {
        return thresholdTemps;
    }



    public String getDepart() {
        return depart;
    }



    public String getArrivee() {
        return arrivee;
    }



    public int getNb_trajet() {
        return nb_trajet;
    }



    public List<Chemin> computeBestPath() {
        if (Tools.donneesValides(DATA)) {
            Plateforme g = Tools.initPlateforme(DATA);

            if (g.hasPathByModalite(this.depart, this.arrivee, this.modalite)) {
                List<Chemin> chemins = g.getPathByModaliteAndTypeCout(this.depart, this.arrivee, this.modalite, this.critere, nb_trajet);
                for (TypeCout c : new TypeCout[]{TypeCout.PRIX, TypeCout.CO2, TypeCout.TEMPS}) {
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
                return chemins;
            }
            else {
                System.out.println("Aucun chemin trouvé pour les critères demandés");
                return null;
            }
        }
        else {
            System.out.println("Données invalides");
            return null;
        }

    }

    public static void main(String[] args) {
        Voyageur u = new Voyageur("Doe", "John", TypeCout.PRIX, ModaliteTransport.TRAIN, 1000, 1000, 1000, "villeA", "villeD", 3);
        List<Chemin> chemins = u.computeBestPath();
        if (chemins != null) {
            System.out.println("Les trajets recommandés de " + u.depart + " à " + u.arrivee + " sont:");
            for (int i = 0; i < chemins.size(); i++) {
                System.out.println(i + 1 + ") " + Tools.cheminWithCorre(chemins.get(i), u.critere));
            }
        }
    }

}
