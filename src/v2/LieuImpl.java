package src.v2;

import fr.ulille.but.sae_s2_2024.Lieu;

/**
 * Implémentation de l'interface Lieu
 */
public class LieuImpl implements Lieu {
    private String nom;

    /**
     * @constructor LieuImpl
     *              Crée un lieu avec un nom
     * @param nom le nom du lieu
     */
    public LieuImpl(String nom) {
        this.nom = nom;
    }

    /**
     * @return le nom du lieu
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return le nom du lieu sous forme de chaîne de caractères
     */
    public String toString() {
        return nom;
    }
}
