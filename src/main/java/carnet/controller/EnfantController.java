package carnet.controller;

import carnet.dao.EnfantDAO;
import carnet.model.Enfant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller pour la gestion des enfants.
 * Lié à EnfantView.fxml
 *
 * Responsabilités :
 *   - Afficher la liste des enfants du parent connecté
 *   - Ajouter / Modifier / Supprimer un enfant
 */
public class EnfantController {

    // ── Composants FXML ───────────────────────────────────────────────────────
    @FXML private TableView<Enfant>              tableEnfants;
    @FXML private TableColumn<Enfant, String>    colNom;
    @FXML private TableColumn<Enfant, String>    colPrenom;
    @FXML private TableColumn<Enfant, String>    colDateNaissance;
    @FXML private TableColumn<Enfant, String>    colSexe;
    @FXML private TableColumn<Enfant, String>    colGroupeSanguin;

    @FXML private TextField         champNom;           // → NomEn
    @FXML private TextField         champPrenom;        // → PrenomEN
    @FXML private DatePicker        champDateNaissance; // → dateNaissance
    @FXML private ChoiceBox<String> champSexe;          // → sexe ENUM
    @FXML private TextField         champGroupeSanguin; // → groupeSanguin (optionnel)

    // ── Dépendances ───────────────────────────────────────────────────────────
    private final EnfantDAO enfantDAO = new EnfantDAO();

    // ID du parent connecté — transmis depuis la session C++ (fichier ou argument)
    private int idParentConnecte = 1; // ← Remplacer par la valeur réelle

    // ── Initialisation JavaFX ─────────────────────────────────────────────────
    @FXML
    public void initialize() {
        champSexe.setItems(FXCollections.observableArrayList("M", "F"));

        // Remplir le formulaire quand on clique sur un enfant dans la table
        tableEnfants.getSelectionModel().selectedItemProperty().addListener(
            (obs, ancien, selectionne) -> {
                if (selectionne != null) remplirFormulaire(selectionne);
            }
        );

        chargerEnfants();
    }

    // ── Bouton : Ajouter ──────────────────────────────────────────────────────
    @FXML
    public void onAjouter() {
        if (!formulaireValide()) return;

        Enfant enfant = new Enfant();
        enfant.setNom(champNom.getText().trim());
        enfant.setPrenom(champPrenom.getText().trim());
        enfant.setDateNaissance(champDateNaissance.getValue());
        enfant.setSexe(champSexe.getValue());
        enfant.setGroupeSanguin(champGroupeSanguin.getText().trim().isEmpty()
                                ? null : champGroupeSanguin.getText().trim());
        enfant.setIdParent(idParentConnecte);

        try {
            enfantDAO.ajouter(enfant);
            chargerEnfants();
            viderFormulaire();
            afficherSucces("Enfant ajouté avec succès.");
        } catch (SQLException e) {
            afficherErreur("Erreur lors de l'ajout :\n" + e.getMessage());
        }
    }

    // ── Bouton : Modifier ─────────────────────────────────────────────────────
    @FXML
    public void onModifier() {
        Enfant selectionne = tableEnfants.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherErreur("Sélectionnez un enfant à modifier.");
            return;
        }
        if (!formulaireValide()) return;

        selectionne.setNom(champNom.getText().trim());
        selectionne.setPrenom(champPrenom.getText().trim());
        selectionne.setDateNaissance(champDateNaissance.getValue());
        selectionne.setSexe(champSexe.getValue());
        selectionne.setGroupeSanguin(champGroupeSanguin.getText().trim().isEmpty()
                                     ? null : champGroupeSanguin.getText().trim());

        try {
            enfantDAO.modifier(selectionne);
            chargerEnfants();
            viderFormulaire();
            afficherSucces("Enfant modifié avec succès.");
        } catch (SQLException e) {
            afficherErreur("Erreur lors de la modification :\n" + e.getMessage());
        }
    }

    // ── Bouton : Supprimer ────────────────────────────────────────────────────
    @FXML
    public void onSupprimer() {
        Enfant selectionne = tableEnfants.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherErreur("Sélectionnez un enfant à supprimer.");
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION,
            "Supprimer " + selectionne.getPrenom() + " " + selectionne.getNom() + " ?",
            ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(rep -> {
            if (rep == ButtonType.YES) {
                try {
                    enfantDAO.supprimer(selectionne.getIdEnfant());
                    chargerEnfants();
                    viderFormulaire();
                } catch (SQLException e) {
                    afficherErreur("Erreur suppression :\n" + e.getMessage());
                }
            }
        });
    }

    // ── Méthodes utilitaires privées ──────────────────────────────────────────

    private void chargerEnfants() {
        try {
            List<Enfant> enfants = enfantDAO.trouverParParent(idParentConnecte);
            tableEnfants.setItems(FXCollections.observableArrayList(enfants));
        } catch (SQLException e) {
            afficherErreur("Impossible de charger les enfants :\n" + e.getMessage());
        }
    }

    private void remplirFormulaire(Enfant e) {
        champNom.setText(e.getNom());
        champPrenom.setText(e.getPrenom());
        champDateNaissance.setValue(e.getDateNaissance());
        champSexe.setValue(e.getSexe());
        champGroupeSanguin.setText(e.getGroupeSanguin() != null ? e.getGroupeSanguin() : "");
    }

    private void viderFormulaire() {
        champNom.clear();
        champPrenom.clear();
        champDateNaissance.setValue(null);
        champSexe.setValue(null);
        champGroupeSanguin.clear();
        tableEnfants.getSelectionModel().clearSelection();
    }

    private boolean formulaireValide() {
        if (champNom.getText().isBlank() || champPrenom.getText().isBlank()) {
            afficherErreur("Le nom et le prénom sont obligatoires.");
            return false;
        }
        if (champDateNaissance.getValue() == null) {
            afficherErreur("La date de naissance est obligatoire.");
            return false;
        }
        if (champSexe.getValue() == null) {
            afficherErreur("Veuillez sélectionner le sexe.");
            return false;
        }
        return true;
    }

    private void afficherSucces(String msg) {
        new Alert(AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private void afficherErreur(String msg) {
        new Alert(AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
