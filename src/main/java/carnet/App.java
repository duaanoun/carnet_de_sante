package carnet;

import carnet.config.DatabaseConfig;
import carnet.session.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entrée de l'application JavaFX.
 * 
 * Cette classe lance l'interface graphique et gère :
 *   - Initialisation de la connexion MySQL
 *   - Chargement de la première vue (EnfantView)
 *   - Fermeture propre de la base de données
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Simuler une connexion utilisateur (à remplacer par le C++)
        // En production, cette valeur vient de l'authentification C++
        SessionManager.getInstance().setIdParent(1);

        // Charger la vue principale (EnfantView.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/carnet/view/EnfantView.fxml"));
        Parent root = loader.load();

        // Créer la scène
        Scene scene = new Scene(root, 900, 700);

        // Configurer la fenêtre
        stage.setTitle("Carnet de Santé - Gestion des Enfants");
        stage.setScene(scene);
        stage.show();

        System.out.println("[App] ✓ Application démarrée");
    }

    @Override
    public void stop() throws Exception {
        // Fermer proprement la connexion à la base de données
        DatabaseConfig.fermer();
        SessionManager.getInstance().deconnecter();
        System.out.println("[App] ✓ Application arrêtée");
        super.stop();
    }

    /**
     * Point d'entrée de l'application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
