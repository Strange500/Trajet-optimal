package src;

import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Trancon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implémentation de l'interface Chemin
 */
public class CheminImpl implements Chemin {
    private final List<Trancon> ARRETES;
    private final double POIDS;

    /**
     * @return la liste des trançons qui composent le chemin
     */
    public List<Trancon> aretes() {
        return ARRETES;
    }

    /**
     * @return le poids du chemin
     */
    public double poids() {
        return POIDS;
    }

    /**
     * @constructor CheminImpl
     *              Crée un chemin vide
     */
    public CheminImpl() {
        ARRETES = new ArrayList<Trancon>();
        POIDS = 0;
    }

    /**
     * Divise un chemin en plusieurs chemins, chaque chemin contenant des trançons
     * de la même modalité.
     * 
     * @param chemin
     * @return Une liste de chemins contenant des trançons de la même modalité
     */
    public static List<Chemin> splitByModalite(Chemin chemin) {
        if (chemin.aretes().isEmpty()) {
            return new ArrayList<Chemin>();
        }
        List<Chemin> chemins = new ArrayList<Chemin>();
        Iterator<Trancon> it = chemin.aretes().iterator();
        Trancon tmpNext = it.next();
        Chemin tmp;
        while (tmpNext != null) {
            Trancon t = tmpNext;
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
        if (che.aretes().isEmpty()) {
            return 0;
        }
        return (CheminImpl.splitByModalite(che).size()) - 1;
    }

    /**
     * @param chemin
     * @return la durée des changements dans un chemin
     */
    public static int getChangementDuration(Chemin che) {
        return CheminImpl.getNbChangement(che) * Plateforme.TEMP_CHANGEMENT;
    }

    /**
     * @param chemin
     * @return un chemin sous forme de chaine de caractères
     */
    public static String toString(Chemin che) {
        String r = "";
        for (Trancon t : che.aretes()) {
            r += t.getModalite() + " de " + t.getDepart() + " à " + t.getArrivee() + " ";
        }
        return r;
    }
}
