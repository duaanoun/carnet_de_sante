package carnet.controller;

import carnet.dao.*;
import carnet.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller pour l'historique médical complet d'un enfant.
 * Lié à HistoriqueView.fxml
 *
 * Charge toutes les données via CarnetDeSante (agrégateur) :
 *   - Vaccinations
 *   - Consultations
 *   - Examens
 *   - Mesures de croissance
 *   - Notifications non lues
 */
public class HistoriqueController {

    // ── Composants FXML ───────────────────────────────────────────────────────
    @FXML private ChoiceBox<Enfant>   champEnfant;
    @FXML private Label               labelInfoEnfant;
    @FXML private ListView<String>    listeVaccinations;
    @FXML private ListView<String>    listeConsultations;
    @FXML private ListView<String>    listeExamens;
    @FXML private ListView<String>    listeCroissance;
    @FXML private ListView<String>    listeNotifications;

    // ── Dépendances ───────────────────────────────────────────────────────────
    private final EnfantDAO       enfantDAO       = new EnfantDAO();
    private final VaccinationDAO  vaccinationDAO  = new VaccinationDAO();
    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final CroissanceDAO   croissanceDAO   = new CroissanceDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    private int idParentConnecte = 1; // ← Remplacer

    // ── Initialisation ────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        chargerEnfants();
        champEnfant.setOnAction(e -> afficherHistorique());
    }

    @FXML
    public void onActualiser() {
        afficherHistorique();
    }

    // ── Chargement de l'historique complet ───────────────────────────────────

    private void afficherHistorique() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) return;

        int idCarnet = enfant.getIdCarnetDeSante();

        try {
            // Construire le carnet complet
            CarnetDeSante carnet = new CarnetDeSante(idCarnet, enfant);
            carnet.setVaccinations(vaccinationDAO.trouverParCarnet(idCarnet));
            carnet.setConsultations(consultationDAO.trouverParCarnet(idCarnet));
            carnet.setMesuresCroissance(croissanceDAO.trouverParCarnet(idCarnet));

            // Infos de l'enfant
            labelInfoEnfant.setText(
                enfant.getPrenom() + " " + enfant.getNom()
                + "  |  Né(e) le " + enfant.getDateNaissance()
                + "  |  Sexe : " + enfant.getSexe()
                + "  |  Groupe : " + (enfant.getGroupeSanguin() != null ? enfant.getGroupeSanguin() : "—")
            );

            // Vaccinations
            listeVaccinations.setItems(FXCollections.observableArrayList(
                carnet.getVaccinations().stream()
                      .map(v -> "💉  " + v.getNomVaccin()
                               + "  —  dose " + v.getDose()
                               + "  —  " + v.getDateVaccin()
                               + (v.getRappel() != null ? "  (rappel : " + v.getRappel() + ")" : ""))
                      .toList()
            ));

            // Consultations
            listeConsultations.setItems(FXCollections.observableArrayList(
                carnet.getConsultations().stream()
                      .map(c -> "🩺  " + c.getDateCons().toLocalDate()
                               + "  —  " + c.getMotifC()
                               + "  [" + c.getStatutC() + "]")
                      .toList()
            ));

            // Croissance
            listeCroissance.setItems(FXCollections.observableArrayList(
                carnet.getMesuresCroissance().stream()
                      .map(m -> "📏  " + m.getDateC()
                               + "  —  " + m.getTailleC() + " cm  /  " + m.getPoidsC() + " kg")
                      .toList()
            ));

            // Notifications non lues
            List<Notification> notifs = notificationDAO.trouverNonLues(enfant.getIdEnfant());
            listeNotifications.setItems(FXCollections.observableArrayList(
                notifs.stream()
                      .map(n -> "🔔  " + n.getMessage())
                      .toList()
            ));

        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Erreur chargement :\n" + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    // ── Méthodes privées ──────────────────────────────────────────────────────

    private void chargerEnfants() {
        try {
            List<Enfant> enfants = enfantDAO.trouverParParent(idParentConnecte);
            champEnfant.setItems(FXCollections.observableArrayList(enfants));
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Impossible de charger les enfants.", ButtonType.OK).showAndWait();
        }
    }
}
