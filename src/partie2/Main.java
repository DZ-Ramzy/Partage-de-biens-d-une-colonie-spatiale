package partie2;

import partie1.Colonie;
import partie1.InterfaceUtilisateur;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * La classe principale pour gérer l'exécution du programme.
 * Elle permet à l'utilisateur de charger les données d'une colonie depuis un fichier ou d'utiliser une interface utilisateur interactive.
 * Une fois les données chargées, elle propose des options pour résoudre les affectations optimisées et sauvegarder les résultats.
 */
public class Main {

    /**
     * Point d'entrée principal du programme.
     * @param args Si un fichier est fourni comme argument, il est utilisé pour charger les données de la colonie.
     *             Sinon, le programme démarre avec l'interface utilisateur interactive.
     */
    public static void main(String[] args) {
        Colonie colonie = new Colonie();
        Map<String, String> solution = null;

        if (args.length > 0) {
            String entreeFichier = args[0];
            try {
                AnaliseFichier analise = new AnaliseFichier(entreeFichier);
                colonie = analise.analiseFichier();
                System.out.println("Données de la colonie chargées depuis le fichier : " + entreeFichier);

                // Menu interactif pour gérer les solutions
                Scanner scanner = new Scanner(System.in);
                boolean sortie = false;

                while (!sortie) {
                    System.out.println("Menu :");
                    System.out.println("1) Résolution automatique");
                    System.out.println("2) Sauvegarder la solution actuelle");
                    System.out.println("3) Quitter");

                    int choix = getChoixUtilisateur(scanner);
                    switch (choix) {
                        case 1 -> {
                            SolutionOptimise optimise = new SolutionOptimise(colonie);
                            solution = optimise.optimiseSolution2(colonie.getColons().size() * colonie.getColons().size());
                            System.out.println("Solution optimisée : " + solution);
                            System.out.println("Coût de la solution : " + colonie.calculerCout());
                        }
                        case 2 -> {
                            System.out.println("Entrez le nom du fichier de sortie :");
                            String sortieFichier = scanner.nextLine();
                            try {
                                if (solution == null) {
                                    SolutionSauvegarde.sauvegardeSolution(sortieFichier, colonie.genererSolutionNaive());
                                } else {
                                    SolutionSauvegarde.sauvegardeSolution(sortieFichier, solution);
                                }
                                System.out.println("Solution sauvegardée dans " + sortieFichier);
                            } catch (IOException e) {
                                System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
                            }
                        }
                        case 3 -> {
                            sortie = true;
                            System.out.println("Programme terminé.");
                        }
                        default -> System.out.println("Choix invalide. Veuillez réessayer.");
                    }
                }
                scanner.close();
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            }
        } else {
            // Démarrage de l'interface utilisateur interactive
            InterfaceUtilisateur u = new InterfaceUtilisateur();
            u.demarrer();
        }
    }

    /**
     * Permet de lire et valider le choix de l'utilisateur à partir du menu.
     * @param scanner L'objet Scanner utilisé pour lire l'entrée utilisateur.
     * @return Un entier représentant le choix valide de l'utilisateur.
     */
    private static int getChoixUtilisateur(Scanner scanner) {
        int choix = -1;

        while (choix < 1 || choix > 3) {
            try {
                System.out.print("Votre choix : ");
                choix = Integer.parseInt(scanner.nextLine());
                if (choix < 1 || choix > 3) {
                    System.out.println("Votre choix doit être entre 1 (inclus) et 3 (inclus).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
            }
        }
        return choix;
    }
}
