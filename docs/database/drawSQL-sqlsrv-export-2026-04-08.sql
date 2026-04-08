CREATE TABLE "roles"(
    "id" INT NOT NULL,
    "libelle" VARCHAR(20) NOT NULL
);
ALTER TABLE
    "roles" ADD CONSTRAINT "roles_id_primary" PRIMARY KEY("id");
CREATE TABLE "utilisateurs"(
    "id" INT NOT NULL,
    "email" VARCHAR(100) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "nom" VARCHAR(50) NOT NULL,
    "prenom" VARCHAR(50) NOT NULL,
    "telephone" VARCHAR(20) NULL,
    "id_role" INT NOT NULL,
    "date_naissance" DATETIME2 NOT NULL,
    "date_suppression" DATETIME2 NOT NULL
);
ALTER TABLE
    "utilisateurs" ADD CONSTRAINT "utilisateurs_id_primary" PRIMARY KEY("id");
CREATE UNIQUE INDEX "utilisateurs_email_unique" ON
    "utilisateurs"("email");
CREATE TABLE "categories"(
    "id" INT NOT NULL,
    "libelle" VARCHAR(50) NOT NULL
);
ALTER TABLE
    "categories" ADD CONSTRAINT "categories_id_primary" PRIMARY KEY("id");
CREATE TABLE "livres"(
    "id" INT NOT NULL,
    "isbn" VARCHAR(13) NOT NULL,
    "titre" VARCHAR(255) NOT NULL,
    "auteur" VARCHAR(255) NOT NULL,
    "resume" VARCHAR(255) NULL,
    "image_couverture" VARCHAR(255) NULL,
    "id_categorie" INT NOT NULL
);
ALTER TABLE
    "livres" ADD CONSTRAINT "livres_id_primary" PRIMARY KEY("id");
CREATE UNIQUE INDEX "livres_isbn_unique" ON
    "livres"("isbn");
CREATE TABLE "exemplaires"(
    "id" INT NOT NULL,
    "code_barre" VARCHAR(50) NOT NULL,
    "etat" VARCHAR(50) NULL,
    "est_disponible" BIT NULL DEFAULT 1,
    "id_livre" INT NOT NULL
);
ALTER TABLE
    "exemplaires" ADD CONSTRAINT "exemplaires_id_primary" PRIMARY KEY("id");
CREATE UNIQUE INDEX "exemplaires_code_barre_unique" ON
    "exemplaires"("code_barre");
CREATE TABLE "emprunts"(
    "id" INT NOT NULL,
    "date_debut" DATETIME2 NULL DEFAULT GETDATE(), "date_retour_prevue" DATETIME2 NOT NULL, "date_retour_effective" DATETIME2 NULL, "statut" VARCHAR(20) NULL DEFAULT 'EN_COURS', "id_utilisateur" INT NOT NULL, "id_exemplaire" INT NOT NULL);
ALTER TABLE
    "emprunts" ADD CONSTRAINT "emprunts_id_primary" PRIMARY KEY("id");
CREATE TABLE "reservations"(
    "id" INT NOT NULL,
    "date_reservation" DATETIME2 NULL DEFAULT GETDATE(), "rang_file" INT NOT NULL, "statut" VARCHAR(20) NULL DEFAULT 'ATTENTE', "id_utilisateur" INT NOT NULL, "id_livre" INT NOT NULL);
ALTER TABLE
    "reservations" ADD CONSTRAINT "reservations_id_primary" PRIMARY KEY("id");
CREATE TABLE "evaluations"(
    "id" INT NOT NULL,
    "note" INT NULL,
    "commentaire" VARCHAR(1000) NULL,
    "est_modere" BIT NULL,
    "date_publication" DATETIME2 NULL DEFAULT GETDATE(), "id_utilisateur" INT NOT NULL, "id_livre" INT NOT NULL);
ALTER TABLE
    "evaluations" ADD CONSTRAINT "evaluations_id_primary" PRIMARY KEY("id");
ALTER TABLE
    "emprunts" ADD CONSTRAINT "emprunts_id_exemplaire_foreign" FOREIGN KEY("id_exemplaire") REFERENCES "exemplaires"("id");
ALTER TABLE
    "utilisateurs" ADD CONSTRAINT "utilisateurs_id_role_foreign" FOREIGN KEY("id_role") REFERENCES "roles"("id");
ALTER TABLE
    "reservations" ADD CONSTRAINT "reservations_id_livre_foreign" FOREIGN KEY("id_livre") REFERENCES "livres"("id");
ALTER TABLE
    "evaluations" ADD CONSTRAINT "evaluations_id_livre_foreign" FOREIGN KEY("id_livre") REFERENCES "livres"("id");
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