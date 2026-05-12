package carnet.model;

import java.time.LocalDate;

/**
 * Représente un examen de la table Examen.
 * Colonnes SQL : id_examen, dateExamen, details, id_carnetDeSante, id_medecin
 */
public class Examen {

    private int       idExamen;
    private LocalDate dateExamen;
    private String    details;
    private int       idCarnetDeSante;
    private int       idMedecin;

    public Examen() {}

    public Examen(int idExamen, LocalDate dateExamen, String details,
                  int idCarnetDeSante, int idMedecin) {
        this.idExamen        = idExamen;
        this.dateExamen      = dateExamen;
        this.details         = details;
        this.idCarnetDeSante = idCarnetDeSante;
        this.idMedecin       = idMedecin;
    }

    // ────── GETTERS ──────
    public int       getIdExamen()        { return idExamen; }
    public LocalDate getDateExamen()      { return dateExamen; }
    public String    getDetails()         { return details; }
    public int       getIdCarnetDeSante() { return idCarnetDeSante; }
    public int       getIdMedecin()       { return idMedecin; }

    // ────── SETTERS ──────
    public void setIdExamen(int id)        { this.idExamen = id; }
    public void setDateExamen(LocalDate d) { this.dateExamen = d; }
    public void setDetails(String details) { this.details = details; }
    public void setIdCarnetDeSante(int id) { this.idCarnetDeSante = id; }
    public void setIdMedecin(int id)       { this.idMedecin = id; }

    @Override
    public String toString() {
        return dateExamen + " — " + details;
    }
}
