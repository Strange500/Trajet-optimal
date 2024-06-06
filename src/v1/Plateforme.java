package src.v1;

import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import fr.ulille.but.sae_s2_2024.Trancon;


/*
 * c'est un graphe qui contient 3 graphes, chacun se basant sur un critere different
 */
public class Plateforme {
    public static int TEMP_CHANGEMENT = 10;
    public static String ALPHA = "ALPHA";
    public static String OMEGA = "OMEGA";
    
    protected MultiGrapheOrienteValue g1;
    protected MultiGrapheOrienteValue g2;
    protected MultiGrapheOrienteValue g3;

    /**
     * @constructor Plateforme
     * Crée une plateforme vide
     */
    public Plateforme() {
        g1 = new MultiGrapheOrienteValue();
        g2 = new MultiGrapheOrienteValue();
        g3 = new MultiGrapheOrienteValue();
    }

    /**
     * @param chemin un chemin à évaluer
     * @param critere le critère à utiliser (CO2, PRIX, TEMPS)
     * @return le poids d'un chemin en fonction d'un critère
     */
    public double getPoidsByTypeCout(Chemin chemin, TypeCout critere) {
        double poids = 0;
        for (Trancon t : chemin.aretes()) {
            if (t.getDepart().toString().equals(ALPHA) || t.getArrivee().toString().equals(OMEGA)) {
                continue;
            }
            switch (critere) {
                case PRIX:
                    poids += this.g1.getPoidsArete(t);
                    break;
                case CO2:
                    poids += this.g2.getPoidsArete(t);
                    break;
                case TEMPS:
                    poids += this.g3.getPoidsArete(t);
                    break;
            }
        }
        return poids;
        
    }


    // private Trancon findTrancon(String depart, String arrivee, ModaliteTransport modalite) {
    //     for (Trancon t : this.g1.aretes()) {
    //         if (t.getDepart().toString().equals(depart) && t.getArrivee().toString().equals(arrivee) && t.getModalite() == modalite) {
    //             return t;
    //         }
    //     }
    //     return null;
    // }

    /**
     * @param depart le lieu de départ
     * @param arrivee le lieu d'arrivée
     * @param modalite le mode de transport
     * @return vrai si un chemin existe entre deux lieux avec un mode de transport donné
     */
    public boolean hasPathByModalite(String depart, String arrivee, ModaliteTransport modalite) {
        Plateforme g = this.clone();
        g.filterByModality(modalite);
        Lieu dep = this.getSommet(depart);
        Lieu arr = this.getSommet(arrivee);
        return AlgorithmeKPCC.kpcc(g.g1, dep, arr, 1).size() > 0;
    }

    /**
     * @param dep le lieu de départ
     * @param arr le lieu d'arrivée
     * @param modalite le mode de transport
     * @param critere le critère à utiliser (CO2, PRIX, TEMPS)
     * @param nbChemin le nombre de chemins à retourner
     * @return les chemins entre deux lieux avec un mode de transport donné et un critère donné
     */
    public List<Chemin> getPathByModaliteAndTypeCout(String dep, String arr, ModaliteTransport modalite, TypeCout critere, int nbChemin) {
        Plateforme g = this.clone();
        g.filterByModality(modalite);
        return g.getPathByTypeCout(dep, arr, critere, nbChemin);
    }

