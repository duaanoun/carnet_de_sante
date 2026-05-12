package carnet.controller;

import carnet.dao.*;
import carnet.model.*;
import carnet.session.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.util.List;

/**
 * Affiche l'historique médical complet d'un enfant.
 * Lié à HistoriqueView.fxml
 *
 * Charge automatiquement :
 *   - Vaccinations
 *   - Consultations
 *   - Examens
 *   - Mesures de croissance
 *   - Notifications
 */
public class HistoriqueController {

    @FXML private ChoiceBox<Enfant>      champEnfant;
    @FXML private Label                  labelInfoEnfant;
    @FXML private ListView<String>       listeVaccinations;
    @FXML private ListView<String>       listeConsultations;
    @FXML private ListView<String>       listeExamens;
    @FXML private ListView<String>       listeCroissance;
    @FXML private ListView<String>       listeNotifications;

    private final EnfantDAO       enfantDAO       = new EnfantDAO();
    private final VaccinationDAO  vaccinationDAO  = new VaccinationDAO();
    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final ExamenDAO       examenDAO       = new ExamenDAO();
    private final CroissanceDAO   croissanceDAO   = new CroissanceDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    // ────── INIT ──────
    @FXML
    public void initialize() {
        chargerEnfants();
        champEnfant.setOnAction(e -> afficherHistorique());
    }

    @FXML
    public void onActualiser() {
        afficherHistorique();
    }

    // ────── CHARGEMENT COMPLET ──────
    private void afficherHistorique() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) return;

        int idCarnet = enfant.getIdCarnetDeSante();

        try {
            // Infos enfant
            labelInfoEnfant.setText(
                enfant.getPrenom() + " " + enfant.getNom()
                + "  |  Né le " + enfant.getDateNaissance()
                + "  |  " + enfant.getSexe()
                + "  |  Groupe : " + (enfant.getGroupeSanguin() != null ? enfant.getGroupeSanguin() : "?")
            );

            // Vaccinations
            List<Vaccination> vaccins = vaccinationDAO.trouverParCarnet(idCarnet);
            listeVaccinations.setItems(FXCollections.observableArrayList(
                vaccins.stream().map(v -> "💉 " + v.getNomVaccin() + " (dose " + v.getDose() + ") - " + v.getDateVaccin()).toList()
            ));

            // Consultations
            List<Consultation> consultations = consultationDAO.trouverParCarnet(idCarnet);
            listeConsultations.setItems(FXCollections.observableArrayList(
                consultations.stream().map(c -> "🩺 " + c.getDateCons().toLocalDate() + " - " + c.getMotifC() + " [" + c.getStatutC() + "]").toList()
            ));

            // Examens
            List<Examen> examens = examenDAO.trouverParCarnet(idCarnet);
            listeExamens.setItems(FXCollections.observableArrayList(
                examens.stream().map(e -> "📋 " + e.getDateExamen() + " - " + e.getDetails()).toList()
            ));

            // Croissance
            List<Croissance> croissances = croissanceDAO.trouverParCarnet(idCarnet);
            listeCroissance.setItems(FXCollections.observableArrayList(
                croissances.stream().map(m -> "📏 " + m.getDateC() + " - " + m.getTailleC() + " cm / " + m.getPoidsC() + " kg").toList()
            ));

            // Notifications
            List<Notification> notifs = notificationDAO.trouverNonLues(enfant.getIdEnfant());
            listeNotifications.setItems(FXCollections.observableArrayList(
                notifs.stream().map(n -> "🔔 " + n.getMessage()).toList()
            ));

        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Erreur chargement : " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    // ────── UTILITAIRES ──────
    private void chargerEnfants() {
        try {
            int idParent = SessionManager.getInstance().getIdParent();
            List<Enfant> enfants = enfantDAO.trouverParParent(idParent);
            champEnfant.setItems(FXCollections.observableArrayList(enfants));
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Impossible de charger : " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
