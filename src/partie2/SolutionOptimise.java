package partie2;

import partie1.Colonie;
import partie1.Colon;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe est responsable de l'optimisation des affectations entre les colons et les ressources dans une colonie.
 * Elle utilise une approche heuristique pour minimiser le coût global des affectations.
 */
public class SolutionOptimise {
    private Colonie colonie;

    /**
     * Constructeur de la classe SolutionOptimise.
     * @param colonie La colonie contenant les colons, les ressources, et les préférences à optimiser.
     */
    public SolutionOptimise(Colonie colonie) {
        this.colonie = colonie;
    }

    /**
     * Optimise les affectations des ressources aux colons en minimisant les jalousies
     * avec une stratégie probabiliste.
     *
     * @param iterations Le nombre d'itérations pour l'optimisation.
     * @return La map des affectations optimisées.
     */
    public Map<String, String> optimiseSolution2(int iterations) {
        // Générer une solution initiale
        Map<String, String> actuelleSolution = colonie.genererSolutionNaive();
        int actuelleCout = colonie.calculerCout();
    
        // Sauvegarder la meilleure solution absolue
        Map<String, String> meilleureSolution = new HashMap<>(actuelleSolution);
        int meilleurCout = actuelleCout;
    
        Random random = new Random();

        for (int i = 0; i < iterations; i++) {
            // Choisir le premier colon avec 80% de chance parmi les jaloux
            String colon1 = choisirColon(random);

            // Choisir le deuxième colon comme le 1er, en s'assurant qu'il est différent du premier
            String colon2;
            do {
                colon2 = choisirColon(random);
            } while (colon1.equals(colon2));
    
            // Sauvegarder les ressources avant l'échange
            String resource1 = actuelleSolution.get(colon1);
            String resource2 = actuelleSolution.get(colon2);
    
            // Réaliser l'échange
            actuelleSolution.put(colon1, resource2);
            actuelleSolution.put(colon2, resource1);
    
            // Calculer le coût après l'échange
            colonie.setAffectations(actuelleSolution); // Mettre à jour les affectations
            int nouveauCout = colonie.calculerCout();
    
            // Accepter ou rejeter l'échange
            if (nouveauCout < actuelleCout) {
                // Accepter si le coût diminue
                actuelleCout = nouveauCout;
                if (nouveauCout < meilleurCout) {
                    // Mettre à jour la meilleure solution absolue
                    meilleureSolution = new HashMap<>(actuelleSolution);
                    meilleurCout = nouveauCout;
                }
            } else if (random.nextDouble() > 0.7)
                // Accepter avec 30% de chance même si le coût augmente
                actuelleCout = nouveauCout;
            else {
                // Annuler l'échange
                actuelleSolution.put(colon1, resource1);
                actuelleSolution.put(colon2, resource2);
            }
        }
        // Restaurer la meilleure solution absolue
        colonie.setAffectations(meilleureSolution);
        return meilleureSolution;
    }
    
    /**
     * Méthode pour choisir un colon avec 80% de chance parmi les jaloux,
     * et 20% parmi la population totale.
     */
    private String choisirColon(Random random) {
        if (!colonie.getColonsJaloux().isEmpty() && random.nextDouble() < 0.8) {
            List<Colon> jaloux = new ArrayList<>(colonie.getColonsJaloux());
            return jaloux.get(random.nextInt(jaloux.size())).getNom();
        }
        else {
            List<String> tousLesColons = new ArrayList<>(colonie.getColons().keySet());
            return tousLesColons.get(random.nextInt(tousLesColons.size()));
        }
    }

    /**
     * Optimise les affectations des ressources aux colons pour réduire le coût global.
     * L'algorithme effectue un nombre défini d'itérations pour tenter d'améliorer la solution initiale en échangeant des ressources.
     * 
     * @param iterations Le nombre d'itérations à effectuer pour l'optimisation.
     * @return Une Map représentant la solution optimisée où chaque clé est un nom de colon
     *         et la valeur correspond à la ressource qui lui est assignée.
     */
    public Map<String, String> optimiseSolution(int iterations) {
        // Générer une solution initiale
        Map<String, String> actuelleSolution = colonie.genererSolutionNaive(); // Génère une solution naïve

        // Calculer le coût initial
        int actuelleCout = colonie.calculerCout(); // Méthode pour calculer le coût initial de la solution

        Random random = new Random();

        for (int i = 0; i < iterations; i++) {
            // Choisir deux colons différents au hasard
            String[] colonNoms = actuelleSolution.keySet().toArray(new String[0]);
            String colon1 = colonNoms[random.nextInt(colonNoms.length)];
            String colon2;

            // S'assurer que colon1 et colon2 sont différents
            do {
                colon2 = colonNoms[random.nextInt(colonNoms.length)];
            } while (colon1.equals(colon2));

            // Échanger leurs ressources
            String resource1 = actuelleSolution.get(colon1);
            String resource2 = actuelleSolution.get(colon2);
            actuelleSolution.put(colon1, resource2);
            actuelleSolution.put(colon2, resource1);

            // Calculer le nouveau coût
            int nouveauCost = colonie.calculerCout();

            // Accepter ou annuler l'échange
            if (nouveauCost >= actuelleCout) {
                // Annuler l'échange si le coût augmente ou reste le même
                actuelleSolution.put(colon1, resource1);
                actuelleSolution.put(colon2, resource2);
            } else {
                // Accepter la solution avec un coût réduit
                actuelleCout = nouveauCost;
            }
        }
        return actuelleSolution; // Retourner la solution optimisée
    }
}