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
			String user = "chahbana";
			String password = "chahbana";

			connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			// Valider si des changements ont été réalisés puis fermer
			connection.commit();
			connection.close();

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
			// Affiche tout les utilisateurs (ID et noms)
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
			// Recherche de l'utilisateur correspondant
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

	public void ficheProduit(int Id_prod) {
		try {
			// Informations sur un produit
			PreparedStatement stat = connection.prepareStatement("SELECT * FROM PRODUIT WHERE ID_prod=?");
			stat.setInt(1, Id_prod);
			ResultSet res = stat.executeQuery();
			
			PreparedStatement caract = connection.prepareStatement("SELECT * FROM CARACTERISTIQUE WHERE ID_prod=?");
			caract.setInt(1, Id_prod);
			ResultSet cara = stat.executeQuery();


			if (res.next()) {
				System.out.println("Voici la fiche complète du produit numéro "+ Id_prod);
				System.out.println("intitulé : " + res.getString("intitule"));
				System.out.println("prix : " +res.getInt("prix_produit") +" euros");
				System.out.println("description du produit : \n" + res.getString("texte"));
				System.out.println("URL : " + res.getString("URL"));
				System.out.println("présent dans la catégorie : "+res.getString("nom_cat"));
				while (cara.next()){
					System.out.println("Caractéristique " + cara.getString("nom_cara") + " : " + cara.getString("valeur"));
				}

			}else {
				System.out.println("Le produit n'existe pas");
			}


		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void enchere(int ID_prod, int prix_propose, String mail) {
		try {
			// Récupérer l'ID de l'utilisateur correspondant
			PreparedStatement sta = connection.prepareStatement("SELECT ID_uti FROM UTILISATEUR WHERE mail=?");
			sta.setString(1,  mail);
			ResultSet r = sta.executeQuery();
			r.next();

			// Récupérer l'ID du produit correspondant et vérifier qu'il ne soit ni déjà acheté, ni trop cher par rapport au prix proposé
			PreparedStatement statmt = connection.prepareStatement("SELECT ID_prod FROM PRODUIT WHERE ID_prod=? AND prix_produit < ? AND ID_prod NOT IN (SELECT ID_prod FROM REMPORTE)");
			statmt.setInt(1, ID_prod);
			statmt.setInt(2, prix_propose);
			ResultSet res = statmt.executeQuery();
			
			if(res.next() ) {
				// Ajouter l'offre dans la base de donnée
				PreparedStatement statm = connection.prepareStatement("INSERT INTO OFFRE VALUES(?, (SELECT LOCALTIMESTAMP FROM dual), ?, ?)");
				statm.setInt(1, res.getInt("ID_prod"));
				statm.setInt(2,  prix_propose);
				statm.setInt(3,  r.getInt("ID_uti"));
				statm.executeUpdate();

				// Mettre à jour le prix du produit
				PreparedStatement prd= connection.prepareStatement("UPDATE PRODUIT SET prix_produit=? WHERE ID_prod=?");
				prd.setInt(1, prix_propose);
				prd.setInt(2,ID_prod);
				prd.executeUpdate();

				System.out.println("Votre enchère a bien été prise en compte !");

				// Compter le nombre d'offres pour le produit
				PreparedStatement count=connection.prepareStatement("SELECT COUNT(*) FROM OFFRE WHERE ID_prod = ?");
				count.setInt(1, res.getInt("ID_prod"));
				ResultSet nb=count.executeQuery();
				nb.next();

				// Vérifier si c'est la 5ème offre, si oui ajouter le produit dans la table remporte
				if (nb.getInt(1)>=5) {
					System.out.println("Bravo vous avez remporté le produit "+ res.getInt("ID_prod"));
					PreparedStatement ajout = connection.prepareStatement("INSERT INTO REMPORTE VALUES(? , ((SELECT date_heure FROM OFFRE WHERE ID_prod = ?)\n"
							+ "                                MINUS\n"
							+ "                                (SELECT DISTINCT O1.date_heure FROM OFFRE O1 JOIN OFFRE O2\n"
							+ "                                    ON O1.ID_prod = O2.ID_prod AND O1.ID_prod = ?\n"
							+ "                                WHERE O1.date_heure < O2.date_heure)))");
					ajout.setInt(1, res.getInt("ID_prod"));
					ajout.setInt(2, res.getInt("ID_prod"));
					ajout.setInt(3, res.getInt("ID_prod"));
					ajout.executeUpdate();
				}
			}else {
				System.out.println("produit n'est pas à vendre / prix trop bas");
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void espaceUser(String mail) {
		while (true) {
			System.out.println("------------------------------------------------");
			System.out.println("Session de " + mail + ", que souhaitez-vous faire ?");
			System.out.println("1 - Proposer une enchère sur un produit");
			System.out.println("2 - Afficher les catégories & sous-catégories");
			System.out.println("3 - Afficher les catégories & sous-catégories recommandés");
			System.out.println("4 - Afficher les produits d'une catégorie");
			System.out.println("5 - Voir la fiche d'un produit");
			System.out.println("6 - Afficher les utilisateurs");
			System.out.println("7 - Supprimer votre compte (irréversible)");
			System.out.println("0 - Déconnexion");

			Scanner scan = new Scanner(System.in);
			String optionUser = scan.next();
			scan.nextLine();
			switch (Integer.valueOf(optionUser)) {
				case 0:
					System.out.println("Déconnexion validée");
					scan.close();
					return;
				case 1:
					System.out.println("Saisir le numéro du produit :");
					String id = scan.next();
					scan.nextLine();
					System.out.println("Saisir le prix proposé :");
					String prix = scan.next();
					enchere(Integer.valueOf(id), Integer.valueOf(prix), mail);
					break;
				case 2:
					afficheProduitCategorie();
					break;
				case 3:
					afficheRecommandationsPerso(mail);
					recommandationsGenerales(mail);
					break;
				case 4:
					System.out.println("Saisir la catégorie recherchée :");
					String categorie = scan.next();
					scan.nextLine();
					afficheProduits(categorie);
					break;
				case 5:
					System.out.println("Saisir le numéro du produit :");
					String idProd = scan.next();
					scan.nextLine();
					ficheProduit(Integer.valueOf(idProd));
					break;
				case 6:
					afficherUser();
					break;
				case 7:
					System.out.println("Toutes vos données personnelles vont être supprimées et vous ne pourrez plus vous connecter à ce compte");
					boolean reponseNonValide = true;
					while(reponseNonValide)
					{
						System.out.println("Etes-vous certain(e) que vous voulez supprimer votre compte?");
						System.out.println("1 - Confirmation");
						System.out.println("2 - Annulation");
						String optionUserSuppression = scan.next();
						scan.nextLine();
						switch(Integer.valueOf(optionUserSuppression))
						{
							case 1:
								supprimer(mail);
								return;
							case 2:
								reponseNonValide = false;
								break;
							default:
								// Ne rien faire
								break;
						}
					}
					break;
				default:
					// Ne rien faire
					break;
			}
			scan.close();
		}
	}

	public void afficheProduitCategorie() {
		try {
			Statement statmt = connection.createStatement();
			ResultSet res = statmt.executeQuery("SELECT * FROM CATEGORIE WHERE nom_cat NOT IN (SELECT nom_cat FROM EST_LA_SOUS_CATEGORIE_DE)");
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
											"SELECT nom_cat, COUNT(*) AS nb_offres FROM OFFRE "
											+ "JOIN PRODUIT ON PRODUIT.ID_prod = OFFRE.ID_prod "
											+ "JOIN UTILISATEUR ON UTILISATEUR.ID_uti = OFFRE.ID_uti "
											+ "WHERE UTILISATEUR.mail = ? AND NOT EXISTS (SELECT * FROM REMPORTE JOIN OFFRE OFR "
											+ "ON REMPORTE.ID_prod = OFR.ID_prod AND REMPORTE.date_heure = OFR.date_heure "
											+ "WHERE OFR.ID_uti = OFFRE.ID_uti AND OFFRE.ID_prod = REMPORTE.ID_prod) "
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
			stmt.setString(1, mail);
			ResultSet res = stmt.executeQuery();
			while (res.next()) {
				System.out.println("-> " + res.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}






	public void afficheProduits(String categorie) {
		try {
			PreparedStatement statmt = connection.prepareStatement(
					"SELECT OFFRE.ID_PROD, count(*) AS nb_offres, PRODUIT.intitule FROM OFFRE"
					+ "      JOIN PRODUIT ON PRODUIT.ID_prod = OFFRE.ID_prod"
					+ "      WHERE PRODUIT.nom_cat LIKE ?"
					+ "            AND NOT EXISTS (SELECT * FROM REMPORTE"
					+ "                            JOIN OFFRE OFR ON REMPORTE.ID_prod = OFR.ID_prod AND"
					+ "                                             REMPORTE.date_heure = OFR.date_heure"
					+ "                            WHERE OFFRE.ID_prod = REMPORTE.ID_prod) "
					+ "GROUP BY OFFRE.ID_prod, PRODUIT.intitule "
					+ "ORDER BY nb_offres DESC, PRODUIT.intitule");
			statmt.setString(1, categorie);
			ResultSet res = statmt.executeQuery();
			while (res.next()) {
				PreparedStatement statmt2 = connection.prepareStatement(
						"SELECT * FROM PRODUIT WHERE ID_prod=?");
				statmt2.setInt(1, res.getInt("ID_prod"));
				ResultSet res2 = statmt2.executeQuery();
				res2.next();
				System.out.println(
						" -> ID " + res2.getInt("ID_prod") + " : " + res2.getString("intitule") + " " + res2.getInt("prix_produit")
						);
			}

			PreparedStatement statmt3 = connection.prepareStatement(
					"SELECT * FROM PRODUIT"
					+ "      WHERE nom_cat LIKE ?"
					+ "            AND NOT EXISTS (SELECT ID_prod FROM OFFRE WHERE PRODUIT.ID_prod = OFFRE.ID_prod) "
					+ "ORDER BY PRODUIT.intitule");
			statmt3.setString(1, categorie);
			ResultSet res3 = statmt3.executeQuery();
			while (res3.next()) {
				System.out.println(
						" -> ID " + res3.getInt("ID_prod") + " : " + res3.getString("intitule") + " " + res3.getInt("prix_produit")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
