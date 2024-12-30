package tests_partie2;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import partie1.Colonie;
import partie2.SolutionOptimise;

/**
 * Classe de tests unitaires pour la classe {@link SolutionOptimise}.
 * 
 * Cette classe contient des tests qui valident le bon fonctionnement de la méthode
 * {@link SolutionOptimise#optimiseSolution(int)} dans différents scénarios,
 * tels que l'optimisation d'une solution pour un petit nombre de colons et de ressources,
 * ainsi que l'optimisation pour un grand nombre de colons et de ressources.
 */
class TestSolutionOptimise {

    /**
     * Teste la méthode {@link SolutionOptimise#optimiseSolution(int)} en optimisant
     * une solution simple avec quelques colons et ressources. L'objectif est de vérifier
     * que l'optimisation minimise les conflits de ressources.
     */
    @Test
    void testOptimiserSolution() {
        Colonie colonie = new Colonie();
        colonie.ajouterColon("Alice");
        colonie.ajouterColon("Bob");
        colonie.ajouterRessource("Ressource1");
        colonie.ajouterRessource("Ressource2");

        colonie.ajouterPreferences("Alice", List.of("Ressource1", "Ressource2"));
        colonie.ajouterPreferences("Bob", List.of("Ressource2", "Ressource1"));

        // Génération d'une solution naïve
        colonie.genererSolutionNaive();
        SolutionOptimise optimizer = new SolutionOptimise(colonie);

        // Optimisation de la solution
        optimizer.optimiseSolution(100); // Optimisation avec 100 itérations

        // Calcul du coût de la solution optimisée (doit être 0 s'il n'y a plus de conflits)
        int cout = colonie.calculerCout();
        assertEquals(0, cout); // L'optimisation doit minimiser les conflits.
    }

    /**
     * Teste la méthode {@link SolutionOptimise#optimiseSolution(int)} en optimisant
     * une solution avec un grand nombre de colons et de ressources. L'objectif est de vérifier
     * que l'optimisation fonctionne efficacement même avec un grand nombre de colons et de ressources.
     */
    @Test
    void testOptimisationGrandNombreColonsRessources() {
        Colonie colonie = new Colonie();
        int nombreColons = 100;
        int nombreRessources = nombreColons;

        // Ajout des colons
        for (int i = 1; i <= nombreColons; i++) {
            colonie.ajouterColon("Colon" + i);
        }

        // Ajout des ressources
        for (int i = 1; i <= nombreRessources; i++) {
            colonie.ajouterRessource("Ressource" + i);
        }

        // Ajout des préférences pour chaque colon
        for (int i = 1; i <= nombreColons; i++) {
            List<String> preferences = new ArrayList<>();
            for (int j = 1; j <= nombreRessources; j++) {
                preferences.add("Ressource" + j);
            }
            colonie.ajouterPreferences("Colon" + i, preferences);
        }

        // Génération d'une solution naïve
        colonie.genererSolutionNaive();

        // Optimisation de la solution
        SolutionOptimise optimiser = new SolutionOptimise(colonie);
        optimiser.optimiseSolution(100); // Optimisation avec 100 itérations

        // Vérification de l'optimisation
        int cout = colonie.calculerCout();
        assertEquals(0, cout); // L'optimisation doit minimiser les conflits
    }
}
