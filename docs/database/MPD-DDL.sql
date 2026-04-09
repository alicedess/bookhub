CREATE TABLE "roles"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "libelle" NVARCHAR(20) NOT NULL
);

CREATE TABLE "utilisateurs"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "pseudo" NVARCHAR(100) NOT NULL,
    "email" NVARCHAR(100) NOT NULL,
    "password" NVARCHAR(255) NOT NULL,
    "nom" NVARCHAR(50) NOT NULL,
    "prenom" NVARCHAR(50) NOT NULL,
    "date_naissance" DATETIME2 NOT NULL,
    "telephone" NVARCHAR(20) NULL,
    "id_role" INT NOT NULL,
    "date_suppression" DATETIME2 NOT NULL,
    "commentaire_avec_pseudo" BIT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX "utilisateurs_pseudo_unique" ON
    "utilisateurs"("pseudo");
CREATE UNIQUE INDEX "utilisateurs_email_unique" ON
    "utilisateurs"("email");
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'Voir dans le futur pour une table préférences', @level0type = N'SCHEMA', @level0name = N'dbo', @level1type = N'TABLE', @level1name = N'utilisateurs', @level2type = N'COLUMN', @level2name = N'commentaire_avec_pseudo';

CREATE TABLE "categories"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,,
    "libelle" NVARCHAR(50) NOT NULL
);

CREATE TABLE "livres"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "isbn" NVARCHAR(13) NOT NULL,
    "titre" NVARCHAR(255) NOT NULL,
    "id_auteur" INT NOT NULL,
    "resume" NVARCHAR(255) NULL,
    "image_couverture" NVARCHAR(255) NULL,
    "id_categorie" INT NOT NULL
);


CREATE UNIQUE INDEX "livres_isbn_unique" ON "livres"("isbn");

CREATE TABLE "exemplaires"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "code_barre" NVARCHAR(50) NOT NULL,
    "etat" NVARCHAR(50) NULL,
    "est_disponible" BIT NULL DEFAULT 1,
    "id_livre" INT NOT NULL
);

CREATE UNIQUE INDEX "exemplaires_code_barre_unique" ON "exemplaires"("code_barre");

CREATE TABLE "emprunts"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "date_debut" DATETIME2 NULL DEFAULT GETDATE(), "date_retour_prevue" DATETIME2 NOT NULL, "date_retour_effective" DATETIME2 NULL, "statut" NVARCHAR(20) NULL DEFAULT 'EN_COURS', "id_utilisateur" INT NOT NULL, "id_exemplaire" INT NOT NULL);

CREATE TABLE "reservations"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "date_reservation" DATETIME2 NULL DEFAULT GETDATE(), "rang_file" INT NOT NULL, "statut" NVARCHAR(20) NULL DEFAULT 'ATTENTE', "id_utilisateur" INT NOT NULL, "id_livre" INT NOT NULL);

CREATE TABLE "evaluations"(
    "id" INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    "note" INT NULL,
    "commentaire" NVARCHAR(1000) NULL,
    "est_modere" BIT NULL,
    "date_publication" DATETIME2 NULL DEFAULT GETDATE(), "id_utilisateur" INT NOT NULL, "id_livre" INT NOT NULL);

CREATE TABLE "auteurs"(
    "id" INT NOT NULL IDENTITY(1, 1) PRIMARY KEY,
    "nom" NVARCHAR(100) NOT NULL,
    "prenom" NVARCHAR(100) NOT NULL
);

ALTER TABLE
    "emprunts" ADD CONSTRAINT "emprunts_id_exemplaire_foreign" FOREIGN KEY("id_exemplaire") REFERENCES "exemplaires"("id");
ALTER TABLE
    "utilisateurs" ADD CONSTRAINT "utilisateurs_id_role_foreign" FOREIGN KEY("id_role") REFERENCES "roles"("id");
ALTER TABLE
    "reservations" ADD CONSTRAINT "reservations_id_livre_foreign" FOREIGN KEY("id_livre") REFERENCES "livres"("id");
ALTER TABLE
    "evaluations" ADD CONSTRAINT "evaluations_id_livre_foreign" FOREIGN KEY("id_livre") REFERENCES "livres"("id");
ALTER TABLE
    "livres" ADD CONSTRAINT "livres_id_auteur_foreign" FOREIGN KEY("id_auteur") REFERENCES "auteurs"("id");
ALTER TABLE
    "reservations" ADD CONSTRAINT "reservations_id_utilisateur_foreign" FOREIGN KEY("id_utilisateur") REFERENCES "utilisateurs"("id");
ALTER TABLE
    "evaluations" ADD CONSTRAINT "evaluations_id_utilisateur_foreign" FOREIGN KEY("id_utilisateur") REFERENCES "utilisateurs"("id");
ALTER TABLE
    "livres" ADD CONSTRAINT "livres_id_categorie_foreign" FOREIGN KEY("id_categorie") REFERENCES "categories"("id");
ALTER TABLE
    "emprunts" ADD CONSTRAINT "emprunts_id_utilisateur_foreign" FOREIGN KEY("id_utilisateur") REFERENCES "utilisateurs"("id");
ALTER TABLE
    "exemplaires" ADD CONSTRAINT "exemplaires_id_livre_foreign" FOREIGN KEY("id_livre") REFERENCES "livres"("id");