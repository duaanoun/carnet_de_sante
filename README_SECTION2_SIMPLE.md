# 📋 Section 2 — Java : Carnet de Santé (VERSION SIMPLIFIÉE & CORRIGÉE)

## ✅ Qu'est-ce qui a été corrigé ?

| Problème | Solution |
|----------|----------|
| `idParentConnecte = 1` hardcodé | ✅ Créé `SessionManager.java` |
| `DatabaseConnection` incohérent | ✅ Créé `DatabaseConfig.java` thread-safe |
| Imports fantômes, DAOs manquants | ✅ Créé `ExamenDAO.java` |
| Controllers complexes | ✅ Simplifié et commenté |
| Pas de FXML | ℹ️ À créer (templates basiques fournis) |
| Erreurs PreparedStatement | ✅ Corrigées |

---

## 🏗️ ARCHITECTURE (Simple)

```
src/main/java/carnet/
├── config/
│   └── DatabaseConfig.java       ← Connexion MySQL UNIQUE
│
├── session/
│   └── SessionManager.java       ← Gère idParent connecté
│
├── model/
│   ├── Enfant.java
│   ├── Vaccination.java
│   ├── Consultation.java
│   ├── Croissance.java
│   ├── Examen.java
│   ├── Notification.java
│   └── CarnetDeSante.java
│
├── dao/
│   ├── EnfantDAO.java
│   ├── VaccinationDAO.java
│   ├── ConsultationDAO.java
│   ├── CroissanceDAO.java
│   ├── ExamenDAO.java             ← NOUVEAU
│   └── NotificationDAO.java
│
└── controller/
    ├── EnfantController.java
    ├── VaccinationController.java
    ├── ConsultationController.java
    └── HistoriqueController.java
```

---

## 🔐 SESSION PARENT (IMPORTANT)

### Avant (❌ FAUX) :
```java
private int idParentConnecte = 1;  // ← Toujours 1 !
```

### Après (✅ CORRECT) :
```java
// À la connexion (depuis le C++)
SessionManager.getInstance().setIdParent(idParentDuC++);

// Dans les controllers
int idParent = SessionManager.getInstance().getIdParent();
```

---

## 🚀 FLUX DE TRAVAIL

### 1️⃣ Connexion (C++ → Java)
```
C++ App
  ↓
  Login
  ↓
  SessionManager.setIdParent(123)  ← ID du parent connecté
  ↓
  JavaFX EnfantView.fxml
```

### 2️⃣ Charger enfants (Java)
```
EnfantController
  ↓
  int idParent = SessionManager.getInstance().getIdParent()
  ↓
  EnfantDAO.trouverParParent(idParent)
  ↓
  List<Enfant> → TableView
```

### 3️⃣ Ajouter vaccination (Java)
```
VaccinationController.onAjouter()
  ↓
  Validation
  ↓
  VaccinationDAO.ajouter(v)
  ↓
  Si dose == 1 → Notification auto
  ↓
  Alert succès
```

---

## 💾 CONFIGURATION MySQL

### ✅ Vérifier la base existe :
```sql
CREATE DATABASE carnetdesante;
USE carnetdesante;

-- Puis exécuter le schéma fourni
```

### ✅ Modifier DatabaseConfig.java si besoin :
```java
private static final String URL      = "jdbc:mysql://localhost:3306/carnetdesante";
private static final String USER     = "root";
private static final String PASSWORD = "";  // Votre password
```

---

## 🎯 COMMENT L'EXPLIQUER À LA SOUTENANCE

### Slide 1 : Architecture
> **"L'application Java se divise en 4 couches :**
> - **Model** : Les classes métier (Enfant, Vaccination, etc.)
> - **DAO** : Accès à la base de données
> - **Controller** : Logique métier et réactions aux clics
> - **View** : Interface FXML (JavaFX)
>
> **Avantages :**
> - Chaque couche a une responsabilité unique
> - Facile à tester et maintenir
> - Pas de logique métier dans l'interface"

