-- ============================================================
-- SECTION 2 : Tables Java — Carnet de Santé
-- Ces tables s'ajoutent à la base existante de la SECTION C++
-- ============================================================

-- Table des enfants (gérée côté Java)
CREATE TABLE IF NOT EXISTS enfant (
    id             INT          AUTO_INCREMENT PRIMARY KEY,
    nom            VARCHAR(100) NOT NULL,
    prenom         VARCHAR(100) NOT NULL,
    date_naissance DATE         NOT NULL,
    sexe           CHAR(1)      NOT NULL CHECK (sexe IN ('M', 'F')),
    id_parent      INT          NOT NULL,   -- Référence vers l'utilisateur C++
    FOREIGN KEY (id_parent) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- Table des vaccinations
CREATE TABLE IF NOT EXISTS vaccination (
    id               INT          AUTO_INCREMENT PRIMARY KEY,
    id_enfant        INT          NOT NULL,
    nom_vaccin       VARCHAR(150) NOT NULL,
    date_vaccination DATE         NOT NULL,
    remarques        TEXT,
    FOREIGN KEY (id_enfant) REFERENCES enfant(id) ON DELETE CASCADE
);

-- Table des consultations médicales
CREATE TABLE IF NOT EXISTS consultation (
    id                 INT          AUTO_INCREMENT PRIMARY KEY,
    id_enfant          INT          NOT NULL,
    date_consultation  DATE         NOT NULL,
    motif              VARCHAR(255) NOT NULL,
    diagnostic         TEXT,
    traitement         TEXT,
    nom_medecin        VARCHAR(150),
    FOREIGN KEY (id_enfant) REFERENCES enfant(id) ON DELETE CASCADE
);

-- Table des examens (optionnel)
CREATE TABLE IF NOT EXISTS examen (
    id          INT          AUTO_INCREMENT PRIMARY KEY,
    id_enfant   INT          NOT NULL,
    date_examen DATE         NOT NULL,
    type_examen VARCHAR(100) NOT NULL,
    resultat    TEXT,
    FOREIGN KEY (id_enfant) REFERENCES enfant(id) ON DELETE CASCADE
);
