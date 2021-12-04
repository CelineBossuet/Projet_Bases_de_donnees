import java.util.Scanner;

public class InterfaceUtilisateur {

	ManagerDatabase mdb = new ManagerDatabase();

	public void start() {
		System.out.println("Bienvenue dans le Gange");
		mdb.setup();
		mdb.connect();
		int option = -1;
		Scanner scanner = new Scanner(System.in);
		while (option != 0) {
			System.out.println("------------------------------------------------");
			System.out.println("Que voulez-vous faire ?");
			System.out.println("1 - Connexion utilisateur");
			System.out.println("2 - Afficher un produit");
			System.out.println("3 - Afficher les catégories");
			System.out.println("4 - Afficher les produits d'une catégorie");
			System.out.println("5 - Afficher les utilisateurs");
			System.out.println("0 - Quitter l'application");
			String optionUser = scanner.next();
			scanner.nextLine();
			switch (Integer.valueOf(optionUser)) {
				case 0:
					// Quitter l'application en fermant la connexion
					mdb.close();
					System.out.println("Au revoir, a bientot :)");
					return;
				case 1:
					System.out.println("Saisir les informations de connexion :");
					System.out.println("Mail :");
					String mailUser = scanner.next();
					scanner.nextLine();
					System.out.println("Mot de passe :");
					String mdpUser = scanner.next();
					scanner.nextLine();
					mdb.connexionUser(mailUser, mdpUser);
					break;
				case 2:
					System.out.println("Saisir le numéro du produit :");
					String ID=scanner.next();
					scanner.nextLine();
					mdb.ficheProduit(Integer.valueOf(ID));
					break;
				case 3:
					mdb.afficheProduitCategorie();
					break;
				case 4:
					System.out.println("Saisir la catégorie recherchée :");
					String categorie = scanner.next();
					scanner.nextLine();
					mdb.afficheProduits(categorie);
					break;
				case 5:
					mdb.afficherUser();
					break;
				default:
					// Ne rien faire
					break;
			}
		}
		scanner.close();
	}

}
