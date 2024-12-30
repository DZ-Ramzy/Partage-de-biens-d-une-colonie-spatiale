package partie1;

import java.util.List;

/**
 * Représente un colon avec un nom et une liste de préférences.
 */
public class Colon {

    /**
     * Le nom du colon.
     */
    private String nom;

    /**
     * La liste des préférences du colon.
     */
    private List<String> preferences;

    /**
     * Constructeur de la classe Colon.
     *
     * @param nom le nom du colon.
     */
    public Colon(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient le nom du colon.
     *
     * @return le nom du colon.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit les préférences du colon.
     *
     * @param preferences une liste de préférences associées au colon.
     */
    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    /**
     * Obtient les préférences du colon.
     *
     * @return une liste de préférences associées au colon.
     */
    public List<String> getPreferences() {
        return preferences;
    }
}
