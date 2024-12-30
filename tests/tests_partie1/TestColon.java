package tests_partie1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import partie1.Colon;

/**
 * Classe de test pour la classe {@link partie1.Colon}.
 * Cette classe utilise JUnit pour tester les fonctionnalités de la classe Colon.
 */
class TestColon {

    /**
     * Teste la création d'un colon avec un nom donné.
     * Vérifie que le nom fourni au constructeur est correctement assigné.
     */
    @Test
    void testCreerColon() {
        // Création d'un colon avec un nom
        Colon colon = new Colon("Alice");
        
        // Assertion pour vérifier que le nom est correct
        assertEquals("Alice", colon.getNom());
    }

    /**
     * Teste l'ajout de préférences à un colon.
     * Vérifie que les préférences sont correctement enregistrées.
     */
    @Test
    void testAjouterPreferences() {
        // Création d'un colon
        Colon colon = new Colon("Alice");
        
        // Définition des préférences
        colon.setPreferences(List.of("Ressource1", "Ressource2"));
        
        // Assertion pour vérifier que les préférences sont correctes
        assertEquals(List.of("Ressource1", "Ressource2"), colon.getPreferences());
    }
}
