package carnet.dao;

import carnet.config.DatabaseConfig;
import carnet.model.Examen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD pour la table Examen.
 * Colonnes SQL : id_examen, dateExamen, details, id_carnetDeSante, id_medecin
 */
public class ExamenDAO {

    // ────── CREATE ──────
    public void ajouter(Examen e) throws SQLException {
        String sql = """
            INSERT INTO Examen (dateExamen, details, id_carnetDeSante, id_medecin)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(e.getDateExamen()));
            stmt.setString(2, e.getDetails());
            stmt.setInt(3, e.getIdCarnetDeSante());
            stmt.setInt(4, e.getIdMedecin());

            stmt.executeUpdate();
            System.out.println("[ExamenDAO] ✓ Ajouté : " + e);
        }
    }

    // ────── READ ──────
    public List<Examen> trouverParCarnet(int idCarnetDeSante) throws SQLException {
        String sql = "SELECT * FROM Examen WHERE id_carnetDeSante = ? ORDER BY dateExamen DESC";
        List<Examen> liste = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarnetDeSante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) liste.add(construire(rs));
        }
        return liste;
    }

    // ────── DELETE ──────
    public void supprimer(int idExamen) throws SQLException {
        String sql = "DELETE FROM Examen WHERE id_examen = ?";

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idExamen);
            stmt.executeUpdate();
            System.out.println("[ExamenDAO] ✓ Supprimé id=" + idExamen);
        }
    }

    // ────── PRIVÉE ──────
    private Examen construire(ResultSet rs) throws SQLException {
        Examen e = new Examen();
        e.setIdExamen(rs.getInt("id_examen"));
        e.setDateExamen(rs.getDate("dateExamen").toLocalDate());
        e.setDetails(rs.getString("details"));
        e.setIdCarnetDeSante(rs.getInt("id_carnetDeSante"));
        e.setIdMedecin(rs.getInt("id_medecin"));
        return e;
    }
}
