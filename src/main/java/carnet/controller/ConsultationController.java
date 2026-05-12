package carnet.controller;

import carnet.dao.ConsultationDAO;
import carnet.dao.EnfantDAO;
import carnet.model.Consultation;
import carnet.model.Enfant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller pour la gestion des consultations médicales.
 * Lié à ConsultationView.fxml
 *
 * Gère le statut ENUM : planifiee / realisee / annulee
 */
public class ConsultationController {

    // ── Composants FXML ───────────────────────────────────────────────────────
    @FXML private ChoiceBox<Enfant>                    champEnfant;
    @FXML private DatePicker                           champDate;       // → date_Cons
    @FXML private TextField                            champMotif;      // → motifC
    @FXML private ChoiceBox<String>                    champStatut;     // → statutC ENUM
    @FXML private TableView<Consultation>              tableConsultations;
    @FXML private TableColumn<Consultation, String>    colDate;
    @FXML private TableColumn<Consultation, String>    colMotif;
    @FXML private TableColumn<Consultation, String>    colStatut;

    // ── Dépendances ───────────────────────────────────────────────────────────
    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final EnfantDAO       enfantDAO       = new EnfantDAO();

    private int idParentConnecte = 1; // ← Remplacer

    // ── Initialisation ────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        // Valeurs de l'ENUM MySQL
        champStatut.setItems(FXCollections.observableArrayList("planifiee", "realisee", "annulee"));
        champStatut.setValue("planifiee"); // Valeur par défaut

        chargerEnfants();
        champEnfant.setOnAction(e -> chargerConsultations());
    }

    // ── Bouton : Ajouter ──────────────────────────────────────────────────────
    @FXML
    public void onAjouter() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) { afficherErreur("Sélectionnez un enfant."); return; }
        if (champDate.getValue() == null) { afficherErreur("La date est obligatoire."); return; }
        if (champMotif.getText().isBlank()) { afficherErreur("Le motif est obligatoire."); return; }

        Consultation c = new Consultation();
        c.setIdCarnetDeSante(enfant.getIdCarnetDeSante());
        // date_Cons est DATETIME → on utilise LocalDateTime avec heure à minuit par défaut
        c.setDateCons(champDate.getValue().atStartOfDay());
        c.setMotifC(champMotif.getText().trim());
        c.setStatutC(champStatut.getValue());
        c.setIdMedecin(0); // Médecin géré en C++

        try {
            consultationDAO.ajouter(c);
            chargerConsultations();
            viderFormulaire();
            afficherSucces("Consultation planifiée.");
        } catch (SQLException e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    // ── Bouton : Modifier statut ──────────────────────────────────────────────
    @FXML
    public void onModifierStatut() {
        Consultation sel = tableConsultations.getSelectionModel().getSelectedItem();
        if (sel == null) { afficherErreur("Sélectionnez une consultation."); return; }
        String nouveau = champStatut.getValue();
        if (nouveau == null) { afficherErreur("Sélectionnez un statut."); return; }

        try {
            consultationDAO.modifierStatut(sel.getIdConsultation(), nouveau);
            chargerConsultations();
            afficherSucces("Statut mis à jour : " + nouveau);
        } catch (SQLException e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    // ── Bouton : Supprimer ────────────────────────────────────────────────────
    @FXML
    public void onSupprimer() {
        Consultation sel = tableConsultations.getSelectionModel().getSelectedItem();
        if (sel == null) { afficherErreur("Sélectionnez une consultation."); return; }

        try {
            consultationDAO.supprimer(sel.getIdConsultation());
            chargerConsultations();
        } catch (SQLException e) {
            afficherErreur("Erreur suppression : " + e.getMessage());
        }
    }

    // ── Méthodes privées ──────────────────────────────────────────────────────

    private void chargerEnfants() {
        try {
            List<Enfant> enfants = enfantDAO.trouverParParent(idParentConnecte);
            champEnfant.setItems(FXCollections.observableArrayList(enfants));
        } catch (SQLException e) {
            afficherErreur("Impossible de charger les enfants.");
        }
    }

    private void chargerConsultations() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) return;
        try {
            List<Consultation> liste = consultationDAO.trouverParCarnet(enfant.getIdCarnetDeSante());
            tableConsultations.setItems(FXCollections.observableArrayList(liste));
        } catch (SQLException e) {
            afficherErreur("Impossible de charger les consultations.");
        }
    }

    private void viderFormulaire() {
        champDate.setValue(null);
        champMotif.clear();
        champStatut.setValue("planifiee");
    }

    private void afficherSucces(String msg) {
        new Alert(AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private void afficherErreur(String msg) {
        new Alert(AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
