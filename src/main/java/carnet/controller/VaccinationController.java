package carnet.controller;

import carnet.dao.EnfantDAO;
import carnet.dao.NotificationDAO;
import carnet.dao.VaccinationDAO;
import carnet.model.Enfant;
import carnet.model.Notification;
import carnet.model.Vaccination;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller pour la gestion des vaccinations.
 * Lié à VaccinationView.fxml
 *
 * Logique intégrée :
 *   - Calcul automatique du rappel (dose == 1 → rappel dans 6 mois)
 *   - Vérification dose >= 1
 *   - Création d'une notification de rappel après enregistrement
 */
public class VaccinationController {

    // ── Composants FXML ───────────────────────────────────────────────────────
    @FXML private ChoiceBox<Enfant>                  champEnfant;
    @FXML private TextField                          champNomVaccin;    // → NomVaccin
    @FXML private DatePicker                         champDateVaccin;   // → dateVaccin
    @FXML private Spinner<Integer>                   champDose;         // → dose (min 1)
    @FXML private DatePicker                         champRappel;       // → rappel (facultatif)
    @FXML private TableView<Vaccination>             tableVaccinations;
    @FXML private TableColumn<Vaccination, String>   colVaccin;
    @FXML private TableColumn<Vaccination, String>   colDate;
    @FXML private TableColumn<Vaccination, Integer>  colDose;
    @FXML private TableColumn<Vaccination, String>   colRappel;

    // ── Dépendances ───────────────────────────────────────────────────────────
    private final VaccinationDAO  vaccinationDAO  = new VaccinationDAO();
    private final EnfantDAO       enfantDAO       = new EnfantDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    private int idParentConnecte = 1; // ← Remplacer

    // ── Initialisation ────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        // Spinner dose : minimum = 1, maximum = 10, valeur par défaut = 1
        champDose.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        chargerEnfants();
        champEnfant.setOnAction(e -> chargerVaccinations());
    }

    // ── Bouton : Ajouter ──────────────────────────────────────────────────────
    @FXML
    public void onAjouter() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) { afficherErreur("Sélectionnez un enfant."); return; }
        if (champNomVaccin.getText().isBlank()) { afficherErreur("Le nom du vaccin est obligatoire."); return; }
        if (champDateVaccin.getValue() == null) { afficherErreur("La date est obligatoire."); return; }

        int dose = champDose.getValue();
        if (dose < 1) { afficherErreur("La dose doit être >= 1."); return; } // Vérification dose

        Vaccination v = new Vaccination();
        v.setIdCarnetDeSante(enfant.getIdCarnetDeSante());
        v.setNomVaccin(champNomVaccin.getText().trim());
        v.setDateVaccin(champDateVaccin.getValue());
        v.setDose(dose);
        v.setRappel(champRappel.getValue()); // Peut être null → calculé dans le DAO
        v.setIdMedecin(0); // Médecin non géré côté Java

        try {
            vaccinationDAO.ajouter(v);

            // Créer une notification de rappel si dose == 1
            if (dose == 1) {
                LocalDate dateRappel = champDateVaccin.getValue().plusMonths(6);
                Notification notif = new Notification();
                notif.setIdEnfant(enfant.getIdEnfant());
                notif.setMessage("Rappel vaccin \"" + v.getNomVaccin()
                                 + "\" prévu le " + dateRappel);
                notificationDAO.ajouter(notif);
            }

            chargerVaccinations();
            viderFormulaire();
            afficherSucces("Vaccination enregistrée.");
        } catch (SQLException e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    // ── Bouton : Supprimer ────────────────────────────────────────────────────
    @FXML
    public void onSupprimer() {
        Vaccination sel = tableVaccinations.getSelectionModel().getSelectedItem();
        if (sel == null) { afficherErreur("Sélectionnez une vaccination."); return; }

        try {
            vaccinationDAO.supprimer(sel.getIdVaccination());
            chargerVaccinations();
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

    private void chargerVaccinations() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) return;
        try {
            List<Vaccination> liste = vaccinationDAO.trouverParCarnet(enfant.getIdCarnetDeSante());
            tableVaccinations.setItems(FXCollections.observableArrayList(liste));
        } catch (SQLException e) {
            afficherErreur("Impossible de charger les vaccinations.");
        }
    }

    private void viderFormulaire() {
        champNomVaccin.clear();
        champDateVaccin.setValue(null);
        champDose.getValueFactory().setValue(1);
        champRappel.setValue(null);
    }

    private void afficherSucces(String msg) {
        new Alert(AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private void afficherErreur(String msg) {
        new Alert(AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
