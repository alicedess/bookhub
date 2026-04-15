-- 1. GÉNÉRATION DES AUTEURS (6 auteurs)
INSERT INTO auteur (nom, prenom)
VALUES ('Hugo', 'Victor'),
       ('Zola', 'Émile'),
       ('Orwell', 'George'),
       ('Asimov', 'Isaac'),
       ('Christie', 'Agatha'),
       ('Rowling', 'J.K.');

-- 2. GÉNÉRATION DES CATÉGORIES (5 catégories)
INSERT INTO categorie (libelle)
VALUES ('Roman Classique'),
       ('Science-Fiction'),
       ('Policier'),
       ('Fantasy'),
       ('Essai');

-- 3. GÉNÉRATION DES LIVRES (30 livres)
-- On alterne les auteurs (IDs 1 à 6) et les catégories (IDs 1 à 5)

INSERT INTO livre (isbn, titre, resume, image_couverture, id_auteur, id_categorie, nb_page, date_parution)
VALUES
    -- Victor Hugo (Auteur ID: 1)
    ('9782070409228', 'Les Misérables', 'La vie de Jean Valjean.', 'hugo_mis.jpg', 1, 1, 1500, '1862-01-01'),
    ('9782070409174', 'Notre-Dame de Paris', 'L histoire de Quasimodo.', 'hugo_ndp.jpg', 1, 1, 640, '1831-01-14'),
    ('9782253006244', 'Le Dernier Jour d un condamné', 'Réflexion sur la peine de mort.', 'hugo_cond.jpg', 1, 5, 120, '1829-02-01'),
    ('9782253004325', 'Les Travailleurs de la mer', 'Lutte contre les éléments.', 'hugo_mer.jpg', 1, 1, 480, '1866-03-12'),
    ('9782253009771', 'Claude Gueux', 'Critique sociale.', 'hugo_claude.jpg', 1, 1, 80, '1834-07-01'),
    ('9782070322251', 'Le Rhin', 'Récit de voyage.', 'hugo_rhin.jpg', 1, 5, 520, '1842-01-20'),

    -- Émile Zola (Auteur ID: 2)
    ('9782253002840', 'Germinal', 'La grève des mineurs.', 'zola_ger.jpg', 2, 1, 550, '1885-02-01'),
    ('9782253005421', 'L Assommoir', 'La chute de Gervaise.', 'zola_ass.jpg', 2, 1, 440, '1877-01-01'),
    ('9782253003052', 'Nana', 'L ascension d une courtisane.', 'zola_nana.jpg', 2, 1, 480, '1880-02-15'),
    ('9782253005438', 'Au Bonheur des Dames', 'Les débuts des grands magasins.', 'zola_dames.jpg', 2, 1, 520, '1883-03-01'),
    ('9782253005445', 'La Bête humaine', 'Drame ferroviaire.', 'zola_bete.jpg', 2, 1, 450, '1890-01-01'),

    -- George Orwell (Auteur ID: 3)
    ('9782070409242', '1984', 'Big Brother vous regarde.', 'orwell_1984.jpg', 3, 2, 328, '1949-06-08'),
    ('9782070375165', 'La Ferme des animaux', 'Une satire politique.', 'orwell_ferme.jpg', 3, 5, 144, '1945-08-17'),
    ('9782264030382', 'Hommage à la Catalogne', 'Récit de guerre.', 'orwell_cat.jpg', 3, 5, 320, '1938-04-25'),
    ('9782264030375', 'Dans la dèche à Paris et à Londres', 'Récit de pauvreté.', 'orwell_deche.jpg', 3, 5, 280, '1933-01-09'),

    -- Isaac Asimov (Auteur ID: 4)
    ('9782207249123', 'Fondation', 'La psychohistoire de Seldon.', 'asimov_fond.jpg', 4, 2, 255, '1951-01-01'),
    ('9782290342480', 'Les Robots', 'Les trois lois de la robotique.', 'asimov_rob.jpg', 4, 2, 320, '1950-12-02'),
    ('9782207249130', 'La Fin de l éternité', 'Voyage dans le temps.', 'asimov_fin.jpg', 4, 2, 250, '1955-01-01'),
    ('9782207249147', 'Cavernes d acier', 'Enquête futuriste.', 'asimov_cav.jpg', 4, 2, 280, '1954-01-01'),
    ('9782207249154', 'Face aux feux du soleil', 'Suite des robots.', 'asimov_soleil.jpg', 4, 2, 300, '1957-01-01'),

    -- Agatha Christie (Auteur ID: 5)
    ('9782253004110', 'Le Crime de l Orient-Express', 'Hercule Poirot enquête.', 'christie_orient.jpg', 5, 3, 250, '1934-01-01'),
    ('9782253004127', 'Ils étaient dix', 'Dix personnes sur une île.', 'christie_dix.jpg', 5, 3, 220, '1939-11-06'),
    ('9782253004134', 'Mort sur le Nil', 'Une nouvelle enquête.', 'christie_nil.jpg', 5, 3, 320, '1937-11-01'),
    ('9782253004141', 'ABC contre Poirot', 'Un tueur en série.', 'christie_abc.jpg', 5, 3, 240, '1936-01-06'),
    ('9782253004158', 'La Maison du péril', 'Enquête de Poirot.', 'christie_peril.jpg', 5, 3, 260, '1932-03-01'),

    -- J.K. Rowling (Auteur ID: 6)
    ('9782070643028', 'Harry Potter à l école des sorciers', 'Le début de la saga.', 'jk_hp1.jpg', 6, 4, 305, '1997-06-26'),
    ('9782070643035', 'Harry Potter et la Chambre des secrets', 'Le tome 2.', 'jk_hp2.jpg', 6, 4, 340, '1998-07-02'),
    ('9782070643042', 'Harry Potter et le Prisonnier d Azkaban', 'Le tome 3.', 'jk_hp3.jpg', 6, 4, 435, '1999-07-08'),
    ('9782070643059', 'Harry Potter et la Coupe de feu', 'Le tournoi des trois sorciers.', 'jk_hp4.jpg', 6, 4, 640, '2000-07-08'),
    ('9782246779414', 'Une place à prendre', 'Roman social.', 'jk_place.jpg', 6, 1, 608, '2012-09-27');



