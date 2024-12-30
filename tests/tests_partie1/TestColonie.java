package tests_partie1;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import partie1.Colonie;

/**
 * Classe de test pour la classe {@link partie1.Colonie}.
 * Cette classe utilise JUnit pour valider les fonctionnalités de la classe Colonie.
 */
class TestColonie {

    /**
     * Teste l'ajout d'un colon à la colonie.
     * Vérifie que la taille de la liste des colons augmente correctement.
     */
    @Test
    void testAjouterColon() {
        Colonie colonie = new Colonie();
        int tailleAvant = colonie.getColons().size();
        colonie.ajouterColon("Alice");
        int tailleApres = colonie.getColons().size();
        assertEquals(tailleAvant + 1, tailleApres);
    }

    /**
     * Teste l'ajout d'une ressource à la colonie.
     * Vérifie que les ressources sont correctement enregistrées.
     */
    @Test
    void testAjouterRessource() {
        Colonie colonie = new Colonie();
        colonie.ajouterRessource("Ressource1");
        Set<String> expectedRessources = Set.of("Ressource1");
        assertEquals(expectedRessources, colonie.getRessources());
    }

    /**
     * Teste l'ajout d'une relation entre deux colons.
     * Vérifie que la relation est correctement enregistrée dans l'ensemble des relations.
     */
    @Test
    void testAjouterRelation() {
        Colonie colonie = new Colonie();
        colonie.ajouterColon("Alice");
        colonie.ajouterColon("Bob");
        colonie.ajouterRelation("Alice", "Bob");

        Set<Set<String>> expectedRelations = Set.of(Set.of("Alice", "Bob"));
        assertEquals(expectedRelations, colonie.getRelations());
    }

    /**
     * Teste la génération d'une solution naïve pour l'affectation des ressources.
     * Vérifie que chaque colon a bien une ressource affectée.
     */
    @Test
    void testGenererSolutionNaive() {
        Colonie colonie = new Colonie();
        colonie.ajouterColon("Alice");
        colonie.ajouterColon("Bob");
        colonie.ajouterRessource("Ressource1");
        colonie.ajouterRessource("Ressource2");

        colonie.ajouterPreferences("Alice", List.of("Ressource1", "Ressource2"));
        colonie.ajouterPreferences("Bob", List.of("Ressource2", "Ressource1"));

        colonie.genererSolutionNaive();

        assertNotNull(colonie.getAffectations().get("Alice"));
        assertNotNull(colonie.getAffectations().get("Bob"));
    }

    /**
     * Teste le calcul du coût de la solution générée.
     * Vérifie que le coût est correctement calculé et est supérieur ou égal à 0.
     */
    @Test
    void testCalculerCout() {
        Colonie colonie = new Colonie();
        colonie.ajouterColon("Alice");
        colonie.ajouterColon("Bob");
        colonie.ajouterRessource("Ressource1");
        colonie.ajouterRessource("Ressource2");

        colonie.ajouterPreferences("Alice", List.of("Ressource1", "Ressource2"));
        colonie.ajouterPreferences("Bob", List.of("Ressource2", "Ressource1"));

        colonie.genererSolutionNaive();
        int cout = colonie.calculerCout();

        assertEquals(0, cout, "Le coût doit être au moins égal à 0");
    }

    /**
     * Teste les relations complexes entre plusieurs colons.
     * Vérifie que toutes les relations sont correctement enregistrées.
     */
    @Test
    void testRelationsComplexes() {
        Colonie colonie = new Colonie();
        colonie.ajouterColon("Alice");
        colonie.ajouterColon("Bob");
        colonie.ajouterColon("Charlie");

        colonie.ajouterRelation("Alice", "Bob"); // Alice et Bob se détestent
        colonie.ajouterRelation("Bob", "Charlie"); // Bob et Charlie se détestent
        colonie.ajouterRelation("Alice", "Charlie"); // Alice et Charlie se détestent

        // Vérification des relations
        Set<Set<String>> expectedRelations = Set.of(Set.of("Alice", "Bob"), Set.of("Bob", "Charlie"),
                Set.of("Alice", "Charlie"));
        assertEquals(expectedRelations, colonie.getRelations());
    }

    /**
     * Teste les préférences conflictuelles entre deux colons.
     * Vérifie que les affectations tiennent compte des conflits et que le coût est supérieur à 0.
     */
    @Test
    void testPreferencesConflictuelles() {
        Colonie colonie = new Colonie();
        colonie.ajouterColon("Alice");
        colonie.ajouterColon("Bob");

        colonie.ajouterRessource("Ressource1");
        colonie.ajouterRessource("Ressource2");

        // Alice et Bob se détestent
        colonie.ajouterRelation("Alice", "Bob");

        // Alice préfère Ressource1 et Bob préfère Ressource1 aussi
        colonie.ajouterPreferences("Alice", List.of("Ressource1", "Ressource2"));
        colonie.ajouterPreferences("Bob", List.of("Ressource1", "Ressource2"));

        // Générer la solution naïve
        colonie.genererSolutionNaive();

        // Vérification des affectations (doivent être différentes car les deux
        // préfèrent la même ressource)
        assertNotEquals(colonie.getAffectations().get("Alice"), colonie.getAffectations().get("Bob"));

        // Vérification du coût (devrait être supérieur à 0 en raison du conflit)
        int cout = colonie.calculerCout();
        assertThat(cout, greaterThan(0));
    }

    /**
     * Teste l'extraction des données après la génération d'une solution naïve.
     * Vérifie que les affectations correspondent à ce qui est attendu.
     */
    @Test
    void testExtractionDonnees() {
        Colonie colonie = new Colonie();
        colonie.ajouterColon("Alice");
        colonie.ajouterColon("Bob");
        colonie.ajouterRessource("Ressource1");
        colonie.ajouterRessource("Ressource2");

        colonie.ajouterPreferences("Alice", List.of("Ressource1", "Ressource2"));
        colonie.ajouterPreferences("Bob", List.of("Ressource2", "Ressource1"));

        // Générer une solution naïve
        colonie.genererSolutionNaive();

        // Vérification des données extraites
        assertEquals("Ressource1", colonie.getAffectations().get("Alice"));
        assertEquals("Ressource2", colonie.getAffectations().get("Bob"));
    }
}
