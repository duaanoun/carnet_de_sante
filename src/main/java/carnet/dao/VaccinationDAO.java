package carnet.dao;

import carnet.model.Vaccination;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD pour la table Vaccination.
 *
 * Noms des colonnes SQL : id_Vaccination, NomVaccin, dateVaccin,
 *                         dose, rappel, id_carnetDeSante, id_medecin
 *
 * Logique métier simple : calculerRappel() → date + 6 mois (si dose == 1)
 */
public class VaccinationDAO {

    // ── CREATE ───────────────────────────────────────────────────────────────

    public void ajouter(Vaccination v) throws SQLException {
        String sql = """
            INSERT INTO Vaccination (NomVaccin, dateVaccin, dose, rappel, id_carnetDeSante, id_medecin)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, v.getNomVaccin());
            stmt.setDate(2, Date.valueOf(v.getDateVaccin()));
            stmt.setInt(3, v.getDose());

            // Calcul automatique du rappel si dose == 1 et rappel non défini
            LocalDate rappel = v.getRappel();
            if (rappel == null && v.getDose() == 1) {
                rappel = v.getDateVaccin().plusMonths(6); // Rappel à 6 mois
            }
            stmt.setDate(4, rappel != null ? Date.valueOf(rappel) : null);

            stmt.setInt(5, v.getIdCarnetDeSante());
            stmt.setInt(6, v.getIdMedecin());

            stmt.executeUpdate();
            System.out.println("[VaccinationDAO] Ajouté : " + v);
        }
    }

    // ── READ — toutes les vaccinations d'un carnet ────────────────────────────

    public List<Vaccination> trouverParCarnet(int idCarnetDeSante) throws SQLException {
        String sql = "SELECT * FROM Vaccination WHERE id_carnetDeSante = ? ORDER BY dateVaccin DESC";
        List<Vaccination> liste = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarnetDeSante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) liste.add(construire(rs));
        }
        return liste;
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public void supprimer(int idVaccination) throws SQLException {
        String sql = "DELETE FROM Vaccination WHERE id_Vaccination = ?";

        try (Connection conn = DatabaseConnection.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVaccination);
            stmt.executeUpdate();
        }
    }

    // ── Méthode privée ────────────────────────────────────────────────────────

    private Vaccination construire(ResultSet rs) throws SQLException {
        Vaccination v = new Vaccination();
        v.setIdVaccination(rs.getInt("id_Vaccination"));
        v.setNomVaccin(rs.getString("NomVaccin"));
        v.setDateVaccin(rs.getDate("dateVaccin").toLocalDate());
        v.setDose(rs.getInt("dose"));

        // rappel peut être NULL en base
        Date rappelSQL = rs.getDate("rappel");
        v.setRappel(rappelSQL != null ? rappelSQL.toLocalDate() : null);

        v.setIdCarnetDeSante(rs.getInt("id_carnetDeSante"));
        v.setIdMedecin(rs.getInt("id_medecin"));
        return v;
    }
}
