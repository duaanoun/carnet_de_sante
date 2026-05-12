package carnet.dao;

import carnet.config.DatabaseConfig;
import carnet.model.Consultation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD pour la table Consultation.
 * Colonnes SQL : id_consultation, date_Cons, motifC, statutC,
 *                id_carnetDeSante, id_medecin
 *
 * ENUM statutC : 'planifiee' | 'realisee' | 'annulee'
 */
public class ConsultationDAO {

    // ────── CREATE ──────
    public void ajouter(Consultation c) throws SQLException {
        String sql = """
            INSERT INTO Consultation (date_Cons, motifC, statutC, id_carnetDeSante, id_medecin)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(c.getDateCons()));
            stmt.setString(2, c.getMotifC());
            stmt.setString(3, c.getStatutC() != null ? c.getStatutC() : "planifiee");
            stmt.setInt(4, c.getIdCarnetDeSante());
            stmt.setInt(5, c.getIdMedecin());

            stmt.executeUpdate();
            System.out.println("[ConsultationDAO] ✓ Ajouté : " + c);
        }
    }

    // ────── READ ──────
    public List<Consultation> trouverParCarnet(int idCarnetDeSante) throws SQLException {
        String sql = "SELECT * FROM Consultation WHERE id_carnetDeSante = ? ORDER BY date_Cons DESC";
        List<Consultation> liste = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarnetDeSante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) liste.add(construire(rs));
        }
        return liste;
    }

    // ────── UPDATE ──────
    public void modifierStatut(int idConsultation, String nouveauStatut) throws SQLException {
        String sql = "UPDATE Consultation SET statutC = ? WHERE id_consultation = ?";

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nouveauStatut);
            stmt.setInt(2, idConsultation);
            stmt.executeUpdate();
            System.out.println("[ConsultationDAO] ✓ Statut mis à jour : " + nouveauStatut);
        }
    }

    // ────── DELETE ──────
    public void supprimer(int idConsultation) throws SQLException {
        String sql = "DELETE FROM Consultation WHERE id_consultation = ?";

        try (Connection conn = DatabaseConfig.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idConsultation);
            stmt.executeUpdate();
            System.out.println("[ConsultationDAO] ✓ Supprimé id=" + idConsultation);
        }
    }

    // ────── PRIVÉE ──────
    private Consultation construire(ResultSet rs) throws SQLException {
        Consultation c = new Consultation();
        c.setIdConsultation(rs.getInt("id_consultation"));
        c.setDateCons(rs.getTimestamp("date_Cons").toLocalDateTime());
        c.setMotifC(rs.getString("motifC"));
        c.setStatutC(rs.getString("statutC"));
        c.setIdCarnetDeSante(rs.getInt("id_carnetDeSante"));
        c.setIdMedecin(rs.getInt("id_medecin"));
        return c;
    }
}
