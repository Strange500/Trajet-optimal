package src;

import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Trancon;

/**
 * Implémentation de l'interface Trancon
 */
public class TranconImpl implements Trancon {
    private final Lieu DEPART;
    private final Lieu ARRIVEE;
    private final ModaliteTransport MODALITE;

    /**
     * @constructor TranconImpl
     * @param depart   Lieu de départ
     * @param arrivee  Lieu d'arrivée
     * @param modalite Modalité de transport
     *                 Crée une arête entre deux lieux
     */
    public TranconImpl(Lieu depart, Lieu arrivee, ModaliteTransport modalite) {
        this.DEPART = depart;
        this.ARRIVEE = arrivee;
        this.MODALITE = modalite;
    }

    /**
     * @return Lieu de départ
     */
    public Lieu getDepart() {
        return DEPART;
    }

    /**
     * @return Lieu d'arrivée
     */
    public Lieu getArrivee() {
        return ARRIVEE;
    }

    /**
     * @return Modalité de transport
     */
    public ModaliteTransport getModalite() {
        return MODALITE;
    }

    /**
     * @return Chaîne de caractères représentant l'arete
     */
    public String toString() {
        return DEPART + " -> " + ARRIVEE + " (" + MODALITE + ")";
    }
}
