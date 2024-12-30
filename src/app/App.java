package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import partie1.Colonie;
import partie2.AnaliseFichier;
import partie2.SolutionOptimise;
import partie2.SolutionSauvegarde;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Classe principale de l'application de gestion de colonie.
 * Cette application JavaFX permet de charger un fichier contenant les données d'une colonie,
 * de générer une solution optimisée et de sauvegarder cette solution dans un fichier.
 */
public class App extends Application {

    /**
     * Objet représentant la colonie chargée depuis un fichier.
     */
    private Colonie colonie;

    /**
     * Solution optimisée générée pour la colonie.
     * Les clés et valeurs représentent des informations spécifiques à la solution.
     */
    private Map<String, String> solution;

    /**
     * Zone de texte permettant d'afficher des messages d'information ou d'erreur à l'utilisateur.
     */
    private TextArea textArea;

    /**
     * Point d'entrée principal de l'application JavaFX.
     * @param primaryStage la fenêtre principale de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        colonie = new Colonie();
        solution = null;

        primaryStage.setTitle("Gestion de Colonie");

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Boutons de l'application
        Button btnChargerFichier = new Button("Charger un fichier");
        Button btnOptimiserSolution = new Button("Résolution automatique");
        Button btnSauvegarderSolution = new Button("Sauvegarder la solution");

        // Désactiver les boutons jusqu'à ce qu'une action préalable soit réalisée
        btnOptimiserSolution.setDisable(true);
        btnSauvegarderSolution.setDisable(true);

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(300);

        Button btnAccederAppManuel = new Button("Accéder à l'Interface Utilisateur");
        btnAccederAppManuel.setOnAction(event -> {
            Stage appManuelStage = new Stage();
            AppManuel appManuel = new AppManuel();
            appManuel.afficher(appManuelStage);
        });

        // Gestion des actions des boutons
        btnChargerFichier.setOnAction(event -> {
            chargerFichier(primaryStage);
            if (colonie != null && !colonie.getColons().isEmpty()) {
                btnOptimiserSolution.setDisable(false);
            }
        });

        btnOptimiserSolution.setOnAction(event -> {
            optimiserSolution();
            if (solution != null && !solution.isEmpty()) {
                btnSauvegarderSolution.setDisable(false);
            }
        });

        btnSauvegarderSolution.setOnAction(event -> sauvegarderSolution(primaryStage));

        // Ajouter les composants à l'interface
        root.getChildren().addAll(btnChargerFichier, btnAccederAppManuel, btnOptimiserSolution, 
                                   btnSauvegarderSolution, textArea);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Ouvre un sélecteur de fichier pour charger un fichier de colonie et analyser son contenu.
     * @param stage la fenêtre de l'application pour afficher le sélecteur de fichier
     */
    private void chargerFichier(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier de colonie");
        File fichier = fileChooser.showOpenDialog(stage);

        if (fichier != null) {
            try {
                AnaliseFichier analyse = new AnaliseFichier(fichier.getAbsolutePath());
                colonie = analyse.analiseFichier();

                if (colonie == null || colonie.getColons().isEmpty()) {
                    throw new IllegalArgumentException("Le fichier chargé ne contient pas de données valides.");
                }

                afficherMessage("Données de la colonie chargées depuis le fichier : " + fichier.getName());
            } catch (IOException | IllegalArgumentException e) {
                afficherMessage("Erreur lors de la lecture du fichier : " + e.getMessage());
            }
        }
    }

    /**
     * Génère une solution optimisée pour la colonie chargée.
     */
    private void optimiserSolution() {
        if (colonie != null) {
            SolutionOptimise optimise = new SolutionOptimise(colonie);
            solution = optimise.optimiseSolution(colonie.getColons().size() * 10);
            afficherMessage("Solution optimisée : " + solution);
            afficherMessage("Coût de la solution : " + colonie.calculerCout());
        }
    }

    /**
     * Sauvegarde la solution optimisée dans un fichier choisi par l'utilisateur.
     * @param stage la fenêtre de l'application pour afficher le sélecteur de fichier
     */
    private void sauvegarderSolution(Stage stage) {
        if (solution != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la solution");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));
            File fichier = fileChooser.showSaveDialog(stage);

            if (fichier != null) {
                try {
                    // Si le fichier n'existe pas, il sera créé automatiquement.
                    SolutionSauvegarde.sauvegardeSolution(fichier.getAbsolutePath(), solution);
                    afficherMessage("Solution sauvegardée dans le fichier : " + fichier.getName());
                } catch (IOException e) {
                    afficherMessage("Erreur lors de la sauvegarde : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Affiche un message dans la zone de texte de l'application et dans la console.
     * @param message le message à afficher
     */
    private void afficherMessage(String message) {
        System.out.println(message);
        textArea.appendText(message + "\n");
    }

    /**
     * Méthode principale permettant de lancer l'application JavaFX.
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }
}

