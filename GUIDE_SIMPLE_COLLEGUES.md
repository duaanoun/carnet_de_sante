# 📚 GUIDE ULTRA-SIMPLE POUR MES COLLÈGUES

## 🎯 **C'est quoi ce projet ?**

**Carnet de Santé** = Une application pour suivre les vaccins et consultations des enfants.

```
Enfant (Alice, 5 ans)
    ↓
    Vaccinations (ROR, DTP, ...)
    Consultations (visite routine, otite, ...)
    Croissance (taille, poids par jour)
    Notifications (rappels automatiques)
```

---

## 🚀 **DÉMARRAGE (3 étapes)**

### **1️⃣ Récupérer les fichiers**

```bash
git checkout java-section2-fixed
git pull origin java-section2-fixed
```

### **2️⃣ Recharger IntelliJ**

- **File → Reload Project**
- Attendez 30 secondes (Maven télécharge)
- Vert = OK, Rouge = Erreur

### **3️⃣ Configurer la base de données**

Ouvrez : `src/main/java/carnet/config/DatabaseConfig.java`

Changez **ligne 14** :
```java
private static final String PASSWORD = "votre_password_mysql";  // ← VOTRE PASSWORD
```

---

## 🗄️ **CRÉER LA BASE DE DONNÉES**

### Dans MySQL Workbench :

```sql
-- 1. Créer la base
CREATE DATABASE carnetdesante;
USE carnetdesante;

-- 2. Copier-coller le schéma fourni (image 1 du projet)
```

---

## ▶️ **LANCER L'APPLICATION**

### Dans IntelliJ :

1. **Clic droit** sur `src/main/java/carnet/App.java`
2. **Run 'App.main()'**

Vous verrez une fenêtre JavaFX ! 🎉

---

## 📁 **STRUCTURE (Simple)**

```
src/main/java/carnet/
│
├── App.java                        ← LANCE L'APP
│
├── config/
│   └── DatabaseConfig.java         ← Connexion MySQL
│
├── session/
│   └── SessionManager.java         ← Qui est connecté ?
│
├── model/                          ← Les DONNÉES
│   ├── Enfant.java
│   ├── Vaccination.java
│   └── ...
│
├── dao/                            ← SQL (Ajouter, Modifier, Supprimer)
│   ├── EnfantDAO.java
│   ├── VaccinationDAO.java
│   └── ...
│
└── controller/                     ← Réagir aux clics
    ├── EnfantController.java
    ├── VaccinationController.java
    └── ...
```

---

## 🧠 **C'EST QUOI UN DAO ? (5 min)**

### **DAO = Accès à la base de données**

```java
// Vous voulez ajouter un enfant ?
Enfant alice = new Enfant();
alice.setNom("Dupont");
alice.setPrenom("Alice");

// Utilisez le DAO :
EnfantDAO dao = new EnfantDAO();
dao.ajouter(alice);  // ← Ça fait : INSERT INTO Enfant ...
```

**Avantage :** 
- Vous écrivez pas du SQL compliqué
- Vous appelez simplement `dao.ajouter(alice)`
- Le DAO gère tout

---

## 🔐 **C'EST QUOI SessionManager ? (3 min)**

### **Problem :** 
```java
// ❌ Mauvais : chaque fois on dit "parent 1"
private int idParent = 1;  // TOUJOURS 1 !
```

### **Solution :**
```java
// ✅ Bon : on mémorise qui est connecté
SessionManager.getInstance().setIdParent(123);

// Et partout ailleurs :
int id = SessionManager.getInstance().getIdParent();  // → 123
```

**C'est un Singleton = une seule instance partagée**

---

## ❌ **ERREURS COURANTES**

### **Erreur 1 : "Cannot find symbol 'SessionManager'"**

**Solution :**
```bash
git pull origin java-section2-fixed
File → Reload Project
```

### **Erreur 2 : "Cannot find resource EnfantView.fxml"**

**Solution :**
Vérifiez le chemin :
```
src/main/resources/carnet/view/EnfantView.fxml
```

### **Erreur 3 : "Connection refused 127.0.0.1:3306"**

**Solution :**
- MySQL n'est pas démarré
- Ou le password est faux dans `DatabaseConfig.java`

### **Erreur 4 : "BUILD FAILED"**

**Solution :**
```bash
mvn clean compile
```

---

## 📖 **POUR LA SOUTENANCE (Slides)**

### **Slide 1 : Architecture**
> "L'appli a 4 couches :
> - **Model** : Enfant, Vaccination (les données)
> - **DAO** : EnfantDAO, VaccinationDAO (accès DB)
> - **Controller** : Réagit aux clics (logique UI)
> - **View** : FXML (interface graphique)
>
> Avantage = chaque couche a 1 responsabilité"

### **Slide 2 : Base de données**
> "7 tables : Enfant, Vaccination, Consultation, Croissance, Examen, Notification, CarnetDeSante
> Toutes liées par des Foreign Keys
> Gérées avec PreparedStatement (sécurité contre injections SQL)"

### **Slide 3 : Fonctionnalités**
> "- Ajouter/Modifier/Supprimer enfants
> - Vaccins avec rappels auto (dose 1 → rappel +6 mois)
> - Consultations avec statuts (planifiée/réalisée/annulée)
> - Historique médical complet par enfant"

### **Slide 4 : Session Parent**
> "SessionManager Singleton = une seule instance
> C++ transmet idParent après login
> Chaque controller récupère via getInstance().getIdParent()"

---

## 🧪 **TESTER L'APPLICATION**

### **Étape 1 : Ajouter un enfant**
1. Cliquez sur le tab "Enfants"
2. Remplissez : Nom, Prénom, Date, Sexe
3. Cliquez "Ajouter"
4. Vous devez voir l'enfant dans la table ✅

### **Étape 2 : Ajouter une vaccination**
1. Cliquez sur le tab "Vaccinations"
2. Sélectionnez l'enfant
3. Remplissez : Nom vaccin, Date, Dose
4. Cliquez "Ajouter"
5. Vous devez voir la vaccination ✅

### **Étape 3 : Voir l'historique**
1. Cliquez sur le tab "Historique"
2. Sélectionnez l'enfant
3. Vous voyez vaccins, consultations, croissance ✅

---

## 🤔 **QUESTIONS FRÉQUENTES**

**Q: Pourquoi Maven ?**
R: Ça télécharge les dépendances (JavaFX, MySQL) automatiquement.

**Q: Pourquoi FXML ?**
R: C'est du XML pour la UI (comme HTML mais pour JavaFX).

**Q: Pourquoi Singleton pour SessionManager ?**
R: Une seule instance = pas de confusion, pas de doublons.

**Q: PreparedStatement c'est quoi ?**
R: Ça sécurise les requêtes SQL (protection contre hacker).

---

## 📞 **BESOIN D'AIDE ?**

1. Vérifiez `pom.xml` existe à la racine
2. Relancez `Maven clean compile`
3. Relancez IntelliJ
4. Vérifiez `DatabaseConfig.java` (password MySQL)
5. Vérifiez la base de données est créée

---

**Tout clair ? C'est prêt pour la soutenance ! 🎉**
