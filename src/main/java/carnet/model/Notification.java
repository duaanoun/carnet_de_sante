package carnet.model;

import java.time.LocalDateTime;

/**
 * Correspond à la table : Notification
 * Colonnes : id_notification, message, dateEnvoiNot, statutNot, id_enfant
 *
 * Utilisée pour les rappels vaccins et consultations.
 */
public class Notification {

    private int           idNotification;
    private String        message;
    private LocalDateTime dateEnvoiNot;
    // ENUM MySQL : 'non_lu' | 'lu'
    private String        statutNot;
    private int           idEnfant;

    public Notification() {}

    public Notification(int idNotification, String message,
                        LocalDateTime dateEnvoiNot, String statutNot, int idEnfant) {
        this.idNotification = idNotification;
        this.message        = message;
        this.dateEnvoiNot   = dateEnvoiNot;
        this.statutNot      = statutNot;
        this.idEnfant       = idEnfant;
    }

    // Getters
    public int           getIdNotification() { return idNotification; }
    public String        getMessage()        { return message; }
    public LocalDateTime getDateEnvoiNot()   { return dateEnvoiNot; }
    public String        getStatutNot()      { return statutNot; }
    public int           getIdEnfant()       { return idEnfant; }

    // Setters
    public void setIdNotification(int id)        { this.idNotification = id; }
    public void setMessage(String message)       { this.message = message; }
    public void setDateEnvoiNot(LocalDateTime d) { this.dateEnvoiNot = d; }
    public void setStatutNot(String statut)      { this.statutNot = statut; }
    public void setIdEnfant(int id)              { this.idEnfant = id; }

    @Override
    public String toString() {
        return "[" + statutNot + "] " + message;
    }
}
