-- 1. GÉNÉRATION DES AUTEURS (6 auteurs)
INSERT INTO auteurs (nom, prenom)
VALUES ('Hugo', 'Victor'),
       ('Zola', 'Émile'),
       ('Orwell', 'George'),
       ('Asimov', 'Isaac'),
       ('Christie', 'Agatha'),
       ('Rowling', 'J.K.');

-- 2. GÉNÉRATION DES CATÉGORIES (5 catégories)
INSERT INTO categories (libelle)
VALUES ('Roman Classique'),
       ('Science-Fiction'),
       ('Policier'),
       ('Fantasy'),
       ('Essai');

-- 3. GÉNÉRATION DES LIVRES (30 livres)
-- On alterne les auteurs (IDs 1 à 6) et les catégories (IDs 1 à 5)

INSERT INTO livres (isbn, titre, resume, image_couverture, id_auteur, id_categorie)
VALUES ('97801', 'Les Misérables', 'La vie de Jean Valjean.', 'hugo_mis.jpg', 1, 1),
       ('97802', 'Notre-Dame de Paris', 'L histoire de Quasimodo.', 'hugo_ndp.jpg', 1, 1),
       ('97803', 'Germinal', 'La grève des mineurs.', 'zola_ger.jpg', 2, 1),
       ('97804', 'L Assommoir', 'La chute de Gervaise.', 'zola_ass.jpg', 2, 1),
       ('97805', '1984', 'Big Brother vous regarde.', 'orwell_1984.jpg', 3, 2),
       ('97806', 'La Ferme des animaux', 'Une satire politique.', 'orwell_ferme.jpg', 3, 5),
       ('97807', 'Fondation', 'La psychohistoire de Seldon.', 'asimov_fond.jpg', 4, 2),
       ('97808', 'Les Robots', 'Les trois lois de la robotique.', 'asimov_rob.jpg', 4, 2),
       ('97809', 'Le Crime de l Orient-Express', 'Hercule Poirot enquête.', 'christie_orient.jpg', 5, 3),
       ('97810', 'Ils étaient dix', 'Dix personnes sur une île.', 'christie_dix.jpg', 5, 3),
       ('97811', 'Harry Potter à l école des sorciers', 'Le début de la saga.', 'jk_hp1.jpg', 6, 4),
       ('97812', 'Harry Potter et la Chambre des secrets', 'Le tome 2.', 'jk_hp2.jpg', 6, 4),
       ('97813', 'Le Dernier Jour d un condamné', 'Réflexion sur la peine de mort.', 'hugo_cond.jpg', 1, 5),
       ('97814', 'Nana', 'L ascension d une courtisane.', 'zola_nana.jpg', 2, 1),
       ('97815', 'Hommage à la Catalogne', 'Récit de guerre.', 'orwell_cat.jpg', 3, 5),
       ('97816', 'La Fin de l éternité', 'Voyage dans le temps.', 'asimov_fin.jpg', 4, 2),
       ('97817', 'Mort sur le Nil', 'Une nouvelle enquête.', 'christie_nil.jpg', 5, 3),
       ('97818', 'Harry Potter et le Prisonnier d Azkaban', 'Le tome 3.', 'jk_hp3.jpg', 6, 4),
       ('97819', 'Les Travailleurs de la mer', 'Lutte contre les éléments.', 'hugo_mer.jpg', 1, 1),
       ('97820', 'Au Bonheur des Dames', 'Les débuts des grands magasins.', 'zola_dames.jpg', 2, 1),
       ('97821', 'Dans la dèche à Paris et à Londres', 'Récit de pauvreté.', 'orwell_deche.jpg', 3, 5),
       ('97822', 'Cavernes d acier', 'Enquête futuriste.', 'asimov_cav.jpg', 4, 2),
       ('97823', 'ABC contre Poirot', 'Un tueur en série.', 'christie_abc.jpg', 5, 3),
       ('97824', 'Harry Potter et la Coupe de feu', 'Le tournoi des trois sorciers.', 'jk_hp4.jpg', 6, 4),
       ('97825', 'Claude Gueux', 'Critique sociale.', 'hugo_claude.jpg', 1, 1),
       ('97826', 'La Bête humaine', 'Drame ferroviaire.', 'zola_bete.jpg', 2, 1),
       ('97827', 'Face aux feux du soleil', 'Suite des robots.', 'asimov_soleil.jpg', 4, 2),
       ('97828', 'La Maison du péril', 'Enquête de Poirot.', 'christie_peril.jpg', 5, 3),
       ('97829', 'Une place à prendre', 'Roman social.', 'jk_place.jpg', 6, 1),
       ('97830', 'Le Rhin', 'Récit de voyage.', 'hugo_rhin.jpg', 1, 5);