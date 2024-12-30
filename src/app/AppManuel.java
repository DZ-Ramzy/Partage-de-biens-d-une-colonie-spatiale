package app;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import partie1.Colonie;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * La classe AppManuel gère une interface utilisateur JavaFX pour configurer et simuler 
 * une colonie de colons, leurs préférences, leurs relations, et leurs affectations de ressources.
 * Elle utilise la classe Colonie pour manipuler les données sous-jacentes.
 */
public class AppManuel {

	private Colonie colonie;
	private Map<String, String> solution;
	
	/**
     * Constructeur par défaut initialisant une nouvelle colonie.
     */
	public AppManuel() {
		colonie = new Colonie();
	}
	
	/**
     * Affiche l'écran principal pour entrer le nombre de colons.
     *
     * @param stage La fenêtre principale de l'application.
     */
	public void afficher(Stage stage) {

		VBox mainLayout = new VBox(10);
		mainLayout.setStyle("-fx-padding: 10; -fx-spacing: 10;");

		Scene scene = new Scene(mainLayout, 600, 400);

		Label titleLabel = new Label("Entrez le nombre de colons (max 26)");
		titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

		TextField colonCountField = new TextField();

		Button initializeButton = new Button("Initialiser la colonie");
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red;");

		initializeButton.setOnAction(e -> {
			try {
				int colonCount = Integer.parseInt(colonCountField.getText());
				if (colonCount <= 0 || colonCount > 26) {
					throw new IllegalArgumentException("Le nombre de colons doit être entre 1 et 26.");
				}

				for (int i = 0; i < colonCount; i++) {
					colonie.ajouterColon(String.valueOf((char) ('A' + i)));
				}
				initialiserRessources(colonCount);
				afficherMenuConfiguration(stage);
			} catch (NumberFormatException ex) {
				errorLabel.setText("Veuillez entrer un nombre entier valide.");
			} catch (IllegalArgumentException ex) {
				errorLabel.setText(ex.getMessage());
			}
		});

		mainLayout.getChildren().addAll(titleLabel, colonCountField, initializeButton, errorLabel);

		stage.setTitle("Gestion de la colonie");
		stage.setScene(scene);
		stage.show();
	}
	
	/**
     * Initialise les ressources pour la colonie en fonction du nombre de colons.
     *
     * @param nombreColons Le nombre total de colons.
     */
	private void initialiserRessources(int nombreColons) {
		colonie.getRessources().clear();
		for (int i = 1; i <= nombreColons; i++) {
			colonie.ajouterRessource(String.valueOf(i));
		}
	}
	
	/**
     * Affiche le menu de configuration pour ajouter des relations, des préférences, 
     * ou terminer la configuration.
     *
     * @param stage La fenêtre principale de l'application.
     */
	private void afficherMenuConfiguration(Stage stage) {
		VBox configLayout = new VBox(10);
		configLayout.setStyle("-fx-padding: 10; -fx-spacing: 10;");

		Label menuLabel = new Label("Menu de configuration");
		menuLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

		Button addRelationButton = new Button("Ajouter une relation 'ne s’aiment pas'");
		Button addPreferencesButton = new Button("Ajouter les préférences d’un colon");
		Button finishButton = new Button("Terminer la configuration");

		addRelationButton.setOnAction(e -> afficherAjoutRelation(stage));
		addPreferencesButton.setOnAction(e -> afficherAjoutPreferences(stage));
		finishButton.setOnAction(e -> {
			if (colonie.estConfigurationComplete()) {
				solution = colonie.genererSolutionNaive();
				afficherMenuSimulation(stage);
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Tous les colons doivent avoir des préférences complètes avant de terminer.");
				alert.showAndWait();
			}
		});

		configLayout.getChildren().addAll(menuLabel, addRelationButton, addPreferencesButton, finishButton);
		stage.getScene().setRoot(configLayout);
	}
	
	/**
     * Affiche un formulaire pour ajouter une relation entre deux colons.
     *
     * @param primaryStage La fenêtre principale de l'application.
     */
	private void afficherAjoutRelation(Stage primaryStage) {
		VBox relationLayout = new VBox(10);

		Label titleLabel = new Label("Entrez les noms de deux colons séparés par un espace");
		TextField relationField = new TextField();
		Button addButton = new Button("Ajouter");
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red;");

		addButton.setOnAction(e -> {
			try {
				String[] noms = relationField.getText().split(" ");
				if (noms.length != 2) {
					throw new IllegalArgumentException("Veuillez entrer exactement deux noms de colons.");
				}
				colonie.ajouterRelation(noms[0], noms[1]);
				afficherMenuConfiguration(primaryStage);
			} catch (IllegalArgumentException ex) {
				errorLabel.setText(ex.getMessage());
			}
		});

		relationLayout.getChildren().addAll(titleLabel, relationField, addButton, errorLabel);
		primaryStage.getScene().setRoot(relationLayout);
	}
	
