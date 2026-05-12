package carnet.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fournit la connexion MySQL unique à toute l'application.
 * Pattern Singleton : une seule instance de Connection partagée.
 *
 * Tous les DAO appellent DatabaseConnection.getConnexion()
 * pour obtenir la connexion — ils ne créent jamais leur propre connexion.
 */
public class DatabaseConnection {

    // ── À adapter selon votre configuration MySQL ───────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/carnet_sante";
    private static final String USER     = "root";
    private static final String PASSWORD = "votre_mot_de_passe";
    // ────────────────────────────────────────────────────────────────────────

    private static Connection connexion = null;

    private DatabaseConnection() {} // Constructeur privé — instanciation interdite

    /**
     * Retourne la connexion active.
     * Si elle est null ou fermée, en crée une nouvelle.
     */
    public static Connection getConnexion() throws SQLException {
        if (connexion == null || connexion.isClosed()) {
            connexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DB] Connexion MySQL établie.");
        }
        return connexion;
    }

    /**
     * Ferme proprement la connexion à la fin de l'application.
     * Appeler depuis App.stop() (JavaFX lifecycle).
     */
    public static void fermer() {
        try {
            if (connexion != null && !connexion.isClosed()) {
                connexion.close();
                System.out.println("[DB] Connexion MySQL fermée.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erreur fermeture : " + e.getMessage());
        }
    }
}
