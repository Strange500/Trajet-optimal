package src;

import fr.ulille.but.sae_s2_2024.Lieu;

/**
 * Implémentation de l'interface Lieu
 */
public class LieuImpl implements Lieu {
    private final String NOM;

    /**
     * @constructor LieuImpl
     *              Crée un lieu avec un nom
     * @param nom le nom du lieu
     */
    public LieuImpl(String nom) {
        this.NOM = nom;
    }

    /**
     * @return le nom du lieu
     */
    public String getNom() {
        return NOM;
    }

    /**
     * @return le nom du lieu sous forme de chaîne de caractères
     */
    @Override
    public String toString() {
        return NOM;
    }
}
