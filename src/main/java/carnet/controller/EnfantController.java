package carnet.controller;

import carnet.dao.EnfantDAO;
import carnet.model.Enfant;
import carnet.session.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.util.List;

/**
 * Gère l'ajout, modification, suppression des enfants.
 * Lié à EnfantView.fxml
 *
 * Simple et clair :
 *   1. Charger la liste des enfants du parent connecté
 *   2. Ajouter/Modifier/Supprimer
 *   3. Afficher erreurs avec Alert
 */
public class EnfantController {

    @FXML private TableView<Enfant>           tableEnfants;
    @FXML private TableColumn<Enfant, String> colNom;
    @FXML private TableColumn<Enfant, String> colPrenom;
    @FXML private TableColumn<Enfant, String> colDateNaissance;
    @FXML private TableColumn<Enfant, String> colSexe;

    @FXML private TextField      champNom;
    @FXML private TextField      champPrenom;
    @FXML private DatePicker     champDateNaissance;
    @FXML private ChoiceBox<String> champSexe;
    @FXML private TextField      champGroupeSanguin;

    private final EnfantDAO enfantDAO = new EnfantDAO();

    // ────── INIT ──────
    @FXML
    public void initialize() {
        champSexe.setItems(FXCollections.observableArrayList("M", "F"));
        tableEnfants.getSelectionModel().selectedItemProperty().addListener(
            (obs, ancien, nouveau) -> {
                if (nouveau != null) remplirFormulaire(nouveau);
            }
        );
        chargerEnfants();
    }

    // ────── BOUTONS ──────
    @FXML
    public void onAjouter() {
        if (!valider()) return;

        Enfant enfant = new Enfant();
        enfant.setNom(champNom.getText().trim());
        enfant.setPrenom(champPrenom.getText().trim());
        enfant.setDateNaissance(champDateNaissance.getValue());
        enfant.setSexe(champSexe.getValue());
        enfant.setGroupeSanguin(champGroupeSanguin.getText().trim().isEmpty() ? null : champGroupeSanguin.getText().trim());
        enfant.setIdParent(SessionManager.getInstance().getIdParent());

        try {
            enfantDAO.ajouter(enfant);
            chargerEnfants();
            vider();
            alerte("✓ Enfant ajouté avec succès", AlertType.INFORMATION);
        } catch (SQLException e) {
            alerte("✗ Erreur : " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void onModifier() {
        Enfant sel = tableEnfants.getSelectionModel().getSelectedItem();
        if (sel == null) { alerte("Sélectionnez un enfant", AlertType.WARNING); return; }
        if (!valider()) return;

        sel.setNom(champNom.getText().trim());
        sel.setPrenom(champPrenom.getText().trim());
        sel.setDateNaissance(champDateNaissance.getValue());
        sel.setSexe(champSexe.getValue());
        sel.setGroupeSanguin(champGroupeSanguin.getText().trim().isEmpty() ? null : champGroupeSanguin.getText().trim());

        try {
            enfantDAO.modifier(sel);
            chargerEnfants();
            vider();
            alerte("✓ Enfant modifié", AlertType.INFORMATION);
        } catch (SQLException e) {
            alerte("✗ Erreur : " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void onSupprimer() {
        Enfant sel = tableEnfants.getSelectionModel().getSelectedItem();
        if (sel == null) { alerte("Sélectionnez un enfant", AlertType.WARNING); return; }

        Alert conf = new Alert(AlertType.CONFIRMATION, "Supprimer " + sel.getPrenom() + " ?", ButtonType.YES, ButtonType.NO);
        conf.showAndWait().ifPresent(rep -> {
            if (rep == ButtonType.YES) {
                try {
                    enfantDAO.supprimer(sel.getIdEnfant());
                    chargerEnfants();
                    vider();
                    alerte("✓ Enfant supprimé", AlertType.INFORMATION);
                } catch (SQLException e) {
                    alerte("✗ Erreur : " + e.getMessage(), AlertType.ERROR);
                }
            }
        });
    }

    // ────── UTILITAIRES ──────
    private void chargerEnfants() {
        try {
            int idParent = SessionManager.getInstance().getIdParent();
            List<Enfant> enfants = enfantDAO.trouverParParent(idParent);
            tableEnfants.setItems(FXCollections.observableArrayList(enfants));
        } catch (SQLException e) {
            alerte("✗ Impossible de charger : " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void remplirFormulaire(Enfant e) {
        champNom.setText(e.getNom());
        champPrenom.setText(e.getPrenom());
        champDateNaissance.setValue(e.getDateNaissance());
        champSexe.setValue(e.getSexe());
        champGroupeSanguin.setText(e.getGroupeSanguin() != null ? e.getGroupeSanguin() : "");
    }

    private void vider() {
        champNom.clear();
        champPrenom.clear();
        champDateNaissance.setValue(null);
        champSexe.setValue(null);
        champGroupeSanguin.clear();
        tableEnfants.getSelectionModel().clearSelection();
    }

    private boolean valider() {
        if (champNom.getText().isBlank() || champPrenom.getText().isBlank()) {
            alerte("Nom et prénom obligatoires", AlertType.WARNING);
            return false;
        }
        if (champDateNaissance.getValue() == null) {
            alerte("Sélectionnez une date", AlertType.WARNING);
            return false;
        }
        if (champSexe.getValue() == null) {
            alerte("Sélectionnez M ou F", AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void alerte(String msg, AlertType type) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
}
