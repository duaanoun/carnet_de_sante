package carnet.dao;

import carnet.config.DatabaseConfig;
import carnet.model.Enfant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD complet pour la table Enfant.
 * Colonnes SQL : id_enfant, NomEn, PrenomEN, dateNaissance, sexe, groupeSanguin,
 *                id_carnetDeSante, id_parent
 */
public class EnfantDAO {

    // ────── CREATE ──────
    public void ajouter(Enfant e) throws SQLException {
        String sql = """
            INSERT INTO Enfant (NomEn, PrenomEN, dateNaissance, sexe, groupeSanguin, id_parent)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, e.getNom());
            stmt.setString(2, e.getPrenom());
            stmt.setDate(3, Date.valueOf(e.getDateNaissance()));
            stmt.setString(4, e.getSexe());
            stmt.setString(5, e.getGroupeSanguin());
            stmt.setInt(6, e.getIdParent());

            stmt.executeUpdate();

            ResultSet cles = stmt.getGeneratedKeys();
            if (cles.next()) {
                e.setIdEnfant(cles.getInt(1));
            }
            System.out.println("[EnfantDAO] ✓ Ajouté : " + e);
        }
    }

    // ────── READ ──────
    public Enfant trouverParId(int idEnfant) throws SQLException {
        String sql = "SELECT * FROM Enfant WHERE id_enfant = ?";

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEnfant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return construire(rs);
        }
        return null;
    }

    public List<Enfant> trouverParParent(int idParent) throws SQLException {
        String sql = "SELECT * FROM Enfant WHERE id_parent = ? ORDER BY PrenomEN";
        List<Enfant> liste = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idParent);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) liste.add(construire(rs));
        }
        return liste;
    }

    // ────── UPDATE ──────
    public void modifier(Enfant e) throws SQLException {
        String sql = """
            UPDATE Enfant
            SET NomEn = ?, PrenomEN = ?, dateNaissance = ?, sexe = ?, groupeSanguin = ?
            WHERE id_enfant = ?
            """;

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getNom());
            stmt.setString(2, e.getPrenom());
            stmt.setDate(3, Date.valueOf(e.getDateNaissance()));
            stmt.setString(4, e.getSexe());
            stmt.setString(5, e.getGroupeSanguin());
            stmt.setInt(6, e.getIdEnfant());

            stmt.executeUpdate();
            System.out.println("[EnfantDAO] ✓ Modifié : " + e);
        }
    }

    // ────── DELETE ──────
    public void supprimer(int idEnfant) throws SQLException {
        String sql = "DELETE FROM Enfant WHERE id_enfant = ?";

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEnfant);
            stmt.executeUpdate();
            System.out.println("[EnfantDAO] ✓ Supprimé id=" + idEnfant);
        }
    }

    // ────── PRIVÉE ──────
    private Enfant construire(ResultSet rs) throws SQLException {
        Enfant e = new Enfant();
        e.setIdEnfant(rs.getInt("id_enfant"));
        e.setNom(rs.getString("NomEn"));
        e.setPrenom(rs.getString("PrenomEN"));
        e.setDateNaissance(rs.getDate("dateNaissance").toLocalDate());
        e.setSexe(rs.getString("sexe"));
        e.setGroupeSanguin(rs.getString("groupeSanguin"));
        e.setIdCarnetDeSante(rs.getInt("id_carnetDeSante"));
        e.setIdParent(rs.getInt("id_parent"));
        return e;
    }
}
