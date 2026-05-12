package carnet.dao;

import carnet.model.Croissance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD pour la table Croissance.
 *
 * Colonnes SQL : id_Croissance, dateC, tailleC, PoidsC, id_carnetDeSante
 *
 * CONTRAINTE IMPORTANTE :
 * La table a une contrainte UNIQUE KEY (id_carnetDeSante, dateC).
 * → On ne peut pas avoir deux mesures le même jour pour le même enfant.
 * → La méthode ajouterOuModifier() fait un INSERT ... ON DUPLICATE KEY UPDATE
 *   pour gérer automatiquement ce cas.
 */
public class CroissanceDAO {

    // ── INSERT ou UPDATE si mesure déjà existante ce jour ────────────────────

    /**
     * Si une mesure existe déjà pour ce carnet à cette date → UPDATE.
     * Sinon → INSERT.
     * Ceci respecte la contrainte UNIQUE KEY (id_carnetDeSante, dateC).
     */
    public void ajouterOuModifier(Croissance c) throws SQLException {
        String sql = """
            INSERT INTO Croissance (dateC, tailleC, PoidsC, id_carnetDeSante)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE tailleC = VALUES(tailleC), PoidsC = VALUES(PoidsC)
            """;

        try (Connection conn = DatabaseConnection.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(c.getDateC()));
            stmt.setDouble(2, c.getTailleC());
            stmt.setDouble(3, c.getPoidsC());
            stmt.setInt(4, c.getIdCarnetDeSante());

            stmt.executeUpdate();
            System.out.println("[CroissanceDAO] Mesure enregistrée : " + c);
        }
    }

    // ── READ — toutes les mesures d'un carnet (ordonnées par date) ────────────

    public List<Croissance> trouverParCarnet(int idCarnetDeSante) throws SQLException {
        String sql = "SELECT * FROM Croissance WHERE id_carnetDeSante = ? ORDER BY dateC";
        List<Croissance> liste = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarnetDeSante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) liste.add(construire(rs));
        }
        return liste;
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public void supprimer(int idCroissance) throws SQLException {
        String sql = "DELETE FROM Croissance WHERE id_Croissance = ?";

        try (Connection conn = DatabaseConnection.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCroissance);
            stmt.executeUpdate();
        }
    }

    // ── Méthode privée ────────────────────────────────────────────────────────

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
