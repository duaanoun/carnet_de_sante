package carnet.model;

import java.time.LocalDateTime;

/**
 * Représente une notification de la table Notification.
 * Colonnes SQL : id_notification, message, dateEnvoiNot, statutNot, id_enfant
 *
 * ENUM statutNot : 'non_lu' | 'lu'
 * Utilisé pour les rappels vaccins et consultations.
 */
public class Notification {

    private int           idNotification;
    private String        message;
    private LocalDateTime dateEnvoiNot;
    private String        statutNot;     // ENUM
    private int           idEnfant;

    public Notification() {}

    public Notification(int idNotification, String message, LocalDateTime dateEnvoiNot,
                        String statutNot, int idEnfant) {
        this.idNotification = idNotification;
        this.message        = message;
        this.dateEnvoiNot   = dateEnvoiNot;
        this.statutNot      = statutNot;
        this.idEnfant       = idEnfant;
    }

    // ────── GETTERS ──────
    public int           getIdNotification() { return idNotification; }
    public String        getMessage()        { return message; }
    public LocalDateTime getDateEnvoiNot()   { return dateEnvoiNot; }
    public String        getStatutNot()      { return statutNot; }
    public int           getIdEnfant()       { return idEnfant; }

    // ────── SETTERS ──────
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