    /**
     * @param dep le lieu de départ
     * @param arr le lieu d'arrivée
     * @param critere le critère à utiliser (CO2, PRIX, TEMPS)
     * @param nbChemin le nombre de chemins à retourner
     * @return les chemins entre deux lieux avec un critère donné
     */
    public List<Chemin> getPathByTypeCout(String dep, String arr,  TypeCout critere, int nbChemin) {
        Plateforme g = this.clone();
        //Lieu depart = this.getSommet(dep);
        //Lieu arrivee = this.getSommet(arr);

        g.ajouterArrete(ALPHA, dep, ModaliteTransport.TRAIN, 0, 0, 0);
        g.ajouterArrete(arr, OMEGA, ModaliteTransport.TRAIN, 0, 0, 0);

        Lieu alpha = g.getSommet(ALPHA);
        Lieu omega = g.getSommet(OMEGA);


        switch (critere) {
            case PRIX:
                return AlgorithmeKPCC.kpcc(g.g1, alpha, omega, nbChemin);
            case CO2:
                return AlgorithmeKPCC.kpcc(g.g2, alpha, omega, nbChemin);
            case TEMPS:
                return AlgorithmeKPCC.kpcc(g.g3, alpha, omega, nbChemin);
        }
        return new ArrayList<Chemin>();
    }

    /**
     * @return une copie de la plateforme
     */
    public Plateforme clone() {
        Plateforme g = new Plateforme();
        g.g1 = clone(g1);
        g.g2 = clone(g2);
        g.g3 = clone(g3);
        return g;
    }

    /**
     * @param mod le mode de transport à filtrer
     */
    public void filterByModality(ModaliteTransport mod) {
        this.g1 = filterByModality(this.g1, mod);
        this.g2 = filterByModality(this.g2, mod);
        this.g3 = filterByModality(this.g3, mod);
    }

    /**
     * @param g le graphe à filtrer
     * @param mod le mode de transport à filtrer
     * @return un graphe filtré
     */
    private static MultiGrapheOrienteValue filterByModality(MultiGrapheOrienteValue g, ModaliteTransport mod) {
        g = clone(g, mod);
        return g;
    }

    /**
     * @param graph le graphe à cloner
     * @return une copie du graphe
     */
    public static MultiGrapheOrienteValue clone(MultiGrapheOrienteValue graph) {
        MultiGrapheOrienteValue g = new MultiGrapheOrienteValue();
        for (Lieu l : graph.sommets()) {
            g.ajouterSommet(l);
        }
        for (Trancon t : graph.aretes()) {
            g.ajouterArete(t, graph.getPoidsArete(t));
        }
        return g;
    }

    /**
     * @param graph le graphe à cloner
     * @param mod le mode de transport à filtrer
     * @return une copie du graphe filtrée
     */
    public static MultiGrapheOrienteValue clone(MultiGrapheOrienteValue graph, ModaliteTransport mod) {
        MultiGrapheOrienteValue g = new MultiGrapheOrienteValue();
        for (Lieu l : graph.sommets()) {
            g.ajouterSommet(l);
        }
        for (Trancon t : graph.aretes()) {
            if (t.getModalite() == mod) {   
                g.ajouterArete(t, graph.getPoidsArete(t));
            }
        }
        return g;
    }

    /**
     * @return la liste des lieux de la plateforme
     */
    public List<Lieu> getLieux() {
        List<Lieu> lieux = new ArrayList<Lieu>();
        for (Lieu l : g1.sommets()) {
            lieux.add(l);
        }
        return lieux;
    }

    /**
     * @param avoid le lieu à éviter
     * @return la liste des lieux de la plateforme sans le lieu à éviter
     */
    public List<Lieu> getLieux(Lieu avoid) {
        List<Lieu> lieux = this.getLieux();
        for (Lieu l : lieux) {
            if (l.toString().equals(avoid.toString())) {
                lieux.remove(l);
                break;
            }
        }
        return lieux;
    }

    /**
     * @return le graphe g1
     */
    public MultiGrapheOrienteValue getG1() {
        return g1;
    }

    /**
     * @return le graphe g2
     */
    public MultiGrapheOrienteValue getG2() {
        return g2;
    }

    /**
     * @return le graphe g3
     */
    public MultiGrapheOrienteValue getG3() {
        return g3;
    }

