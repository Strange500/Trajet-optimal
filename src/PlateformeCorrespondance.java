package src;

import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import fr.ulille.but.sae_s2_2024.Trancon;
import src.exception.CheminInexistantException;

public class PlateformeCorrespondance extends Plateforme {

    PlateformeCorrespondance() {
        super();
    }

    @Override
    public PlateformeCorrespondance clone() {
        PlateformeCorrespondance g = new PlateformeCorrespondance();
        g.g1 = clone(g1);
        g.g2 = clone(g2);
        g.g3 = clone(g3);
        return g;
    }

    /**
     * @param depart   le lieu de départ
     * @param arrivee  le lieu d'arrivée
     * @param modalite le mode de transport
     * @return vrai si un chemin existe entre deux lieux avec un mode de transport
     *         donné
     */
    @Override
    public boolean hasPathByModalite(String depart, String arrivee, ModaliteTransport modalite) {
        PlateformeCorrespondance g = this.clone();
        if (modalite != null) {
            g.filterByModality(modalite);
        }
        try {
            getPathByModaliteAndTypeCoutTriggerNoPath(depart, arrivee, modalite, TypeCout.CO2, 1).size();
            return true;
        } catch (CheminInexistantException e) {
            return false;
        }
    }

    /**
     * @param dep      le lieu de départ
     * @param arr      le lieu d'arrivée
     * @param modalite le mode de transport
     * @param critere  le critère à utiliser (CO2, PRIX, TEMPS)
     * @param nbChemin le nombre de chemins à retourner
     * @return les chemins entre deux lieux avec un mode de transport donné et un
     *         critère donné
     */
    @Override
    public List<Chemin> getPathByModaliteAndTypeCout(String dep, String arr, ModaliteTransport modalite,
            TypeCout critere, int nbChemin)  {
        PlateformeCorrespondance g = this.clone();
        if (modalite != null) {
            g.filterByModality(modalite);
        }
        return g.getPathByTypeCout(dep, arr, critere, nbChemin);
    }

    /**
     * @param dep      le lieu de départ
     * @param arr      le lieu d'arrivée
     * @param modalite le mode de transport
     * @param critere  le critère à utiliser (CO2, PRIX, TEMPS)
     * @param nbChemin le nombre de chemins à retourner
     * @return les chemins entre deux lieux avec un mode de transport donné et un
     *         critère donné
     */
    public List<Chemin> getPathByModaliteAndTypeCoutTriggerNoPath(String dep, String arr, ModaliteTransport modalite,
            TypeCout critere, int nbChemin) throws CheminInexistantException {
        PlateformeCorrespondance g = this.clone();
        if (modalite != null) {
            g.filterByModality(modalite);
        }
        return g.getPathByTypeCoutTriggerNoPath(dep, arr, critere, nbChemin);
    }

    



    /**
     * @param dep      le lieu de départ
     * @param arr      le lieu d'arrivée
     * @param critere  le critère à utiliser (CO2, PRIX, TEMPS)
     * @param nbChemin le nombre de chemins à retourner
     * @return les chemins entre deux lieux avec un critère donné
     * @throws CheminInexistantException 
     */
    @Override
    public List<Chemin> getPathByTypeCout(String dep, String arr, TypeCout critere, int nbChemin)  {
        Plateforme g = this.clone();
        List<Chemin> results = new ArrayList<Chemin>();
        // Lieu depart = this.getSommet(dep);
        // Lieu arrivee = this.getSommet(arr);
        for (String names : ToolsCorrespondance.buildLieuxNames(dep)) {
            g.ajouterArrete(ALPHA, names + ToolsCorrespondance.SUFFIXE, ModaliteTransport.TRAIN, 0, 0, 0);
        }

        for (String names : ToolsCorrespondance.buildLieuxNames(arr)) {
            g.ajouterArrete(names, OMEGA, ModaliteTransport.TRAIN, 0, 0, 0);
        }
        // g.ajouterArrete(ALPHA, dep, ModaliteTransport.TRAIN, 0, 0, 0);
        // g.ajouterArrete(arr, OMEGA, ModaliteTransport.TRAIN, 0, 0, 0);

        Lieu alpha = g.getSommet(ALPHA);
        Lieu omega = g.getSommet(OMEGA);

        switch (critere) {
            case PRIX:
                results = AlgorithmeKPCC.kpcc(g.g1, alpha, omega, nbChemin);
                break;
            case CO2:
                results = AlgorithmeKPCC.kpcc(g.g2, alpha, omega, nbChemin);
                break;
            case TEMPS:
                results = AlgorithmeKPCC.kpcc(g.g3, alpha, omega, nbChemin);
                break;
        }
        return results;
    }

    /**
     * @param dep      le lieu de départ
     * @param arr      le lieu d'arrivée
     * @param critere  le critère à utiliser (CO2, PRIX, TEMPS)
     * @param nbChemin le nombre de chemins à retourner
     * @return les chemins entre deux lieux avec un critère donné
     * @throws CheminInexistantException 
     */
    public List<Chemin> getPathByTypeCoutTriggerNoPath(String dep, String arr, TypeCout critere, int nbChemin) throws CheminInexistantException  {
        List<Chemin> results = this.getPathByTypeCout(dep, arr, critere, nbChemin);
        if (results.size() == 0) {
            throw new CheminInexistantException();
        }
        return results;
    }


    /**
     * @return la liste des lieux de la plateforme (sous forme de chaînes de
     *         caractères)
     */
    public List<String> getLieuxNames() {
        List<String> lieux = new ArrayList<String>();
        for (Lieu l : g1.sommets()) {
            String[] ligne = l.toString().split("_");
            if (!lieux.contains(ligne[0])) {
                lieux.add(ligne[0]);
            }
        }
        return lieux;
    }

    /**
     * @param avoid le lieu à éviter
     * @return la liste des lieux de la plateforme sans le lieu à éviter (sous forme
     *         de chaînes de caractères)
     */
    public List<String> getLieuxNames(String avoid) {
        List<String> lieux = this.getLieuxNames();
        for (String l : lieux) {
            String[] ligne = l.toString().split("_");
            if (ligne[0].equals(avoid)) {
                lieux.remove(l);
                break;
            }
        }
        return lieux;
    }

    
}
