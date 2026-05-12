package carnet.model;

import java.time.LocalDateTime;

/**
 * Correspond à la table : Consultation
 * Colonnes : id_consultation, date_Cons, motifC, statutC,
 *            id_carnetDeSante, id_medecin
 */
public class Consultation {

    private int           idConsultation;
    private LocalDateTime dateCons;
    private String        motifC;
    // ENUM MySQL : 'planifiee' | 'realisee' | 'annulee'
    private String        statutC;
    private int           idCarnetDeSante;
    private int           idMedecin; // Référence vers Medecin (géré en C++)

    public Consultation() {}

    public Consultation(int idConsultation, LocalDateTime dateCons,
                        String motifC, String statutC,
                        int idCarnetDeSante, int idMedecin) {
        this.idConsultation  = idConsultation;
        this.dateCons        = dateCons;
        this.motifC          = motifC;
        this.statutC         = statutC;
        this.idCarnetDeSante = idCarnetDeSante;
        this.idMedecin       = idMedecin;
    }

    // Getters
    public int           getIdConsultation()  { return idConsultation; }
    public LocalDateTime getDateCons()        { return dateCons; }
    public String        getMotifC()          { return motifC; }
    public String        getStatutC()         { return statutC; }
    public int           getIdCarnetDeSante() { return idCarnetDeSante; }
    public int           getIdMedecin()       { return idMedecin; }

    // Setters
    public void setIdConsultation(int id)       { this.idConsultation = id; }
    public void setDateCons(LocalDateTime d)    { this.dateCons = d; }
    public void setMotifC(String motif)         { this.motifC = motif; }
    public void setStatutC(String statut)       { this.statutC = statut; }
    public void setIdCarnetDeSante(int id)      { this.idCarnetDeSante = id; }
    public void setIdMedecin(int id)            { this.idMedecin = id; }

    @Override
    public String toString() {
        return dateCons.toLocalDate() + " — " + motifC + " [" + statutC + "]";
    }
}