	/**
     * Affiche un formulaire pour ajouter les préférences d'un colon.
     *
     * @param primaryStage La fenêtre principale de l'application.
     */
	private void afficherAjoutPreferences(Stage primaryStage) {
		VBox preferencesLayout = new VBox(10);

		Label titleLabel = new Label("Entrez le nom du colon suivi de ses préférences (ex : A 1 2 3 ...)");
		TextField preferencesField = new TextField();
		Button addButton = new Button("Ajouter");
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red;");

		addButton.setOnAction(e -> {
			try {
				String[] input = preferencesField.getText().split(" ");
				String nom = input[0];
				List<String> preferences = Arrays.asList(Arrays.copyOfRange(input, 1, input.length));
				colonie.ajouterPreferences(nom, preferences);
				afficherMenuConfiguration(primaryStage);
			} catch (IllegalArgumentException ex) {
				errorLabel.setText(ex.getMessage());
			}
		});

		preferencesLayout.getChildren().addAll(titleLabel, preferencesField, addButton, errorLabel);
		primaryStage.getScene().setRoot(preferencesLayout);
	}
	
	/**
     * Affiche le menu de simulation où l'utilisateur peut simuler les échanges 
     * de ressources, vérifier les colons jaloux, ou terminer la simulation.
     *
     * @param primaryStage La fenêtre principale de l'application.
     */
	private void afficherMenuSimulation(Stage primaryStage) {
		VBox simulationLayout = new VBox(10);
		simulationLayout.setStyle("-fx-padding: 10; -fx-spacing: 10;");

		Label menuLabel = new Label("Menu de simulation");
		menuLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

		Button exchangeButton = new Button("Échanger les ressources de deux colons");
		Button jealousyButton = new Button("Afficher le nombre de colons jaloux");
		Button finishButton = new Button("Terminer la simulation");

		TextArea affectationsArea = new TextArea();
		affectationsArea.setEditable(false);
		affectationsArea.setPrefHeight(150);

		exchangeButton.setOnAction(e -> afficherEchangeRessources(primaryStage, affectationsArea));
		jealousyButton.setOnAction(e -> afficherColonsJaloux());
		finishButton.setOnAction(e -> primaryStage.close());

		simulationLayout.getChildren().addAll(menuLabel, exchangeButton, jealousyButton, finishButton,
				affectationsArea);

		// Initial display of assignments in the TextArea
		updateAffectationsDisplay(affectationsArea);

		primaryStage.getScene().setRoot(simulationLayout);
	}
	
	/**
     * Affiche un formulaire pour échanger les ressources entre deux colons.
     *
     * @param primaryStage La fenêtre principale de l'application.
     * @param affectationsArea La zone de texte pour afficher les affectations actuelles.
     */
	private void afficherEchangeRessources(Stage primaryStage, TextArea affectationsArea) {
		VBox exchangeLayout = new VBox(10);

		Label titleLabel = new Label("Entrez les noms de deux colons séparés par un espace");
		TextField exchangeField = new TextField();
		Button exchangeButton = new Button("Échanger");
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red;");

		exchangeButton.setOnAction(e -> {
			try {
				String[] noms = exchangeField.getText().split(" ");
				if (noms.length != 2) {
					throw new IllegalArgumentException("Veuillez entrer exactement deux noms de colons.");
				}

				String ressource1 = solution.get(noms[0]);
				String ressource2 = solution.get(noms[1]);

				if (ressource1 != null && ressource2 != null) {
					solution.put(noms[0], ressource2);
					solution.put(noms[1], ressource1);
				} else {
					throw new IllegalArgumentException("Les ressources des colons sont inexistantes.");
				}

				// Update the affectations display
				updateAffectationsDisplay(affectationsArea);
				// Retour au menu de simulation après l'échange
				afficherMenuSimulation(primaryStage);

			} catch (IllegalArgumentException ex) {
				errorLabel.setText(ex.getMessage());
			}
		});

		exchangeLayout.getChildren().addAll(titleLabel, exchangeField, exchangeButton, errorLabel);
		primaryStage.getScene().setRoot(exchangeLayout);
	}
	
	/**
     * Met à jour l'affichage des affectations de ressources dans une zone de texte.
     *
     * @param affectationsArea La zone de texte où afficher les affectations.
     */
	private void updateAffectationsDisplay(TextArea affectationsArea) {
		// Capture the output of afficherAffectations() and redirect to the TextArea
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		System.setOut(printStream);

		colonie.afficherAffectations();

		// Set the captured output in the TextArea
		affectationsArea.setText(outputStream.toString());

		// Restore the original System.out
		System.setOut(System.out);
	}
	
	/**
     * Affiche une alerte contenant le nombre de colons jaloux calculé par la colonie.
     */
	private void afficherColonsJaloux() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("Nombre de colons jaloux : " + colonie.calculerCout());
		alert.showAndWait();
	}
}