INSERT INTO role (libelle) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_LIBRARIAN'),
    ('ROLE_USER');

-- Hash BCrypt pour "mon_super_password" : $2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i

INSERT INTO utilisateur
(pseudo, email, password, nom, prenom, date_naissance, telephone, id_role, date_suppression, commentaire_avec_pseudo)
VALUES
    ('admin_hub', 'admin@bookhub.fr', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Admin', 'Système', '1990-01-01', '0102030405', 1, NULL, 1),
    ('librarian_hub', 'librarian@bookhub.fr', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Dupont', 'Jean', '1985-05-12', '0612345678', 2, NULL, 1),
    ('msmith', 'marie.smith@gmail.com', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Smith', 'Marie', '1992-11-20', '0623456789', 2, NULL, 1),
    ('lartigaud', 'lucas.a@outlook.fr', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Artigaud', 'Lucas', '1998-03-15', '0634567890', 2, NULL, 0),
    ('bibliophile', 'sophie.biblio@yahoo.fr', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Morel', 'Sophie', '1978-07-22', '0645678901', 2, NULL, 1),
    ('thomas_eni', 'thomas.c@eni.fr', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Ciles', 'Thomas', '1995-12-30', '0656789012', 1, NULL, 1),
    ('book_lover', 'emma.l@test.com', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Lefebvre', 'Emma', '2001-09-05', '0667890123', 2, NULL, 1),
    ('v_hugo_fan', 'victor.fan@culture.fr', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Durand', 'Paul', '1988-04-18', '0678901234', 2, NULL, 0),
    ('julie_dev', 'julie.dev@code.com', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Gauthier', 'Julie', '1993-02-28', '0689012345', 2, NULL, 1),
    ('reader99', 'marc.reader@mail.fr', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Bernard', 'Marc', '1982-10-10', '0690123456', 2, NULL, 1);


-- 4. GÉNÉRATION DES EXEMPLAIRES (2 à 3 par livre)
-- On boucle sur les 30 livres (IDs 1 à 30)
INSERT INTO exemplaire (code_barre, etat, est_disponible, id_livre)
VALUES
    -- Exemplaires pour Livre 1
    ('CB-97801-1', 'NEUF', 1, 1),
    ('CB-97801-2', 'BON', 0, 1),
    -- Exemplaires pour Livre 2
    ('CB-97802-1', 'NEUF', 1, 2),
    ('CB-97802-2', 'ABIME', 1, 2),
    -- Exemplaires pour Livre 3
    ('CB-97803-1', 'BON', 1, 3),
    -- Exemplaires pour Livre 4
    ('CB-97804-1', 'NEUF', 0, 4),
    ('CB-97804-2', 'NEUF', 1, 4),
    -- Exemplaires pour Livre 5 (1984)
    ('CB-97805-1', 'BON', 1, 5),
    ('CB-97805-2', 'BON', 1, 5),
    ('CB-97805-3', 'ABIME', 0, 5),
    -- ... On continue pour quelques autres livres significatifs
    ('CB-97807-1', 'NEUF', 1, 7),
    ('CB-97808-1', 'BON', 1, 8),
    ('CB-97809-1', 'NEUF', 1, 9),
    ('CB-97810-1', 'ABIME', 1, 10),
    ('CB-97811-1', 'NEUF', 1, 11),
    ('CB-97811-2', 'NEUF', 0, 11),
    ('CB-97812-1', 'BON', 1, 12),
    ('CB-97824-1', 'NEUF', 1, 24),
    ('CB-97830-1', 'BON', 1, 30);

-- 5. GÉNÉRATION DES ÉVALUATIONS (Commentaires et notes)
-- Utilisateurs IDs 1 à 10, Livres IDs 1 à 30
INSERT INTO evaluation (note, commentaire, date_publication, id_utilisateur, id_livre)
VALUES
    -- Évaluations pour Les Misérables (Livre 1)
    (5, 'Un chef-d oeuvre absolu du patrimoine français.', '2024-01-15', 2, 1),
    (4, 'Un peu long par moments, mais quelle histoire !', '2024-02-10', 3, 1),

    -- Évaluations pour 1984 (Livre 5)
    (5, 'Effrayant de réalisme, même encore aujourd hui.', '2024-03-05', 4, 5),
    (5, 'À lire absolument une fois dans sa vie.', '2024-03-12', 7, 5),

    -- Évaluations pour Fondation (Livre 7)
    (4, 'La science-fiction à son apogée.', '2024-01-20', 9, 7),

    -- Évaluations pour Harry Potter (Livre 11)
    (5, 'Toute mon enfance, je ne m en lasse pas.', '2024-04-01', 10, 11),
    (3, 'Sympa mais un peu jeunesse pour moi.', '2024-04-05', 5, 11),

    -- Évaluations pour Le Crime de l Orient-Express (Livre 9)
    (5, 'La fin est tout simplement géniale, je ne m y attendais pas !', '2024-02-28', 8, 9),

    -- Évaluations pour Germinal (Livre 3)
    (4, 'Zola nous plonge dans l enfer des mines avec brio.', '2024-03-20', 2, 3),

    -- Évaluation diverse
    (2, 'Pas du tout accroché au style de l auteur.', '2024-03-25', 6, 13),
    (4, 'Une belle découverte.', '2024-04-10', 7, 20);


INSERT INTO emprunt (date_debut, date_retour_prevue, date_retour_effective, statut, id_utilisateur, id_exemplaire) VALUES ( N'2026-04-15 22:54:33.7370890', N'2026-04-29 22:54:33.7370890', null, N'EN_COURS', 2, 17);
INSERT INTO emprunt (date_debut, date_retour_prevue, date_retour_effective, statut, id_utilisateur, id_exemplaire) VALUES ( N'2025-04-15 23:32:26.9633565', N'2025-04-29 23:32:26.9633565', null, N'EN_COURS', 2, 14);
INSERT INTO emprunt (date_debut, date_retour_prevue, date_retour_effective, statut, id_utilisateur, id_exemplaire) VALUES ( N'2026-04-15 23:32:38.8619303', N'2026-04-29 23:32:38.8619303', null, N'EN_COURS', 2, 11);
