package src.v1;

import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Trancon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implémentation de l'interface Chemin
 */
public class CheminImpl implements Chemin {
    private List<Trancon> aretes;
    private double poids;

    /**
     * @return la liste des trançons qui composent le chemin
     */
    public List<Trancon> aretes() {
        return aretes;
    }

    /**
     * @return le poids du chemin
     */
    public double poids() {
        return poids;
    }

    /**
     * @constructor CheminImpl
     * Crée un chemin vide
     */
    public CheminImpl() {
        aretes = new ArrayList<Trancon>();
        poids = 0;
    }

    /**
     * Divise un chemin en plusieurs chemins, chaque chemin contenant des trançons de la même modalité. 
     * @param chemin 
     * @return Une liste de chemins contenant des trançons de la même modalité
     */
    public static List<Chemin> splitByModalite(Chemin chemin) {
        if (chemin.aretes().size() == 0) {
            return new ArrayList<Chemin>();
        }
        List<Chemin> chemins = new ArrayList<Chemin>();
        Iterator<Trancon> it = chemin.aretes().iterator();
        Trancon tmpNext = it.next();
        Chemin tmp ;
        while (tmpNext!=null) {
            Trancon t= tmpNext;
            tmp = new CheminImpl();
            tmp.aretes().add(t);
            if (!it.hasNext()) {
                chemins.add(tmp);
                break;
            }
            tmpNext = it.next();
            while (tmpNext.getModalite() == t.getModalite()) {
                t = tmpNext;
                tmp.aretes().add(t);
                if (!it.hasNext()) {
                    tmpNext = null;
                    break;
                }
                tmpNext = it.next();
            }
            chemins.add(tmp);
        }
        return chemins;
        
    }

    /**
     * @param chemin
     * @return le nombre de changements dans un chemin
     */
    public static int getNbChangement(Chemin che) {
        if (che.aretes().size() == 0) {
            return 0;
        }
        return (CheminImpl.splitByModalite(che).size())-1;
    }

    /**
     * @param chemin
     * @return la durée des changements dans un chemin
     */
    public static int getCHangementDuration(Chemin che) {
        return CheminImpl.getNbChangement(che) * Plateforme.TEMP_CHANGEMENT;
    }


    /**
     * @param chemin
     * @return un chemin sous forme de chaine de caractères
     */
    public static String  toString(Chemin che) {
        String r = "";
        for (Trancon t : che.aretes()) {
            r += t.getModalite() + " de " + t.getDepart() + " à " + t.getArrivee() + " ";
        }
        return r;
    }
}