    /**
     * @param nom le nom du lieu à ajouter
     * @return vrai si le lieu a été ajouté
     */
    public boolean ajouterLieux(String nom) {
        Lieu l = new LieuImpl(nom);
        return Plateforme.ajouterLieux(g1, l) && Plateforme.ajouterLieux(g2, l) && Plateforme.ajouterLieux(g3, l);
    }

    /**
     * @param g le graphe à modifier
     * @param lieu le lieu à ajouter
     * @return vrai si le lieu a été ajouté, faux si le lieu est déjà présent
     */
    private static boolean ajouterLieux(MultiGrapheOrienteValue g, Lieu lieu) {
        if (Plateforme.contientLieux(g, lieu)) {
            return false;
        }
        g.ajouterSommet(lieu);
        return true;
    }

    /**
     * @param depart le lieu de départ
     * @param arrivee le lieu d'arrivée
     * @param modalite le mode de transport
     * @param prix le prix de l'arrête
     * @param pollution la pollution de l'arrête
     * @param duree la durée de l'arrête
     * @return vrai si l'arrête a été ajoutée
     */
    public boolean ajouterArrete(String depart, String arrivee, ModaliteTransport modalite, double prix, double pollution, double duree) {
        //Lieu lDepart = new LieuImpl(depart);
        //Lieu lArrivee = new LieuImpl(arrivee);
        this.ajouterLieux(arrivee);
        this.ajouterLieux(depart);
        Lieu g1Depart = this.getSommet(depart);
        Lieu g1Arrivee = this.getSommet(arrivee);
        Trancon t = new TranconImpl(g1Depart, g1Arrivee, modalite);
        return this.ajouterArreteGraph(g1, t, prix) && this.ajouterArreteGraph(g2, t, pollution) && this.ajouterArreteGraph(g3, t, duree);
    }

    /**
     * @param nom le nom du sommet
     * @return le sommet correspondant au nom
     */
    public Lieu getSommet(String nom) {
        for (Lieu l : g1.sommets()) {
            if (l.toString().equals(nom)) {
                return l;
            }
        }
        return null;
    }

    /**
     * @param g le graphe à modifier
     * @param trancon l'arrête à ajouter
     * @param value la valeur de l'arrête
     * @return vrai si l'arrête a été ajoutée
     */
    private boolean ajouterArreteGraph(MultiGrapheOrienteValue g, Trancon trancon, double value) {      
        if (g.aretes().contains(trancon)) {
            return false;
        }
        g.ajouterArete(trancon, value);
        return true;
    }

    /**
     * @param g le graphe à modifier
     * @param lieu le lieu à chercher (sous forme de chaîne de caractères)
     * @return vrai si le graphe contient le lieu
     */
    public static boolean contientLieux(MultiGrapheOrienteValue g, String lieu) {
        for (Lieu l : g.sommets()) {
            if (l.toString().equals(lieu)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param g le graphe à modifier
     * @param lieu le lieu à chercher (sous forme de Lieu)
     * @return vrai si le graphe contient le lieu
     */
    public static boolean contientLieux(MultiGrapheOrienteValue g, Lieu lieu) {
        for (Lieu l : g.sommets()) {
            if (l.toString().equals(lieu.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return une chaîne de caractères représentant la plateforme
     */
    public String toString() {
        String r = "";

        Object[] g1List = this.g1.aretes().toArray();
        //Object[] g2List = this.g2.aretes().toArray();
        //Object[] g3List = this.g3.aretes().toArray();

        for (int j = 0; j < g1List.length; j++){
            r+= ((Trancon) g1List[j]).getModalite().name() +" ";
            r+= ((Trancon) g1List[j]).getDepart() + " -> " + ((Trancon) g1List[j]).getArrivee() + " (" ;
            r+= this.g1.getPoidsArete(((Trancon) g1List[j])) + ",";
            r+= this.g2.getPoidsArete(((Trancon) g1List[j]))+"," ;
            r+= this.g3.getPoidsArete(((Trancon) g1List[j])) + ")\n";

        }
        return r;
    }
}
