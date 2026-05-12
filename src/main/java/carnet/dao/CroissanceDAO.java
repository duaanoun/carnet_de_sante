package carnet.dao;

import carnet.config.DatabaseConfig;
import carnet.model.Croissance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD pour la table Croissance.
 * Colonnes SQL : id_Croissance, dateC, tailleC, PoidsC, id_carnetDeSante
 *
 * Contrainte SQL : UNIQUE KEY (id_carnetDeSante, dateC)
 * → Une seule mesure par enfant par jour. INSERT ... ON DUPLICATE KEY UPDATE pour UPDATE automatique.
 */
public class CroissanceDAO {

    // ────── CREATE ou UPDATE si dupliqué ──────
    public void ajouterOuModifier(Croissance c) throws SQLException {
        String sql = """
            INSERT INTO Croissance (dateC, tailleC, PoidsC, id_carnetDeSante)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE tailleC = VALUES(tailleC), PoidsC = VALUES(PoidsC)
            """;

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(c.getDateC()));
            stmt.setDouble(2, c.getTailleC());
            stmt.setDouble(3, c.getPoidsC());
            stmt.setInt(4, c.getIdCarnetDeSante());

            stmt.executeUpdate();
            System.out.println("[CroissanceDAO] ✓ Mesure enregistrée : " + c);
        }
    }

    // ────── READ ──────
    public List<Croissance> trouverParCarnet(int idCarnetDeSante) throws SQLException {
        String sql = "SELECT * FROM Croissance WHERE id_carnetDeSante = ? ORDER BY dateC";
        List<Croissance> liste = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarnetDeSante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) liste.add(construire(rs));
        }
        return liste;
    }

    // ────── DELETE ──────
    public void supprimer(int idCroissance) throws SQLException {
        String sql = "DELETE FROM Croissance WHERE id_Croissance = ?";

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCroissance);
            stmt.executeUpdate();
            System.out.println("[CroissanceDAO] ✓ Supprimé id=" + idCroissance);
        }
    }

    // ────── PRIVÉE ──────
    private Croissance construire(ResultSet rs) throws SQLException {
        Croissance c = new Croissance();
        c.setIdCroissance(rs.getInt("id_Croissance"));
        c.setDateC(rs.getDate("dateC").toLocalDate());
        c.setTailleC(rs.getDouble("tailleC"));
        c.setPoidsC(rs.getDouble("PoidsC"));
        c.setIdCarnetDeSante(rs.getInt("id_carnetDeSante"));
        return c;
    }
}
