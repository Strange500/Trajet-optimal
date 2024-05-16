package src.v1;
import fr.ulille.but.sae_s2_2024.Lieu;

public class ILieu implements Lieu {
    private String nom;
    public ILieu(String nom) {
        this.nom = nom;
    }
    public String getNom() {
        return nom;
    }
    public String toString() {
        return nom;
    }
}
