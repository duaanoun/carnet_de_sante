package carnet.model;

import java.time.LocalDate;

/**
 * Représente une mesure de croissance de la table Croissance.
 * Colonnes SQL : id_Croissance, dateC, tailleC, PoidsC, id_carnetDeSante
 *
 * Contrainte SQL : UNIQUE KEY (id_carnetDeSante, dateC)
 * → Une seule mesure par enfant par jour (UPDATE au lieu de INSERT si dupliquée).
 */
public class Croissance {

    private int       idCroissance;
    private LocalDate dateC;
    private double    tailleC;         // cm, DECIMAL(5,2)
    private double    poidsC;          // kg, DECIMAL(5,2)
    private int       idCarnetDeSante;

    public Croissance() {}

    public Croissance(int idCroissance, LocalDate dateC, double tailleC,
                      double poidsC, int idCarnetDeSante) {
        this.idCroissance    = idCroissance;
        this.dateC           = dateC;
        this.tailleC         = tailleC;
        this.poidsC          = poidsC;
        this.idCarnetDeSante = idCarnetDeSante;
    }

    // ────── GETTERS ──────
    public int       getIdCroissance()    { return idCroissance; }
    public LocalDate getDateC()           { return dateC; }
    public double    getTailleC()         { return tailleC; }
    public double    getPoidsC()          { return poidsC; }
    public int       getIdCarnetDeSante() { return idCarnetDeSante; }

    // ────── SETTERS ──────
    public void setIdCroissance(int id)    { this.idCroissance = id; }
    public void setDateC(LocalDate dateC)  { this.dateC = dateC; }
    public void setTailleC(double taille)  { this.tailleC = taille; }
    public void setPoidsC(double poids)    { this.poidsC = poids; }
    public void setIdCarnetDeSante(int id) { this.idCarnetDeSante = id; }

    @Override
    public String toString() {
        return dateC + " — " + tailleC + " cm / " + poidsC + " kg";
    }
}
