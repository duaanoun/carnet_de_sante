package carnet.dao;

import carnet.config.DatabaseConfig;
import carnet.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD pour la table Notification.
 * Colonnes SQL : id_notification, message, dateEnvoiNot, statutNot, id_enfant
 *
 * ENUM statutNot : 'non_lu' | 'lu'
 */
public class NotificationDAO {

    // ────── CREATE ──────
    public void ajouter(Notification n) throws SQLException {
        String sql = "INSERT INTO Notification (message, statutNot, id_enfant) VALUES (?, 'non_lu', ?)";

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, n.getMessage());
            stmt.setInt(2, n.getIdEnfant());
            stmt.executeUpdate();
            System.out.println("[NotificationDAO] ✓ Notification créée");
        }
    }

    // ────── READ ──────
    public List<Notification> trouverNonLues(int idEnfant) throws SQLException {
        String sql = "SELECT * FROM Notification WHERE id_enfant = ? AND statutNot = 'non_lu' ORDER BY dateEnvoiNot DESC";
        List<Notification> liste = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEnfant);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) liste.add(construire(rs));
        }
        return liste;
    }

    // ────── UPDATE ──────
    public void marquerLue(int idNotification) throws SQLException {
        String sql = "UPDATE Notification SET statutNot = 'lu' WHERE id_notification = ?";

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idNotification);
            stmt.executeUpdate();
            System.out.println("[NotificationDAO] ✓ Notification marquée comme lue");
        }
    }

    // ────── PRIVÉE ──────
    private Notification construire(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setIdNotification(rs.getInt("id_notification"));
        n.setMessage(rs.getString("message"));
        n.setDateEnvoiNot(rs.getTimestamp("dateEnvoiNot").toLocalDateTime());
        n.setStatutNot(rs.getString("statutNot"));
        n.setIdEnfant(rs.getInt("id_enfant"));
        return n;
    }
}
