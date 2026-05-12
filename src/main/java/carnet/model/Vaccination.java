package carnet.model;

import java.time.LocalDate;

/**
 * Correspond à la table : Vaccination
 * Colonnes : id_Vaccination, NomVaccin, dateVaccin, dose, rappel,
 *            id_carnetDeSante, id_medecin
 */
public class Vaccination {

    private int       idVaccination;
    private String    nomVaccin;
    private LocalDate dateVaccin;
    private int       dose;         // DEFAULT 1 en base
    private LocalDate rappel;       // Peut être NULL
    private int       idCarnetDeSante;
    private int       idMedecin;

    public Vaccination() {}

    public Vaccination(int idVaccination, String nomVaccin, LocalDate dateVaccin,
                       int dose, LocalDate rappel, int idCarnetDeSante, int idMedecin) {
        this.idVaccination   = idVaccination;
        this.nomVaccin       = nomVaccin;
        this.dateVaccin      = dateVaccin;
        this.dose            = dose;
        this.rappel          = rappel;
        this.idCarnetDeSante = idCarnetDeSante;
        this.idMedecin       = idMedecin;
    }

    // Getters
    public int       getIdVaccination()   { return idVaccination; }
    public String    getNomVaccin()       { return nomVaccin; }
    public LocalDate getDateVaccin()      { return dateVaccin; }
    public int       getDose()            { return dose; }
    public LocalDate getRappel()          { return rappel; }   // peut être null
    public int       getIdCarnetDeSante() { return idCarnetDeSante; }
    public int       getIdMedecin()       { return idMedecin; }

    // Setters
    public void setIdVaccination(int id)     { this.idVaccination = id; }
    public void setNomVaccin(String nom)     { this.nomVaccin = nom; }
    public void setDateVaccin(LocalDate d)   { this.dateVaccin = d; }
    public void setDose(int dose)            { this.dose = dose; }
    public void setRappel(LocalDate rappel)  { this.rappel = rappel; }
    public void setIdCarnetDeSante(int id)   { this.idCarnetDeSante = id; }
    public void setIdMedecin(int id)         { this.idMedecin = id; }

    @Override
    public String toString() {
        return nomVaccin + " — dose " + dose + " (" + dateVaccin + ")";
    }
}
