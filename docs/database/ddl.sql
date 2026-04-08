-- https://drawsql.app/teams/tciles/diagrams/bookhub

-- Création des tables
CREATE TABLE roles (
    id INT PRIMARY KEY IDENTITY(1,1),
    libelle VARCHAR(20) NOT NULL -- USER, LIBRARIAN, ADMIN
);

CREATE TABLE utilisateurs (
    id INT PRIMARY KEY IDENTITY(1,1),
    email VARCHAR(100) UNIQUE NOT NULL, -- Validation format et unicité
    password VARCHAR(255) NOT NULL, -- Hachage BCrypt
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    telephone VARCHAR(20),
    id_role INT NOT NULL,
    "date_naissance" DATETIME2 NOT NULL,
    "date_suppression" DATETIME2
    CONSTRAINT fk_user_role FOREIGN KEY (id_role) REFERENCES roles(id)
);

CREATE TABLE categories (
    id INT PRIMARY KEY IDENTITY(1,1),
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE livres (
    id INT PRIMARY KEY IDENTITY(1,1),
    isbn VARCHAR(13) UNIQUE NOT NULL,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    resume TEXT,
    image_couverture VARCHAR(255),
    id_categorie INT NOT NULL,
    CONSTRAINT fk_livre_categorie FOREIGN KEY (id_categorie) REFERENCES categories(id)
);

CREATE TABLE exemplaires (
    id INT PRIMARY KEY IDENTITY(1,1),
    code_barre VARCHAR(50) UNIQUE NOT NULL,
    etat VARCHAR(50),
    est_disponible BIT DEFAULT 1, -- Utilisé pour US-BOOK-01
    id_livre INT NOT NULL,
    CONSTRAINT fk_exemplaire_livre FOREIGN KEY (id_livre) REFERENCES livres(id)
);

CREATE TABLE emprunts (
    id INT PRIMARY KEY IDENTITY(1,1),
    date_debut DATETIME DEFAULT GETDATE(),
    date_retour_prevue DATETIME NOT NULL, -- Calculée à J+14
    date_retour_effective DATETIME NULL,
    statut VARCHAR(20) DEFAULT 'EN_COURS', -- EN_COURS, RENDU, RETARD
    id_utilisateur INT NOT NULL,
    id_exemplaire INT NOT NULL,
    CONSTRAINT fk_emprunt_user FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id),
    CONSTRAINT fk_emprunt_exem FOREIGN KEY (id_exemplaire) REFERENCES exemplaires(id)
);

CREATE TABLE reservations (
    id INT PRIMARY KEY IDENTITY(1,1),
    date_reservation DATETIME DEFAULT GETDATE(),
    rang_file INT NOT NULL, -- US-RESA-01
    statut VARCHAR(20) DEFAULT 'ATTENTE',
    id_utilisateur INT NOT NULL,
    id_livre INT NOT NULL,
    CONSTRAINT fk_resa_user FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id),
    CONSTRAINT fk_resa_livre FOREIGN KEY (id_livre) REFERENCES livres(id)
);

CREATE TABLE evaluations (
    id INT PRIMARY KEY IDENTITY(1,1),
    note INT CHECK (note BETWEEN 1 AND 5), -- US-RATE-01
    commentaire VARCHAR(1000), -- Limite 1000 car.
    est_modere BIT DEFAULT 0,
    date_publication DATETIME DEFAULT GETDATE(),
    id_utilisateur INT NOT NULL,
    id_livre INT NOT NULL,
    CONSTRAINT fk_eval_user FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id),
    CONSTRAINT fk_eval_livre FOREIGN KEY (id_livre) REFERENCES livres(id)
);