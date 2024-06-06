package src;

import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Trancon;

/**
 * Implémentation de l'interface Trancon
 */
public class TranconImpl implements Trancon {
    private Lieu depart;
    private Lieu arrivee;
    private ModaliteTransport modalite;

    /**
     * @constructor TranconImpl
     * @param depart Lieu de départ
     * @param arrivee Lieu d'arrivée
     * @param modalite Modalité de transport
     * Crée une arête entre deux lieux
     */
    public TranconImpl(Lieu depart, Lieu arrivee, ModaliteTransport modalite) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.modalite = modalite;
    }

    /**
     * @return Lieu de départ
     */
    public Lieu getDepart() {
        return depart;
    }

    /**
     * @return Lieu d'arrivée
     */
    public Lieu getArrivee() {
        return arrivee;
    }

    /**
     * @return Modalité de transport
     */
    public ModaliteTransport getModalite() {
        return modalite;
    }

    /**
     * @return Chaîne de caractères représentant l'arete
     */
    public String toString() {
        return depart + " -> " + arrivee + " (" + modalite + ")";
    }
}
