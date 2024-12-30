package tests_partie2;

import org.junit.jupiter.api.*;
import partie2.AnaliseFichier;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

/**
 * Classe de tests unitaires pour la classe {@link AnaliseFichier}.
 * 
 * Cette classe contient plusieurs tests qui valident le bon comportement de la méthode
 * {@link AnaliseFichier#analiseFichier()} dans différentes situations, telles que la gestion des erreurs de syntaxe,
 * les fichiers manquants, les erreurs de colonnes, etc.
 */
public class TestAnaliseFichier {

    private static final String REPERTOIRE_TEST = "tests/resources/";

    /**
     * Teste la méthode {@link AnaliseFichier#analiseFichier()} avec un fichier valide,
     * mais contenant des colons non existants.
     */
    @Test
    void testAnaliseFchierColonEtRessourceValides() {
        String cheminFichier = REPERTOIRE_TEST + "fichier_valide";
        AnaliseFichier fichierAnaliser = new AnaliseFichier(cheminFichier);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fichierAnaliser.analiseFichier();
        });

        assertEquals("Erreur à la ligne 3 : deteste(Colon1, Colon2). -> Les colons spécifiés n'existent pas.", exception.getMessage(),
                "Le message d'exception devrait correspondre à celui généré par l'exception");
    }

    /**
     * Teste la méthode {@link AnaliseFichier#analiseFichier()} avec un fichier contenant des lignes vides.
     */
    @Test
    void testAnaliseFchierAvecLigneVide() {
        String cheminFichier = REPERTOIRE_TEST + "fichier_avec_lignes_vides";
        AnaliseFichier fichierAnaliser = new AnaliseFichier(cheminFichier);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fichierAnaliser.analiseFichier();
        });

        assertEquals("Erreur à la ligne 5 : deteste(Colon1, Colon2). -> Les colons spécifiés n'existent pas.", exception.getMessage(),
                "Le message d'exception devrait correspondre à celui généré par l'exception");
    }

    /**
     * Teste la méthode {@link AnaliseFichier#analiseFichier()} avec un fichier contenant une syntaxe invalide pour les colons.
     */
    @Test
    void testAnaliseFchierSyntaxeColonInvalide() {
        String cheminFichier = REPERTOIRE_TEST + "fichier_syntaxe_invalide_colon";
        AnaliseFichier fichierAnaliser = new AnaliseFichier(cheminFichier);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fichierAnaliser.analiseFichier();
        });

        assertEquals("Erreur à la ligne 1 : colon(Colon1 -> Syntaxe invalide à la ligne 1", exception.getMessage(),
                "Le message d'exception devrait correspondre à celui généré par l'exception");
    }

    /**
     * Teste la méthode {@link AnaliseFichier#analiseFichier()} avec un fichier où il manque un colon avant la fonction deteste.
     */
    @Test
    void testAnaliseFchierColonManquantAvantDeteste() {
        String cheminFichier = REPERTOIRE_TEST + "fichier_colon_manquant";
        AnaliseFichier fichierAnaliser = new AnaliseFichier(cheminFichier);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fichierAnaliser.analiseFichier();
        });

        assertEquals("Erreur à la ligne 2 : deteste(Colon1, Colon2). -> Syntaxe inconnue", exception.getMessage(),
                "Le message d'exception devrait correspondre à celui généré par l'exception");
    }

    /**
     * Teste la méthode {@link AnaliseFichier#analiseFichier()} avec un fichier valide mais contenant des préférences de colonnes non valides.
     */
    @Test
    void testAnaliseFchierAvecDetesteEtPreferences() {
        String cheminFichier = REPERTOIRE_TEST + "fichier_valide_avec_preferences";
        AnaliseFichier fichierAnaliser= new AnaliseFichier(cheminFichier);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fichierAnaliser.analiseFichier();
        });

        assertEquals("Erreur à la ligne 3 : deteste(Colon1, Colon2). -> Les colons spécifiés n'existent pas.",
                exception.getMessage(), "Le message d'exception devrait correspondre à celui généré par l'exception");
    }

    /**
     * Teste la méthode {@link AnaliseFichier#analiseFichier()} lorsqu'un fichier est introuvable.
     */
    @Test
    void testFichierIntrouvable() {
        String cheminFichier = REPERTOIRE_TEST + "fichier_inexistant.txt";
        AnaliseFichier fichierAnaliser = new AnaliseFichier(cheminFichier);

        assertThrows(IOException.class, () -> {
            fichierAnaliser.analiseFichier();
        });
    }
}
