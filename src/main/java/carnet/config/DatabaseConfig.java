package carnet.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestion unique de la connexion MySQL.
 * Pattern Singleton THREAD-SAFE.
 *
 * À adapter dans src/main/resources/application.properties :
 * db.url=jdbc:mysql://localhost:3306/carnetdesante
 * db.user=root
 * db.password=
 */
public class DatabaseConfig {

    private static final String URL      = "jdbc:mysql://localhost:3306/carnetdesante";
    private static final String USER     = "root";
    private static final String PASSWORD = "";

    private static Connection connexion = null;

    // Constructeur privé — Singleton
    private DatabaseConfig() {}

    /**
     * Retourne la connexion active (thread-safe).
     * Si fermée → crée une nouvelle connexion.
     */
    public static synchronized Connection getConnexion() throws SQLException {
        if (connexion == null || connexion.isClosed()) {
            connexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DB] ✓ Connexion MySQL établie");
        }
        return connexion;
    }

    /**
     * Ferme proprement la connexion.
     * À appeler depuis App.stop() (JavaFX lifecycle).
     */
    public static void fermer() {
        try {
            if (connexion != null && !connexion.isClosed()) {
                connexion.close();
                System.out.println("[DB] ✓ Connexion MySQL fermée");
            }
        } catch (SQLException e) {
            System.err.println("[DB] ✗ Erreur fermeture : " + e.getMessage());
        }
    }
}
