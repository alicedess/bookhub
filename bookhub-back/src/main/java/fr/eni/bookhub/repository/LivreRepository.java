package fr.eni.bookhub.repository;

import fr.eni.bookhub.dto.LivreProjection;
import fr.eni.bookhub.entity.Livre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {

    @Override
    @EntityGraph(attributePaths = {"auteur", "categorie"})
    Optional<Livre> findById(Long id);

    @Query("""
            SELECT 
                l.id                    AS id,
                l.isbn                  AS isbn,
                l.titre                 AS titre,
                l.resume                AS resume,
                l.imageCouverture       AS imageCouverture,
                l.nbPage                AS nbPage,
                l.dateParution          AS dateParution,
                l.auteur.id             AS auteurId,
                l.auteur.nom            AS auteurNom,
                l.auteur.prenom         AS auteurPrenom,
                l.categorie.id          AS categorieId,
                l.categorie.libelle     AS categorieLibelle,
                (SELECT COUNT(e.id) FROM Exemplaire e WHERE e.livre = l)
                                        AS nbExemplaires,
                (SELECT COUNT(e.id) FROM Exemplaire e WHERE e.livre = l AND e.estDisponible = true)
                                        AS nbExemplairesDisponibles,
                (SELECT AVG(ev.note) FROM Evaluation ev WHERE ev.livre = l)
                                        AS moyenneEvaluations
            FROM Livre l
            WHERE l.id = :id      
            GROUP BY l.id, l.isbn, l.titre, l.resume, l.imageCouverture, l.nbPage, l.auteur.id, l.auteur.nom, l.auteur.prenom, 
                     l.categorie.id, l.categorie.libelle, l.dateParution
            """)
    Optional<LivreProjection> findByIdForDetails(@Param("id") Long id);

    /**
     * Récupère la liste des
     */
    @Query(value = """
            SELECT 
            l.id                    AS id,
            l.isbn                  AS isbn,
            l.titre                 AS titre,
            l.resume                AS resume,
            l.imageCouverture       AS imageCouverture,
            l.nbPage                AS nbPage,
            l.dateParution          AS dateParution,
            l.auteur.id             AS auteurId,
            l.auteur.nom            AS auteurNom,
            l.auteur.prenom         AS auteurPrenom,
            l.categorie.id          AS categorieId,
            l.categorie.libelle     AS categorieLibelle,
            (SELECT COUNT(e.id) FROM Exemplaire e WHERE e.livre = l)
                                    AS nbExemplaires,
            (SELECT COUNT(e.id) FROM Exemplaire e WHERE e.livre = l AND e.estDisponible = true)
                                    AS nbExemplairesDisponibles,
            (SELECT AVG(ev.note) FROM Evaluation ev WHERE ev.livre = l)
                                    AS moyenneEvaluations
                FROM Livre l
                WHERE (
                      (:query IS NULL OR LOWER(l.titre) LIKE LOWER(CONCAT('%', :query, '%')))
                      OR (:query IS NULL OR LOWER(l.auteur.prenom) LIKE LOWER(CONCAT('%', :query, '%')))
                      OR (:query IS NULL OR LOWER(l.auteur.nom) LIKE LOWER(CONCAT('%', :query, '%')))
                      OR (:query IS NULL OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', :query, '%')))
                      )
                  AND (:auteurId IS NULL OR l.auteur.id = :auteurId)
                  AND (:catId IS NULL OR l.categorie.id = :catId)
               GROUP BY l.id, l.isbn, l.titre, l.resume, l.imageCouverture, l.nbPage, l.auteur.id, l.auteur.nom, l.auteur.prenom, 
                 l.categorie.id, l.categorie.libelle, l.dateParution   
            """, countQuery = """
                    SELECT COUNT(DISTINCT l.id)
                    FROM Livre l
                    WHERE (
                          (:query IS NULL OR LOWER(l.titre) LIKE LOWER(CONCAT('%', :query, '%')))
                          OR (:query IS NULL OR LOWER(l.auteur.prenom) LIKE LOWER(CONCAT('%', :query, '%')))
                          OR (:query IS NULL OR LOWER(l.auteur.nom) LIKE LOWER(CONCAT('%', :query, '%')))
                          OR (:query IS NULL OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', :query, '%')))
                          )
                      AND (:auteurId IS NULL OR l.auteur.id = :auteurId)
                      AND (:catId IS NULL OR l.categorie.id = :catId)
            """)
    Page<LivreProjection> findByCustomFilters(
            @Param("query") String queryFilter,
            @Param("auteurId") Long auteurFilter,
            @Param("catId") Long categorieFilter,
            Pageable pageable
    );

    Optional<Livre> findByIsbn(@NotBlank @Size(min = 3, max = 13) String isbn);

}
