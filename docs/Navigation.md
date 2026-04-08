# Navigation utilisateur

## Légende des rôles

| Rôle            | Accès |
|-----------------|---|
| Public (à voir) | Sans connexion |
| USER            | Lecteur connecté |
| LIBRARIAN       | Bibliothécaire connecté |
| ADMIN           | Administrateur connecté |

---

## Pages publiques

| Page | Route | Description |
|---|---|---|
| Accueil | `/` | Présentation de l'app, accès au catalogue, CTA connexion/inscription |
| Inscription | `/register` | Formulaire de création de compte (nom, prénom, email, mot de passe) |
| Connexion | `/login` | Authentification par email/mot de passe, génération du JWT |
| Catalogue | `/books` | Liste paginée des livres (20/page), barre de recherche, filtres catégorie/dispo |
| Détail livre | `/books/:id` | Fiche complète : infos, disponibilité, note moyenne, commentaires |

---

## Espace lecteur (USER)

| Page | Route | Description |
|---|---|---|
| Dashboard | `/dashboard` | Emprunts en cours, alertes retard, réservations actives, livres lus |
| Mes emprunts | `/loans/my` | Onglets "En cours" / "Historique", dates de retour, statuts |
| Mes réservations | `/reservations/my` | File d'attente, rang, statut (ATTENTE / DISPO / ANNULÉE), annulation |
| Mon profil | `/profile` | Modification infos perso, changement de mot de passe, suppression de compte (RGPD) |

---

## Espace bibliothécaire (LIBRARIAN)

> Hérite de toutes les pages du rôle USER.

| Page | Route | Description |
|---|---|---|
| Dashboard bibliothécaire | `/librarian` | Stats globales, retards en cours, top 10 livres |
| Gestion catalogue | `/librarian/books` | Liste des livres avec actions CRUD |
| Ajouter un livre | `/librarian/books/new` | Formulaire d'ajout (titre, auteur, ISBN, catégorie, exemplaires) |
| Modifier un livre | `/librarian/books/:id/edit` | Modification des informations d'un livre existant |
| Gestion des emprunts | `/librarian/loans` | Liste des emprunts actifs, enregistrement des retours, gestion des retards |
| Modération | `/librarian/reviews` | Consultation et suppression des commentaires inappropriés |

---

## Espace administrateur (ADMIN)

> Hérite de toutes les pages des rôles USER et LIBRARIAN.

| Page | Route | Description |
|---|---|---|
| Dashboard admin | `/admin` | Vue globale : toutes les statistiques bibliothécaire |
| Gestion utilisateurs | `/admin/users` | Liste des comptes, modification des rôles, désactivation |

---

## Règles de navigation

- Toute route protégée redirige vers `/login` si le JWT est absent ou expiré.
- Après connexion, redirection selon le rôle : USER → `/dashboard`, LIBRARIAN → `/librarian`, ADMIN → `/admin`.
- Les routes `/librarian/*` et `/admin/*` renvoient un `403 Forbidden` si le rôle est insuffisant.
- La suppression d'un livre est bloquée s'il possède des emprunts en cours (règle RG côté API).
- Un lecteur avec un retard en cours est bloqué à l'emprunt (RG-LOAN-03).