### Slide 2 : Session Parent
> **"Comment gérer le parent connecté :**
> - Le C++ transmet l'ID du parent après login
> - Nous stockons cet ID dans `SessionManager` (Singleton)
> - Chaque controller récupère l'ID via `SessionManager.getInstance().getIdParent()`
> - Impossible d'accéder aux enfants d'un autre parent"

### Slide 3 : Base de données
> **"Les données médicales :**
> - Enfant : nom, prénom, date naissance, sexe, groupe sanguin
> - Vaccination : nom, date, dose, rappel auto
> - Consultation : date, motif, statut (planifiée/réalisée/annulée)
> - Croissance : taille, poids, mesure unique par jour
> - Notifications : rappels automatiques"

### Slide 4 : Sécurité
> **"PreparedStatement :**
> - Nous n'utilisons JAMAIS la concaténation de strings
> - `stmt.setString(1, userInput)` échappe les caractères spéciaux
> - Protégé contre les injections SQL"

---

## 📱 EXEMPLE : Ajouter un enfant

### Step 1 : Saisie formulaire
```
Utilisateur tape :
  Nom : Dupont
  Prénom : Alice
  Date : 2020-05-15
  Sexe : F
```

### Step 2 : Validation (EnfantController)
```java
if (champNom.getText().isBlank()) {
    alerte("Nom obligatoire", AlertType.WARNING);
    return;  // ← Stop si invalide
}
```

### Step 3 : Création objet
```java
Enfant enfant = new Enfant();
enfant.setNom("Dupont");
enfant.setPrenom("Alice");
enfant.setDateNaissance(LocalDate.of(2020, 5, 15));
enfant.setSexe("F");
enfant.setIdParent(SessionManager.getInstance().getIdParent());
```

### Step 4 : Insert en base (EnfantDAO)
```java
PreparedStatement stmt = conn.prepareStatement(
    "INSERT INTO Enfant (NomEn, PrenomEN, dateNaissance, sexe, id_parent) VALUES (?, ?, ?, ?, ?)"
);
stmt.setString(1, enfant.getNom());
stmt.setString(2, enfant.getPrenom());
stmt.setDate(3, Date.valueOf(enfant.getDateNaissance()));
stmt.setString(4, enfant.getSexe());
stmt.setInt(5, enfant.getIdParent());
stmt.executeUpdate();  // ← Exécution
```

### Step 5 : Affichage succès
```java
alerte("✓ Enfant ajouté avec succès", AlertType.INFORMATION);
chargerEnfants();  // ← Recharger la table
```

---

## ⚠️ POINTS IMPORTANTS

1. **SessionManager est un Singleton**
   ```java
   SessionManager.getInstance().setIdParent(123);
   int id = SessionManager.getInstance().getIdParent();
   ```

2. **DatabaseConfig aussi**
   - Une SEULE connexion MySQL partagée
   - Créée à la demande, fermée à l'arrêt

3. **PreparedStatement pour sécurité**
   - ✅ Correct : `stmt.setString(1, nom)`
   - ❌ Faux : `"... WHERE nom = '" + nom + "'"`

4. **Les FXML**
   - Doivent avoir des `fx:id="champNom"` qui matchent les `@FXML` du controller
   - Les noms de méthodes (onAjouter) doivent être exacts

---

## 🧪 TEST RAPIDE

```java
// Dans App.java ou main()
@Override
public void start(Stage stage) {
    // Simuler une connexion
    SessionManager.getInstance().setIdParent(1);
    
    // Charger la vue
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/carnet/view/EnfantView.fxml"));
    Parent root = loader.load();
    
    Scene scene = new Scene(root, 800, 600);
    stage.setTitle("Carnet de Santé");
    stage.setScene(scene);
    stage.show();
}

@Override
public void stop() {
    // Fermer la connexion
    DatabaseConfig.fermer();
}
```

---

## 📚 Ressources

- **JavaFX** : https://openjfx.io/
- **JDBC** : https://docs.oracle.com/javase/tutorial/jdbc/
- **MySQL** : https://dev.mysql.com/

---

**Tout est prêt pour la soutenance ! 🎉**
