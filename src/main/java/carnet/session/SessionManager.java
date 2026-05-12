package carnet.session;

/**
 * Gère la session utilisateur (parent connecté).
 * Remplace le idParentConnecte = 1 hardcodé.
 *
 * Utilisation :
 *   SessionManager.getInstance().setIdParent(123);  // À la connexion (C++)
 *   int id = SessionManager.getInstance().getIdParent();  // Dans les controllers
 *
 * Pattern Singleton.
 */
public class SessionManager {

    private static SessionManager instance = null;
    private int idParent = -1;  // -1 = pas connecté

    // Constructeur privé — Singleton
    private SessionManager() {}

    /**
     * Récupère l'instance unique de SessionManager.
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Définit l'ID du parent connecté (appelé après authentification C++).
     */
    public void setIdParent(int idParent) {
        this.idParent = idParent;
        System.out.println("[Session] ✓ Parent connecté : id=" + idParent);
    }

    /**
     * Récupère l'ID du parent connecté.
     */
    public int getIdParent() {
        if (idParent == -1) {
            throw new IllegalStateException("❌ Erreur : Aucun parent connecté. Authentifiez-vous d'abord.");
        }
        return idParent;
    }

    /**
     * Vérifie si un parent est connecté.
     */
    public boolean isConnecte() {
        return idParent != -1;
    }

    /**
     * Déconnecte le parent actuel.
     */
    public void deconnecter() {
        idParent = -1;
        System.out.println("[Session] ✓ Parent déconnecté");
    }
}
