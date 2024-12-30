package partie2;

import partie1.Colonie;

import java.io.*;
import java.util.*;

/**
 * Classe permettant de lire un fichier de configuration pour initialiser une colonie.
 */
public class AnaliseFichier {
    /**
     * Chemin du fichier à analyser.
     */
    private final String cheminFichier;

    /**
     * Constructeur de la classe AnaliseFichier.
     *
     * @param cheminFichier le chemin du fichier à analyser.
     */
    public AnaliseFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    /**
     * Analyse le fichier et initialise une colonie en fonction de son contenu.
     *
     * @return une instance de {@link Colonie} initialisée avec les données du fichier.
     * @throws IOException si une erreur se produit lors de la lecture du fichier.
     */
    public Colonie analiseFichier() throws IOException {
        Colonie colonie = new Colonie();
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            int numeroLigne = 0;
            boolean colonComplet = false;
            boolean ressourceComplet = false;
            boolean destesteComplet = false;

            while ((ligne = reader.readLine()) != null) {
                numeroLigne++;
                ligne = ligne.trim();

                // Ignore ligne vide
                if (ligne.isEmpty()) {
                    continue;
                }

                try {
                    if (ligne.startsWith("colon(")) {
                        colonComplet = true;
                        traiterColon(ligne, numeroLigne, colonie);
                    } else if (ligne.startsWith("ressource(")) {
                        ressourceComplet = true;
                        traiterRessource(ligne, numeroLigne, colonie);
                    } else if (ligne.startsWith("deteste(") && colonComplet) {
                        destesteComplet = true;
                        traiterDeteste(ligne, numeroLigne, colonie);
                    } else if (ligne.startsWith("preferences(") && ressourceComplet && destesteComplet) {
                        traiterPreferences(ligne, numeroLigne, colonie);
                    } else {
                        throw new IllegalArgumentException("Syntaxe inconnue");
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Erreur à la ligne " + numeroLigne + " : " + ligne + " -> " + e.getMessage());
                }
            }
        }
        return colonie;
    }

    /**
     * Traite une ligne contenant un colon.
     *
     * @param ligne        la ligne à traiter.
     * @param numeroLigne  le numéro de la ligne dans le fichier.
     * @param colonie      la colonie à mettre à jour.
     */
    private void traiterColon(String ligne, int numeroLigne, Colonie colonie) {
        String colonNom = extraireArgument(ligne, "colon(", numeroLigne);
        colonie.ajouterColon(colonNom);
    }

    /**
     * Traite une ligne contenant une ressource.
     *
     * @param ligne        la ligne à traiter.
     * @param numeroLigne  le numéro de la ligne dans le fichier.
     * @param colonie      la colonie à mettre à jour.
     */
    private void traiterRessource(String ligne, int numeroLigne, Colonie colonie) {
        String ressourceNom = extraireArgument(ligne, "ressource(", numeroLigne);
        colonie.ajouterRessource(ressourceNom);
    }

    /**
     * Traite une ligne contenant une relation "déteste" entre deux colons.
     *
     * @param ligne        la ligne à traiter.
     * @param numeroLigne  le numéro de la ligne dans le fichier.
     * @param colonie      la colonie à mettre à jour.
     */
    private void traiterDeteste(String ligne, int numeroLigne, Colonie colonie) {
        String[] parts = extraireArguments(ligne, "deteste(", numeroLigne, 2);
        colonie.ajouterRelation(parts[0], parts[1]);
    }

    /**
     * Traite une ligne contenant les préférences d'un colon.
     *
     * @param ligne        la ligne à traiter.
     * @param numeroLigne  le numéro de la ligne dans le fichier.
     * @param colonie      la colonie à mettre à jour.
     */
    private void traiterPreferences(String ligne, int numeroLigne, Colonie colonie) {
        String[] parts = extraireArguments(ligne, "preferences(", numeroLigne, 2);
        String colonName = parts[0];
        List<String> preferences = Arrays.asList(parts).subList(1, parts.length);
        colonie.ajouterPreferences(colonName, preferences);
    }

    /**
     * Extrait un argument unique d'une ligne de fichier.
     *
     * @param ligne       la ligne à traiter.
     * @param prefix      le préfixe attendu.
     * @param numeroLigne le numéro de la ligne.
     * @return l'argument extrait.
     */
    private String extraireArgument(String ligne, String prefix, int numeroLigne) {
        if (!ligne.startsWith(prefix) || !ligne.endsWith(".")) {
            throw new IllegalArgumentException("Syntaxe invalide à la ligne " + numeroLigne);
        }
        return ligne.substring(prefix.length(), ligne.length() - 2).trim();
    }

    /**
     * Extrait plusieurs arguments d'une ligne de fichier.
     *
     * @param ligne       la ligne à traiter.
     * @param prefix      le préfixe attendu.
     * @param numeroLigne le numéro de la ligne.
     * @param minArgs     le nombre minimum d'arguments attendus.
     * @return un tableau contenant les arguments extraits.
     */
    private String[] extraireArguments(String ligne, String prefix, int numeroLigne, int minArgs) {
        String contenu = extraireArgument(ligne, prefix, numeroLigne);
        String[] parts = contenu.split(",");
        if (parts.length < minArgs) {
            throw new IllegalArgumentException("Arguments insuffisants à la ligne " + numeroLigne);
        }
        return Arrays.stream(parts).map(String::trim).toArray(String[]::new);
    }
}
