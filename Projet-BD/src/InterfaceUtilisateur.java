import java.util.Scanner;

public class InterfaceUtilisateur {
	
	ManagerDatabase mdb = new ManagerDatabase();
	
	public void start() {
		System.out.println("Bienvenue dans de Gange");
		mdb.setup();
		mdb.connect();
		int option = -1;
		Scanner scanner = new Scanner(System.in);
		while (option != 0) {
			System.out.println("------------------------------------------------");
			System.out.println("Que voulez-vous faire ?");
			System.out.println("1 - Connexion utilisateur");
			
			System.out.println("9 - Afficher les utilisateurs");
			System.out.println("10 - Supprimer un utilisateur");
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
				case 9:
					mdb.afficherUser();
					break;
				case 10:
					String idUser = scanner.next();
					scanner.nextLine();
					mdb.supprimer(Integer.valueOf(idUser));
					break;
				default:
					// Ne rien faire
					break;
			}
		}
	}

}
