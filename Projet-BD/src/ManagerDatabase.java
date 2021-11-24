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
			connection.commit();
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

	public void supprimer(String mailUser) {
		try {
			Savepoint save1 = connection.setSavepoint("Suppression");

			// Chercher un nouvel ID inutilisé
			Statement statmt = connection.createStatement();
			ResultSet res = statmt.executeQuery("SELECT max(id_uti) FROM ID_UTILISATEUR");
			int newID = 1;
			if (res.next()) {
				newID = res.getInt(1) + 1;
			}
			else {
				System.out.println("Aucun utilisateur à supprimer");
				connection.rollback(save1);
				return;
			}


			// Créer un nouvel ID et l'insérer dans les ID_utilisteurs
			PreparedStatement statmt2 = connection.prepareStatement(
					"INSERT INTO ID_UTILISATEUR VALUES (?)"
					);
			statmt2.setInt(1, newID);
			statmt2.executeUpdate();



			// Chercher l'ID de l'utilisateur
			PreparedStatement statmt3 = connection.prepareStatement(
					"SELECT id_uti FROM UTILISATEUR WHERE mail LIKE ?"
					);
			statmt3.setString(1, mailUser);
			ResultSet res3 = statmt3.executeQuery();
			int idUser;
			if (res3.next()) {
				idUser = res3.getInt("id_uti");
			}
			else {
				System.out.println("Aucun utilisateur ne possède ce mail");
				connection.rollback(save1);
				return;
			}


			// Chercher toutes les offres dont il faut modifier l'ID utilisateur
			PreparedStatement statmt6 = connection.prepareStatement(
					"SELECT ID_prod, date_heure, ID_uti FROM OFFRE WHERE ID_uti = ?"
					);
			statmt6.setInt(1, idUser);
			ResultSet res6 = statmt6.executeQuery();
			while (res6.next()) {
				// Modifier les offres réalisées par cet utilisateur
				PreparedStatement statmt4 = connection.prepareStatement(
						"UPDATE OFFRE SET id_uti = ? WHERE id_uti = ? AND date_heure = ? AND ID_prod = ?"
						);
				statmt4.setInt(1, newID);
				statmt4.setInt(2, idUser);
				statmt4.setTimestamp(3, res6.getTimestamp("date_heure"));
				statmt4.setInt(4, res6.getInt("ID_prod"));
				statmt4.executeUpdate();
			}


			// Supprimer l'utilisateur et son ID
			PreparedStatement statmt5 = connection.prepareStatement(
					"DELETE FROM UTILISATEUR WHERE mail LIKE ?"
					);
			statmt5.setString(1, mailUser);
			statmt5.executeUpdate();
			PreparedStatement statmt7 = connection.prepareStatement(
					"DELETE FROM ID_UTILISATEUR WHERE id_uti = ?"
					);
			statmt7.setInt(1, idUser);
			statmt7.executeUpdate();


			System.out.println("Suppression terminée");
			connection.commit();

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
					afficheProduitCategorie();
					break;
				case 2:
                    afficheRecommandationsPerso(mail);
                    recommandationsGenerales(mail);
					break;
				default:
					// Ne rien faire
					break;
			}
		}


	}

	public void afficheProduitCategorie() {
		try {
			Statement statmt = connection.createStatement();
			ResultSet res = statmt.executeQuery("SELECT * FROM CATEGORIE WHERE nom_cat NOT IN (SELECT nom_cat FROM EST_LA_SOUS_CATEGORIE_DE)");
			/*
			Resultats1 = Requete(Entree0) puis for Entree1 in Resultats1
			{ Resultats2 += Requete(Entree1) } etc... (plein de JOIN)
			SELECT nom_cat FROM EST_LA_SOUS_CATEGORIE_DE WHERE nom_pere = '?';
			*/
			while (res.next()) {
				System.out.println(
						" -> " + res.getString("nom_cat")
						);
				afficheProduitCategorieRecurs(1, res.getString("nom_cat"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Boolean afficheProduitCategorieRecurs(int nb_pere, String pere) {
		Boolean est_pere = false;
		try {
			PreparedStatement statmt = connection.prepareStatement(
					"SELECT * FROM EST_LA_SOUS_CATEGORIE_DE WHERE nom_pere LIKE ?"
					);
			statmt.setString(1, pere);
			ResultSet res = statmt.executeQuery();
			while (res.next()) {
				est_pere = true;
				System.out.print(" ");
				for (int i = 0; i < nb_pere; i++) {
					System.out.print("-");
				}
				System.out.println(
						"-> " + res.getString("nom_cat")
						);
				afficheProduitCategorieRecurs(nb_pere +1, res.getString("nom_cat"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return est_pere;
	}

	public void afficheRecommandationsPerso(String mail)
	{
		try
		{
			PreparedStatement statmt = connection.prepareStatement(
											"SELECT nom_cat, COUNT(*) AS nb_offres FROM (SELECT * FROM OFFRE "
											+ "JOIN PRODUIT ON PRODUIT.ID_prod = OFFRE.ID_prod "
											+ "JOIN UTILISATEUR ON UTILISATEUR.ID_uti = OFFRE.ID_uti "
											+ "WHERE UTILISATEUR.mail = ? AND NOT EXISTS (SELECT * FROM REMPORTE JOIN OFFRE OFR "
											+ "ON REMPORTE.ID_prod = OFR.ID_prod AND REMPORTE.date_heure = OFR.date_heure "
											+ "WHERE OFR.ID_uti = OFFRE.ID_uti AND OFFRE.ID_prod = REMPORTE.ID_prod)) "
											+ "GROUP BY nom_cat ORDER BY nb_offres DESC, nom_cat");
			statmt.setString(1, mail);
			ResultSet res = statmt.executeQuery();
			while (res.next()) {
				System.out.println("-> " + res.getString("nom_cat"));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void recommandationsGenerales(String mail) {
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT nom_cat, AVG(nb_offres) AS moyenne_offre FROM (SELECT prod.ID_prod, prod.nom_cat, COUNT(*) AS nb_offres FROM PRODUIT prod   JOIN OFFRE "
					+ "    ON OFFRE.ID_prod = prod.ID_prod"
					+ "    GROUP BY prod.ID_prod, prod.nom_cat"
					+ "    UNION"
					+ "    SELECT prod1.ID_prod, prod1.nom_cat, 0 as nb_offres from PRODUIT prod1"
					+ "    WHERE prod1.ID_prod NOT IN (SELECT OFFRE.ID_prod from OFFRE)"
					+ "    GROUP BY prod1.ID_prod, prod1.nom_cat)"
					+ "    WHERE nom_cat not in (	SELECT DISTINCT PRODUIT.nom_cat FROM OFFRE "
					+ "    						JOIN PRODUIT ON PRODUIT.ID_prod = OFFRE.ID_prod"
					+ "    						JOIN UTILISATEUR ON UTILISATEUR.ID_uti = OFFRE.ID_uti"
					+ "    						WHERE UTILISATEUR.mail = ? AND NOT EXISTS (SELECT * FROM REMPORTE JOIN OFFRE OFR "
					+ "                         ON REMPORTE.ID_prod = OFR.ID_prod AND REMPORTE.date_heure = OFR.date_heure "
					+ "                         WHERE OFR.ID_uti = OFFRE.ID_uti AND OFFRE.ID_prod = REMPORTE.ID_prod))"
					+ "	GROUP BY nom_cat"
					+ "	ORDER BY moyenne_offre DESC, nom_cat ");
			String cpy_mail = new String(mail);
			stmt.setString(1, cpy_mail);
			ResultSet res = stmt.executeQuery();
			while (res.next()) {
				System.out.println("->" + res.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
