package partie1;

import java.util.*;

/**
 * La classe InterfaceUtilisateur gère l'interaction entre l'utilisateur et le
 * programme. Elle permet de configurer une colonie, d'ajouter des relations et
 * des préférences, et de simuler les affectations des ressources.
 */
public class InterfaceUtilisateur {

	/** Scanner utilisé pour lire les entrées de l'utilisateur. */
	private Scanner scanner;

	/** La colonie à configurer et à gérer. */
	private Colonie colonie;
	
	/** Indicateur pour la fin de la configuration. */
	private boolean finConfiguration = false;

	/**
	 * Constructeur de la classe InterfaceUtilisateur. Initialise le scanner et crée
	 * une nouvelle colonie.
	 */
	public InterfaceUtilisateur() {
		scanner = new Scanner(System.in);
		colonie = new Colonie();
	}

	/**
	 * Démarre l'interface utilisateur pour configurer et gérer la colonie.
	 */
	public void demarrer() {
		int n = 0;
		while (n <= 0 || n > 26) {
			try {
				System.out.println("Entrez le nombre de colons (max 26) :");
				n = scanner.nextInt();
				scanner.nextLine(); // Consommer le retour à la ligne
				if (n <= 0 || n > 26) {
					System.out.println("Erreur : Le nombre de colons doit être entre 1 et 26.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
				scanner.nextLine(); // Consommer l'entrée invalide
			}
		}

		// Ajouter les colons et initialiser les ressources
		for (int i = 0; i < n; i++) {
			colonie.ajouterColon(String.valueOf((char) ('A' + i)));
		}
		initialiserRessources(n);

		while (!finConfiguration) {
			System.out.println("Menu :");
			System.out.println("1) Ajouter une relation 'ne s’aiment pas'");
			System.out.println("2) Ajouter les préférences d’un colon");
			System.out.println("3) Fin");

			int choix = -1;
			while (choix < 1 || choix > 3) {
				try {
					choix = scanner.nextInt();
					scanner.nextLine(); // Consommer le retour à la ligne
					if (choix < 1 || choix > 3) {
						System.out.println("Erreur : Choix incorrect. Veuillez choisir entre 1 et 3.");
					}
				} catch (InputMismatchException e) {
					System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
					scanner.nextLine(); // Consommer l'entrée invalide
				}
			}

			switch (choix) {
			case 1 -> ajouterRelation();
			case 2 -> ajouterPreferences();
			case 3 -> verifierFinConfiguration();
			}
		}

		Map<String, String> solution = colonie.genererSolutionNaive();
		colonie.afficherAffectations();

		// Simulation avec échange des ressources
		boolean finSimulation = false;
		while (!finSimulation) {
			System.out.println("Menu :");
			System.out.println("1) Échanger les ressources de deux colons");
			System.out.println("2) Afficher le nombre de colons jaloux");
			System.out.println("3) Fin");

			int choix = -1;
			while (choix < 1 || choix > 3) {
				try {
					choix = scanner.nextInt();
					scanner.nextLine(); // Consommer le retour à la ligne
					if (choix < 1 || choix > 3) {
						System.out.println("Erreur : Choix incorrect. Veuillez choisir entre 1 et 3.");
					}
				} catch (InputMismatchException e) {
					System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
					scanner.nextLine(); // Consommer l'entrée invalide
				}
			}

			switch (choix) {
			case 1 -> echangerRessources(solution);
			case 2 -> afficherColonsJaloux();
			case 3 -> finSimulation = true;
			}
		}

		scanner.close();
	}

	/**
	 * Initialise les ressources disponibles dans la colonie.
	 *
	 * @param nombreColons le nombre de colons (et donc de ressources).
	 */
	private void initialiserRessources(int nombreColons) {
		colonie.getRessources().clear(); // Assurez-vous qu'il n'y a pas d'anciennes ressources
		for (int i = 1; i <= nombreColons; i++) {
			colonie.ajouterRessource(String.valueOf(i)); // Ajout des ressources numérotées
		}
	}

	/**
	 * Permet d'ajouter une relation "ne s’aiment pas" entre deux colons.
	 */
	private void ajouterRelation() {
		boolean relationAjoutee = false;
		while (!relationAjoutee) {
			try {
				System.out.println("Colons disponibles : " + String.join(", ", colonie.getColons().keySet()));
				System.out.println("Entrez les noms des deux colons séparés par un espace :");
				String[] noms = scanner.nextLine().split(" ");
				if (noms.length != 2) {
					throw new IllegalArgumentException("Veuillez entrer exactement deux noms de colons.");
				}
				colonie.ajouterRelation(noms[0], noms[1]);
				relationAjoutee = true; // Relation ajoutée avec succès
			} catch (IllegalArgumentException e) {
				System.out.println("Erreur : " + e.getMessage());
			}
		}
	}

	/**
	 * Permet d'ajouter les préférences d'un colon.
	 */
	private void ajouterPreferences() {
		boolean preferencesAjoutees = false;
		while (!preferencesAjoutees) {
			try {
				System.out.println("Ressources disponibles : " + String.join(", ", colonie.getRessources()));
				System.out.println("Entrez le nom du colon suivi de ses préférences (ex : A 1 2 3 ...) :");
				String[] input = scanner.nextLine().split(" ");
				String nom = input[0];
				List<String> preferences = Arrays.asList(Arrays.copyOfRange(input, 1, input.length));
				colonie.ajouterPreferences(nom, preferences);
				preferencesAjoutees = true; // Préférences ajoutées avec succès
			} catch (IllegalArgumentException e) {
				System.out.println("Erreur : " + e.getMessage());
			}
		}
	}

	/**
	 * Vérifie si la configuration est complète et termine si tous les colons ont
	 * des préférences.
	 */
	private void verifierFinConfiguration() {
		List<String> colonsSansPreferences = new ArrayList<>();
		for (String colonNom : colonie.getColons().keySet()) {
			if (colonie.getColons().get(colonNom).getPreferences() == null) {
				colonsSansPreferences.add(colonNom);
			}
		}
		if (colonsSansPreferences.isEmpty()) {
			System.out.println("Configuration terminée.");
			finConfiguration = true;
		} else {
			System.out.println(
					"Erreur : Les colons suivants n'ont pas de préférences complètes : " + colonsSansPreferences);
		}
	}

	/**
	 * Permet d'échanger les ressources de deux colons.
	 *
	 * @param solution la solution actuelle des affectations.
	 */
	private void echangerRessources(Map<String, String> solution) {
		boolean echangeEffectue = false;
		while (!echangeEffectue) {
			try {
				System.out.println("Entrez les noms des deux colons à échanger :");
				String[] noms = scanner.nextLine().split(" ");
				if (noms.length != 2) {
					throw new IllegalArgumentException("Veuillez entrer exactement deux colons.");
				}
				if (!colonie.colonExiste(noms[0]) || !colonie.colonExiste(noms[1])) {
					throw new IllegalArgumentException("Les colons doivent exister.");
				}

				String ressource1 = solution.get(noms[0]);
				String ressource2 = solution.get(noms[1]);

				if (ressource1 != null && ressource2 != null) {
					solution.put(noms[0], ressource2);
					solution.put(noms[1], ressource1);
					echangeEffectue = true; // Échange effectué avec succès
				} else {
					throw new IllegalArgumentException("Les ressources des colons sont inexistantes.");
				}
				colonie.afficherAffectations();
			} catch (IllegalArgumentException e) {
				System.out.println("Erreur : " + e.getMessage());
			}
		}
	}

	/**
	 * Affiche le nombre de colons jaloux.
	 */
	private void afficherColonsJaloux() {
		System.out.println("Nombre de colons jaloux : " + colonie.calculerCout());
	}
}
