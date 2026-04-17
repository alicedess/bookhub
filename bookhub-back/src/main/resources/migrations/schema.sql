DROP TABLE IF EXISTS emprunt;
DROP TABLE IF EXISTS evaluation;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS exemplaire;
DROP TABLE IF EXISTS livre;
DROP TABLE IF EXISTS auteur;
DROP TABLE IF EXISTS categorie;
DROP TABLE IF EXISTS utilisateur;
DROP TABLE IF EXISTS role;


CREATE TABLE "role"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "libelle" NVARCHAR(20) NOT NULL
);

CREATE TABLE "utilisateur"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "pseudo" NVARCHAR(100) NOT NULL,
    "email" NVARCHAR(100) NOT NULL,
    "password" NVARCHAR(255) NOT NULL,
    "nom" NVARCHAR(50) NOT NULL,
    "prenom" NVARCHAR(50) NOT NULL,
    "date_naissance" DATETIME2 NOT NULL,
    "telephone" NVARCHAR(20) NULL,
    "id_role" INT NOT NULL,
    "date_suppression" DATETIME2 NULL,
    "commentaire_avec_pseudo" BIT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX "utilisateur_pseudo_unique" ON "utilisateur"("pseudo");
CREATE UNIQUE INDEX "utilisateur_email_unique" ON "utilisateur"("email");
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'Voir dans le futur pour une table préférences', @level0type = N'SCHEMA', @level0name = N'dbo', @level1type = N'TABLE', @level1name = N'utilisateur', @level2type = N'COLUMN', @level2name = N'commentaire_avec_pseudo';

CREATE TABLE "categorie"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "libelle" NVARCHAR(50) NOT NULL
);

CREATE TABLE "livre"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "isbn" NVARCHAR(13) NOT NULL,
    "titre" NVARCHAR(255) NOT NULL,
    "id_auteur" INT NOT NULL,
    "resume" NVARCHAR(255) NULL,
    "image_couverture" NVARCHAR(255) NULL,
    "id_categorie" INT NOT NULL
);


CREATE UNIQUE INDEX "livre_isbn_unique" ON "livre"("isbn");

CREATE TABLE "exemplaire"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "code_barre" NVARCHAR(50) NOT NULL,
    "etat" NVARCHAR(50) NULL,
    "est_disponible" BIT NULL DEFAULT 1,
    "id_livre" INT NOT NULL
);

CREATE UNIQUE INDEX "exemplaire_code_barre_unique" ON "exemplaire"("code_barre");

CREATE TABLE "emprunt"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "date_debut" DATETIME2 NOT NULL DEFAULT GETDATE(),
    "date_retour_prevue" DATETIME2 NOT NULL,
    "date_retour_effective" DATETIME2 NULL,
    "statut" NVARCHAR(20) NOT NULL DEFAULT 'EN_COURS',
    "id_utilisateur" INT NOT NULL,
    "id_exemplaire" INT NOT NULL);

CREATE TABLE "reservation"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "date_reservation" DATETIME2 NULL DEFAULT GETDATE(), "rang_file" INT NOT NULL, "statut" NVARCHAR(20) NULL DEFAULT 'ATTENTE', "id_utilisateur" INT NOT NULL, "id_livre" INT NOT NULL);

CREATE TABLE "evaluation"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "note" INT NULL,
    "commentaire" NVARCHAR(1000) NULL,
    "est_modere" BIT NULL,
    "date_publication" DATETIME2 NULL DEFAULT GETDATE(), "id_utilisateur" INT NOT NULL, "id_livre" INT NOT NULL);

CREATE TABLE "auteur"(
    "id" INT NOT NULL IDENTITY(1, 1) PRIMARY KEY,
    "nom" NVARCHAR(100) NOT NULL,
    "prenom" NVARCHAR(100) NOT NULL
);

ALTER TABLE
    "emprunt" ADD CONSTRAINT "emprunt_id_exemplaire_foreign" FOREIGN KEY("id_exemplaire") REFERENCES "exemplaire"("id");
ALTER TABLE
    "emprunt" ADD CONSTRAINT "emprunt_id_utilisateur_foreign" FOREIGN KEY("id_utilisateur") REFERENCES "utilisateur"("id");
ALTER TABLE
    "utilisateur" ADD CONSTRAINT "utilisateur_id_role_foreign" FOREIGN KEY("id_role") REFERENCES "role"("id");
ALTER TABLE
    "reservation" ADD CONSTRAINT "reservation_id_livre_foreign" FOREIGN KEY("id_livre") REFERENCES "livre"("id");
ALTER TABLE
    "evaluation" ADD CONSTRAINT "evaluation_id_livre_foreign" FOREIGN KEY("id_livre") REFERENCES "livre"("id");
ALTER TABLE
    "livre" ADD CONSTRAINT "livre_id_auteur_foreign" FOREIGN KEY("id_auteur") REFERENCES "auteur"("id");
ALTER TABLE
    "reservation" ADD CONSTRAINT "reservation_id_utilisateur_foreign" FOREIGN KEY("id_utilisateur") REFERENCES "utilisateur"("id");
ALTER TABLE
    "evaluation" ADD CONSTRAINT "evaluation_id_utilisateur_foreign" FOREIGN KEY("id_utilisateur") REFERENCES "utilisateur"("id");
ALTER TABLE
    "livre" ADD CONSTRAINT "livre_id_categorie_foreign" FOREIGN KEY("id_categorie") REFERENCES "categorie"("id");
ALTER TABLE
    "exemplaire" ADD CONSTRAINT "exemplaire_id_livre_foreign" FOREIGN KEY("id_livre") REFERENCES "livre"("id");

ALTER TABLE livre ADD nb_page INT;
ALTER TABLE livre ADD date_parution DATE;

CREATE INDEX idx_auteur_nom ON auteur(nom);
CREATE INDEX idx_auteur_prenom ON auteur(prenom);
CREATE INDEX idx_livre_titre ON livre(titre);
