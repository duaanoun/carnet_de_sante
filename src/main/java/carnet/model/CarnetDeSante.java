package carnet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un carnet de santé — conteneur des données médicales d'un enfant.
 * Utilité : regrouper vaccinations, consultations, examens, croissance.
 * Affiché dans HistoriqueController.
 */
public class CarnetDeSante {

    private int               idCarnetDeSante;
    private Enfant            enfant;
    private List<Consultation> consultations      = new ArrayList<>();
    private List<Vaccination>  vaccinations       = new ArrayList<>();
    private List<Examen>       examens            = new ArrayList<>();
    private List<Croissance>   mesuresCroissance  = new ArrayList<>();

    public CarnetDeSante() {}

    public CarnetDeSante(int idCarnetDeSante, Enfant enfant) {
        this.idCarnetDeSante = idCarnetDeSante;
        this.enfant          = enfant;
    }

    // ────── GETTERS ──────
    public int               getIdCarnetDeSante()   { return idCarnetDeSante; }
    public Enfant            getEnfant()             { return enfant; }
    public List<Consultation> getConsultations()     { return consultations; }
    public List<Vaccination>  getVaccinations()      { return vaccinations; }
    public List<Examen>       getExamens()           { return examens; }
    public List<Croissance>   getMesuresCroissance() { return mesuresCroissance; }

    // ────── SETTERS ──────
    public void setIdCarnetDeSante(int id)                { this.idCarnetDeSante = id; }
    public void setEnfant(Enfant enfant)                  { this.enfant = enfant; }
    public void setConsultations(List<Consultation> l)    { this.consultations = l; }
    public void setVaccinations(List<Vaccination> l)      { this.vaccinations = l; }
    public void setExamens(List<Examen> l)                { this.examens = l; }
    public void setMesuresCroissance(List<Croissance> l)  { this.mesuresCroissance = l; }
}
