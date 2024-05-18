package v1;

import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Trancon;

public class ITrancon implements Trancon {
    private Lieu depart;
    private Lieu arrivee;
    private ModaliteTransport modalite;

    public ITrancon(Lieu depart, Lieu arrivee, ModaliteTransport modalite) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.modalite = modalite;
    }
    public Lieu getDepart() {
        return depart;
    }

    public Lieu getArrivee() {
        return arrivee;
    }

    public ModaliteTransport getModalite() {
        return modalite;
    }

    public String toString() {
        return depart + " -> " + arrivee + " (" + modalite + ")";
    }
}
