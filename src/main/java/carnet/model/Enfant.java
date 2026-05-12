package carnet.model;

import java.time.LocalDate;

/**
 * Correspond à la table : Enfant
 * Colonnes : id_enfant, NomEn, PrenomEN, dateNaissance, sexe, groupeSanguin,
 *            id_carnetDeSante, id_parent
 */
public class Enfant {

    private int       idEnfant;
    private String    nom;
    private String    prenom;
    private LocalDate dateNaissance;
    private String    sexe;          // "M" ou "F"
    private String    groupeSanguin; // Optionnel (peut être null)
    private int       idCarnetDeSante;
    private int       idParent;      // Référence vers table parent (gérée en C++)

    public Enfant() {}

    public Enfant(int idEnfant, String nom, String prenom,
                  LocalDate dateNaissance, String sexe,
                  String groupeSanguin, int idCarnetDeSante, int idParent) {
        this.idEnfant        = idEnfant;
        this.nom             = nom;
        this.prenom          = prenom;
        this.dateNaissance   = dateNaissance;
        this.sexe            = sexe;
        this.groupeSanguin   = groupeSanguin;
        this.idCarnetDeSante = idCarnetDeSante;
        this.idParent        = idParent;
    }

    // Getters
    public int       getIdEnfant()        { return idEnfant; }
    public String    getNom()             { return nom; }
    public String    getPrenom()          { return prenom; }
    public LocalDate getDateNaissance()   { return dateNaissance; }
    public String    getSexe()            { return sexe; }
    public String    getGroupeSanguin()   { return groupeSanguin; }
    public int       getIdCarnetDeSante() { return idCarnetDeSante; }
    public int       getIdParent()        { return idParent; }

    // Setters
    public void setIdEnfant(int id)              { this.idEnfant = id; }
    public void setNom(String nom)               { this.nom = nom; }
    public void setPrenom(String prenom)         { this.prenom = prenom; }
    public void setDateNaissance(LocalDate d)    { this.dateNaissance = d; }
    public void setSexe(String sexe)             { this.sexe = sexe; }
    public void setGroupeSanguin(String gs)      { this.groupeSanguin = gs; }
    public void setIdCarnetDeSante(int id)       { this.idCarnetDeSante = id; }
    public void setIdParent(int id)              { this.idParent = id; }

    /** Utilisé dans les ChoiceBox JavaFX pour afficher l'enfant */
    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}
