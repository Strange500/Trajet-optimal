package v1;

import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Trancon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;


public class IChemin implements Chemin {
    private List<Trancon> aretes;
    private double poids;

    public List<Trancon> aretes() {
        return aretes;
    }

    public double poids() {
        return poids;
    }

    public IChemin() {
        aretes = new ArrayList<Trancon>();
        poids = 0;
    }

    public static List<Chemin> splitByModalite(Chemin chemin) {
        List<Chemin> chemins = new ArrayList<Chemin>();
        Iterator<Trancon> it = chemin.aretes().iterator();
        Trancon tmpNext = it.next();
        Chemin tmp ;
        while (tmpNext!=null) {
            Trancon t= tmpNext;
            tmp = new IChemin();
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

    public static int getNbChangement(Chemin che) {
        return (IChemin.splitByModalite(che).size())/2;
    }

    public static int getCHangementDuration(Chemin che) {
        return IChemin.getNbChangement(che) * Tp.TEMP_CHANGEMENT;
    }



    public static String  toString(Chemin che) {
        String r = "";
        for (Trancon t : che.aretes()) {
            r += t.getModalite() + " de " + t.getDepart() + " à " + t.getArrivee() + " ";
        }
        return r;
    }
}
