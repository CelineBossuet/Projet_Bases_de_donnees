import java.sql.*;
import java.util.Scanner;

public class ManagerDatabase {
	
	Connection connection;
	
	public void setup() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void connect() {
		try {
			System.out.println("Connexion ici");
			String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
			String user = "ganderf";
			String password = "ganderf";
			
			connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Requêtes
				//requete();
				
				// Mettre des checkpoint si besoin
				//Savepoint save1 = connection.setSavepoint("Nom du Checkpoint 1");
				
				// Choisir si validation ou non
				//connection.rollback(); 
				//connection.rollback(save1);
				//connection.commit();
	
	
	public void close() {
		try {
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void requete() {
		try {
			Statement statmt = connection.createStatement();
			ResultSet res = statmt.executeQuery("SELECT * FROM emp");
			//ResultSetMetaData rmd =  res.getMetaData();
			//int size = res.getFetchSize();
			while (res.next()) {
				System.out.println(
						"Employe " + res.getString("ename")
						+ " -> salaire : " + res.getInt("sal")
						// getInt(numeroColonne) fonctionne aussi
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void requeteParam() {
		try {
			PreparedStatement statmt = connection.prepareStatement(
					"SELECT * FROM emp WHERE ename LIKE ?"
					);
			
			System.out.println("Nom de l'employe : ");
			Scanner scan = new Scanner(System.in);
			String empName = scan.next();
			scan.nextLine();
			
			statmt.setString(1, empName);
			ResultSet res = statmt.executeQuery();
			
			// Metadonnées associées, existe aussi pour toute la base de données
			ResultSetMetaData rmd = res.getMetaData();
			
			// Utiliser .next() et .previous() 
			while (res.next()) {
				System.out.println(
						"Employe " + res.getString("ename")
						+ " -> salaire : " + res.getInt("sal")
						// getInt(numeroColonne) fonctionne aussi
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void supprimer(int idUser) {
		try {
			PreparedStatement statmt = connection.prepareStatement(
					"DELETE FROM UTILISATEUR WHERE ID_uti = ?"
					);
			statmt.setInt(1, idUser);
			statmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void afficherUser() {
		try {
			Statement statmt = connection.createStatement();
			ResultSet res = statmt.executeQuery("SELECT * FROM UTILISATEUR");
			while (res.next()) {
				System.out.println(
						" -> ID " + res.getInt("ID_uti") + " : "+ res.getString("prenom") + " " + res.getString("nom")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void connexionUser(String mail, String mdp) {
		try {
			PreparedStatement statmt = connection.prepareStatement(
					"SELECT * FROM UTILISATEUR WHERE mail LIKE ? AND mdp LIKE ?"
					);
			statmt.setString(1, mail);
			statmt.setString(2, mdp);
			ResultSet res = statmt.executeQuery();
			
			if (res.next()) {
				System.out.println(
						res.getString("prenom") + " " + res.getString("nom") + " connecté !"
						);
				espaceUser(mail);
			}
			else {
				System.out.println("Mauvaise mot de passe/mail");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void espaceUser(String mail) {
		while (true) {
			System.out.println("Session de " + mail + ", que souhaitez-vous faire ?");
			System.out.println("1 - Afficher les catégories & sous-catégories");
			System.out.println("2 - Afficher les catégories & sous-catégories recommandés");
			System.out.println("0 - Déconnexion");
			
			Scanner scan = new Scanner(System.in);
			String optionUser = scan.next();
			scan.nextLine();
			switch (Integer.valueOf(optionUser)) {
				case 0:
					System.out.println("Déconnexion validée");
					return;
				case 1:
					afficheCategorie();
					break;
				case 2:
					break;
				default:
					// Ne rien faire
					break;
			}
		}

		
	}

	private void afficheCategorie() {
		try {
			Statement statmt = connection.createStatement();
			ResultSet res = statmt.executeQuery("SELECT * FROM UTILISATEUR");
			/*
			Resultats1 = Requete(Entree0) puis for Entree1 in Resultats1
			{ Resultats2 += Requete(Entree1) } etc... (plein de JOIN)
			SELECT nom_cat FROM EST_LA_SOUS_CATEGORIE_DE WHERE nom_pere = '?';
			*/
			while (res.next()) {
				System.out.println(
						" -> ID " + res.getInt("ID_uti") + " : "+ res.getString("prenom") + " " + res.getString("nom")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
