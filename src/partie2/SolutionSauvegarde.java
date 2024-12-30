package partie2;

import java.io.*;
import java.util.Map;

/**
 * Cette classe fournit une méthode utilitaire pour sauvegarder une solution d'affectation
 * entre les colons et les ressources dans un fichier.
 */
public class SolutionSauvegarde {

    /**
     * Sauvegarde une solution dans un fichier spécifié.
     * Chaque ligne du fichier contiendra une affectation au format "colon:ressource".
     *
     * @param cheminFichier Le chemin du fichier où la solution sera sauvegardée.
     * @param solution Une Map contenant la solution à sauvegarder, où chaque clé est un nom de colon
     *                 et chaque valeur est la ressource qui lui est attribuée.
     * @throws IOException Si une erreur survient lors de l'écriture dans le fichier.
     */
    public static void sauvegardeSolution(String cheminFichier, Map<String, String> solution) throws IOException {
        BufferedWriter ecrire = new BufferedWriter(new FileWriter(cheminFichier));
        for (Map.Entry<String, String> entry : solution.entrySet()) {
            ecrire.write(entry.getKey() + ":" + entry.getValue());
            ecrire.newLine();
        }
        ecrire.close();
    }
}
