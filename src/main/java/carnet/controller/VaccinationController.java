package carnet.controller;

import carnet.dao.EnfantDAO;
import carnet.dao.NotificationDAO;
import carnet.dao.VaccinationDAO;
import carnet.model.Enfant;
import carnet.model.Notification;
import carnet.model.Vaccination;
import carnet.session.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Gère les vaccinations (ajout, suppression).
 * Lié à VaccinationView.fxml
 *
 * Logique :
 *   - Dose minimum = 1
 *   - Rappel auto = dose1 + 6 mois
 *   - Notification pour rappel
 */
public class VaccinationController {

    @FXML private ChoiceBox<Enfant>              champEnfant;
    @FXML private TextField                      champNomVaccin;
    @FXML private DatePicker                     champDateVaccin;
    @FXML private Spinner<Integer>               champDose;
    @FXML private DatePicker                     champRappel;
    @FXML private TableView<Vaccination>         tableVaccinations;
    @FXML private TableColumn<Vaccination, String> colVaccin;
    @FXML private TableColumn<Vaccination, String> colDate;
    @FXML private TableColumn<Vaccination, Integer> colDose;

    private final VaccinationDAO  vaccinationDAO  = new VaccinationDAO();
    private final EnfantDAO       enfantDAO       = new EnfantDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    // ────── INIT ──────
    @FXML
    public void initialize() {
        champDose.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        chargerEnfants();
        champEnfant.setOnAction(e -> chargerVaccinations());
    }

    // ────── BOUTONS ──────
    @FXML
    public void onAjouter() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) { alerte("Sélectionnez un enfant", AlertType.WARNING); return; }
        if (champNomVaccin.getText().isBlank()) { alerte("Nom du vaccin obligatoire", AlertType.WARNING); return; }
        if (champDateVaccin.getValue() == null) { alerte("Date obligatoire", AlertType.WARNING); return; }

        int dose = champDose.getValue();
        if (dose < 1) { alerte("Dose minimum = 1", AlertType.WARNING); return; }

        Vaccination v = new Vaccination();
        v.setIdCarnetDeSante(enfant.getIdCarnetDeSante());
        v.setNomVaccin(champNomVaccin.getText().trim());
        v.setDateVaccin(champDateVaccin.getValue());
        v.setDose(dose);
        v.setRappel(champRappel.getValue());
        v.setIdMedecin(0);

        try {
            vaccinationDAO.ajouter(v);

            // Notification si dose 1
            if (dose == 1) {
                LocalDate dateRappel = champDateVaccin.getValue().plusMonths(6);
                Notification notif = new Notification();
                notif.setIdEnfant(enfant.getIdEnfant());
                notif.setMessage("Rappel vaccin \"" + v.getNomVaccin() + "\" prévu le " + dateRappel);
                notificationDAO.ajouter(notif);
            }

            chargerVaccinations();
            vider();
            alerte("✓ Vaccination enregistrée", AlertType.INFORMATION);
        } catch (SQLException e) {
            alerte("✗ Erreur : " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void onSupprimer() {
        Vaccination sel = tableVaccinations.getSelectionModel().getSelectedItem();
        if (sel == null) { alerte("Sélectionnez une vaccination", AlertType.WARNING); return; }

        try {
            vaccinationDAO.supprimer(sel.getIdVaccination());
            chargerVaccinations();
            alerte("✓ Vaccination supprimée", AlertType.INFORMATION);
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

    private void chargerVaccinations() {
        Enfant enfant = champEnfant.getValue();
        if (enfant == null) return;
        try {
            List<Vaccination> liste = vaccinationDAO.trouverParCarnet(enfant.getIdCarnetDeSante());
            tableVaccinations.setItems(FXCollections.observableArrayList(liste));
        } catch (SQLException e) {
            alerte("✗ Impossible de charger : " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void vider() {
        champNomVaccin.clear();
        champDateVaccin.setValue(null);
        champDose.getValueFactory().setValue(1);
        champRappel.setValue(null);
    }

    private void alerte(String msg, AlertType type) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
}
