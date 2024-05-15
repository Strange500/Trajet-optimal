package graphes;

import java.util.ArrayList;
import java.util.Arrays;
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
public class Graphes {
    public static int TEMP_CHANGEMENT = 10;
    public static String ALPHA = "ALPHA";
    public static String OMEGA = "OMEGA";
    
    private MultiGrapheOrienteValue g1;
    private MultiGrapheOrienteValue g2;
    private MultiGrapheOrienteValue g3;

    public Graphes() {
        g1 = new MultiGrapheOrienteValue();
        g2 = new MultiGrapheOrienteValue();
        g3 = new MultiGrapheOrienteValue();
    }

    public  double getPoidsByCritere(Chemin chemin, Critere critere) {
        double poids = 0;
        for (Trancon t : chemin.aretes()) {
            if (t.getDepart().toString().equals(ALPHA) || t.getArrivee().toString().equals(OMEGA)) {
                continue;
            }
            switch (critere) {
                case PRIX:
                    poids += this.g1.getPoidsArete(t);
                    break;
                case POLLUTION:
                    poids += this.g2.getPoidsArete(t);
                    break;
                case DUREE:
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


    public boolean hasPathByModalite(String depart, String arrivee, ModaliteTransport modalite) {
        Graphes g = this.clone();
        g.filterByModality(modalite);
        Lieu dep = this.getSommet(depart);
        Lieu arr = this.getSommet(arrivee);
        return AlgorithmeKPCC.kpcc(g.g1, dep, arr, 1).size() > 0;
    }

    public List<Chemin> getPathByModaliteAndCritere(String dep, String arr, ModaliteTransport modalite, Critere critere, int nbChemin) {
        Graphes g = this.clone();
        g.filterByModality(modalite);
        return g.getPathByCritere(dep, arr, critere, nbChemin);
    }

    public List<Chemin> getPathByCritere(String dep, String arr,  Critere critere, int nbChemin) {
        Graphes g = this.clone();
        Lieu depart = this.getSommet(dep);
        Lieu arrivee = this.getSommet(arr);

        g.ajouterArrete(ALPHA, dep, ModaliteTransport.TRAIN, 0, 0, 0);
        g.ajouterArrete(arr, OMEGA, ModaliteTransport.TRAIN, 0, 0, 0);

        Lieu alpha = g.getSommet(ALPHA);
        Lieu omega = g.getSommet(OMEGA);


        switch (critere) {
            case PRIX:
                return AlgorithmeKPCC.kpcc(g.g1, alpha, omega, nbChemin);
            case POLLUTION:
                return AlgorithmeKPCC.kpcc(g.g2, alpha, omega, nbChemin);
            case DUREE:
                return AlgorithmeKPCC.kpcc(g.g3, alpha, omega, nbChemin);
        }
        return new ArrayList<Chemin>();
    }

    public Graphes clone() {
        Graphes g = new Graphes();
        g.g1 = clone(g1);
        g.g2 = clone(g2);
        g.g3 = clone(g3);
        return g;
    }

    private void filterByModality(ModaliteTransport mod) {
        this.g1 = filterByModality(this.g1, mod);
        this.g2 = filterByModality(this.g2, mod);
        this.g3 = filterByModality(this.g3, mod);
    }

    private static MultiGrapheOrienteValue filterByModality(MultiGrapheOrienteValue g, ModaliteTransport mod) {
        g = clone(g, mod);
        return g;
    }

    private static MultiGrapheOrienteValue clone(MultiGrapheOrienteValue graph) {
        MultiGrapheOrienteValue g = new MultiGrapheOrienteValue();
        for (Lieu l : graph.sommets()) {
            g.ajouterSommet(l);
        }
        for (Trancon t : graph.aretes()) {
            g.ajouterArete(t, graph.getPoidsArete(t));
        }
        return g;
    }

    private static MultiGrapheOrienteValue clone(MultiGrapheOrienteValue graph, ModaliteTransport mod) {
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

    public List<Lieu> getLieux() {
        List<Lieu> lieux = new ArrayList<Lieu>();
        for (Lieu l : g1.sommets()) {
            lieux.add(l);
        }
        return lieux;
    }

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





    public MultiGrapheOrienteValue getG1() {
        return g1;
    }

    public MultiGrapheOrienteValue getG2() {
        return g2;
    }

    public MultiGrapheOrienteValue getG3() {
        return g3;
    }

    public boolean ajouterLieux(String nom) {
        Lieu l = new ILieu(nom);
        return Graphes.ajouterLieux(g1, l) && Graphes.ajouterLieux(g2, l) && Graphes.ajouterLieux(g3, l);
    }

    private static boolean ajouterLieux(MultiGrapheOrienteValue g, Lieu lieu) {
        if (Graphes.contientLieux(g, lieu)) {
            return false;
        }
        g.ajouterSommet(lieu);
        return true;
    }

    public boolean ajouterArrete(String depart, String arrivee, ModaliteTransport modalite, double prix, double pollution, double duree) {
        Lieu lDepart = new ILieu(depart);
        Lieu lArrivee = new ILieu(arrivee);
        this.ajouterLieux(arrivee);
        this.ajouterLieux(depart);
        Lieu g1Depart = this.getSommet(depart);
        Lieu g1Arrivee = this.getSommet(arrivee);
        Trancon t = new ITrancon(g1Depart, g1Arrivee, modalite);
        return this.ajouterArreteGraph(g1, t, prix) && this.ajouterArreteGraph(g2, t, pollution) && this.ajouterArreteGraph(g3, t, duree);
    }

    private Lieu getSommet(String nom) {
        for (Lieu l : g1.sommets()) {
            if (l.toString().equals(nom)) {
                return l;
            }
        }
        return null;
    }
    private boolean ajouterArreteGraph(MultiGrapheOrienteValue g, Trancon trancon, double value) {      
        if (g.aretes().contains(trancon)) {
            return false;
        }
        g.ajouterArete(trancon, value);
        return true;
    }

    public static boolean contientLieux(MultiGrapheOrienteValue g, String lieu) {
        for (Lieu l : g.sommets()) {
            if (l.toString().equals(lieu)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contientLieux(MultiGrapheOrienteValue g, Lieu lieu) {
        for (Lieu l : g.sommets()) {
            if (l.toString().equals(lieu.toString())) {
                return true;
            }
        }
        return false;
    }


    public String toString() {
        String r = "";

        Object[] g1List = this.g1.aretes().toArray();
        Object[] g2List = this.g2.aretes().toArray();
        Object[] g3List = this.g3.aretes().toArray();

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
