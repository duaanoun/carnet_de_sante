package carnet.controller;

import carnet.dao.ConsultationDAO;
import carnet.dao.EnfantDAO;
import carnet.model.Consultation;
import carnet.model.Enfant;
import carnet.session.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.util.List;

/**
 * Gère les consultations médicales (ajout, modification statut, suppression).
 * Lié à ConsultationView.fxml
 *
 * ENUM statutC : planifiee | realisee | annulee
 */
public class ConsultationController {

    @FXML private ChoiceBox<Enfant>              champEnfant;
    @FXML private DatePicker                     champDate;
    @FXML private TextField                      champMotif;
    @FXML private ChoiceBox<String>              champStatut;
    @FXML private TableView<Consultation>        tableConsultations;
    @FXML private TableColumn<Consultation, String> colDate;
    @FXML private TableColumn<Consultation, String> colMotif;
    @FXML private TableColumn<Consultation, String> colStatut;

    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final EnfantDAO       enfantDAO       = new EnfantDAO();

    // ────── INIT ──────
    @FXML
    public void initialize() {
        champStatut.setItems(FXCollections.observableArrayList("planifiee", "realisee", "annulee"));
        champStatut.setValue("planifiee");
        chargerEnfants();
        champEnfant.setOnAction(e -> chargerConsultations());
    }

    // ────── BOUTONS ──────
    @FXML
    public void onAjouter() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) { alerte("Sélectionnez un enfant", AlertType.WARNING); return; }
        if (champDate.getValue() == null) { alerte("Date obligatoire", AlertType.WARNING); return; }
        if (champMotif.getText().isBlank()) { alerte("Motif obligatoire", AlertType.WARNING); return; }

        Consultation c = new Consultation();
        c.setIdCarnetDeSante(enfant.getIdCarnetDeSante());
        c.setDateCons(champDate.getValue().atStartOfDay());
        c.setMotifC(champMotif.getText().trim());
        c.setStatutC(champStatut.getValue());
        c.setIdMedecin(0);

        try {
            consultationDAO.ajouter(c);
            chargerConsultations();
            vider();
            alerte("✓ Consultation planifiée", AlertType.INFORMATION);
        } catch (SQLException e) {
            alerte("✗ Erreur : " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void onModifierStatut() {
        Consultation sel = tableConsultations.getSelectionModel().getSelectedItem();
        if (sel == null) { alerte("Sélectionnez une consultation", AlertType.WARNING); return; }
        String nouveau = champStatut.getValue();
        if (nouveau == null) { alerte("Sélectionnez un statut", AlertType.WARNING); return; }

        try {
            consultationDAO.modifierStatut(sel.getIdConsultation(), nouveau);
            chargerConsultations();
            alerte("✓ Statut mis à jour : " + nouveau, AlertType.INFORMATION);
        } catch (SQLException e) {
            alerte("✗ Erreur : " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void onSupprimer() {
        Consultation sel = tableConsultations.getSelectionModel().getSelectedItem();
        if (sel == null) { alerte("Sélectionnez une consultation", AlertType.WARNING); return; }

        try {
            consultationDAO.supprimer(sel.getIdConsultation());
            chargerConsultations();
            alerte("✓ Consultation supprimée", AlertType.INFORMATION);
        } catch (SQLException e) {
            alerte("✗ Erreur : " + e.getMessage(), AlertType.ERROR);
        }
    }

    // ────── UTILITAIRES ──────
    private void chargerEnfants() {
        try {
            int idParent = SessionManager.getInstance().getIdParent();
            List<Enfant> enfants = enfantDAO.trouverParParent(idParent);
            champEnfant.setItems(FXCollections.observableArrayList(enfants));
        } catch (SQLException e) {
            alerte("✗ Impossible de charger : " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void chargerConsultations() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) return;
        try {
            List<Consultation> liste = consultationDAO.trouverParCarnet(enfant.getIdCarnetDeSante());
            tableConsultations.setItems(FXCollections.observableArrayList(liste));
        } catch (SQLException e) {
            alerte("✗ Impossible de charger : " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void vider() {
        champDate.setValue(null);
        champMotif.clear();
        champStatut.setValue("planifiee");
    }

    private void alerte(String msg, AlertType type) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
}
