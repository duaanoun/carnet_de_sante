# Section 2 — Java : Carnet de Santé

## Architecture simplifiée : FXML → Controller → Database

```
src/main/java/carnet/
├── model/
│   ├── Enfant.java          ← Données d'un enfant
│   ├── CarnetDeSante.java   ← Regroupe vaccinations + consultations
│   ├── Vaccination.java     ← Un acte vaccinal
│   ├── Consultation.java    ← Une visite médicale
│   └── Examen.java          ← (optionnel)
│
├── dao/
│   ├── DatabaseConnection.java  ← Connexion MySQL unique (Singleton)
│   ├── EnfantDAO.java           ← CRUD enfant
│   ├── VaccinationDAO.java      ← CRUD vaccination
│   └── ConsultationDAO.java     ← CRUD consultation
│
└── controller/
    ├── EnfantController.java        ← Gestion des enfants
    ├── VaccinationController.java   ← Ajout de vaccinations
    ├── ConsultationController.java  ← Ajout de consultations
    └── HistoriqueController.java    ← Affichage de l'historique

src/main/resources/carnet/view/
    ├── EnfantView.fxml
    ├── VaccinationView.fxml
    ├── ConsultationView.fxml
    └── HistoriqueView.fxml
```

---

## Ce que fait chaque couche

| Couche       | Rôle                                            | Fichiers                  |
|--------------|-------------------------------------------------|---------------------------|
| **Modèle**   | Représente les données (pas de logique)         | `Enfant`, `Vaccination`… |
| **DAO**      | Requêtes SQL avec `PreparedStatement`           | `EnfantDAO`, `VaccinationDAO`… |
| **Controller** | Réagit aux clics, valide les champs, appelle le DAO | `EnfantController`… |
| **FXML**     | Interface graphique JavaFX                      | `EnfantView.fxml`… |

---

## Ce qui N'EST PAS dans la section Java

| Fonctionnalité       | Où c'est géré |
|----------------------|---------------|
| Authentification     | C++ (Section 1) |
| Gestion des comptes  | C++ (Section 1) |
| Rôles utilisateurs   | C++ (Section 1) |
| Médecins / Parents   | C++ (Section 1) |
| Sessions             | C++ (Section 1) |

Le lien entre les deux sections se fait via `id_parent` stocké en base.

---

## Technologies utilisées

- **Java 17+**
- **JavaFX 21** (interface graphique)
- **JDBC** (connexion à MySQL)
- **MySQL** (base de données)
- `PreparedStatement` pour éviter les injections SQL

---

## Comment expliquer à la soutenance

1. "La section C++ gère tout ce qui est utilisateur : connexion, rôles, sessions."
2. "La section Java se concentre uniquement sur les données médicales de l'enfant."
3. "L'architecture est simple : la vue FXML appelle le controller, qui appelle le DAO, qui fait la requête SQL."
4. "On utilise des `PreparedStatement` pour sécuriser les requêtes contre les injections SQL."
