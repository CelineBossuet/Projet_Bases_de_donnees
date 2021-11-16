import java.sql.*;
import java.util.Scanner;

public class ManagerDatabase {
	
	public void setup() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void connect() {
		try {
			String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
			String user = "ganderf";
			String password = "ganderf@oracle1";
			
			Connection connection = DriverManager.getConnection(url, user, password);
			
			connection.setAutoCommit(false);
			
			// Requêtes
			requete(connection);
			
			// Mettre des checkpoint si besoin
			Savepoint save1 = connection.setSavepoint("Nom du Checkpoint 1");
			
			// Choisir si validation ou non
			connection.rollback(); 
			connection.rollback(save1);
			connection.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void close(Connection connection) {
		try {
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void requete(Connection connection) {
		try {
			Statement statmt = connection.createStatement();
			statmt.executeQuery("SELECT * FROM emp");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void requeteParam(Connection connection) {
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
	
}
