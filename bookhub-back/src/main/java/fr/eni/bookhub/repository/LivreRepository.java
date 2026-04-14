package fr.eni.bookhub.repository;

import fr.eni.bookhub.dto.LivreDTO;
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
SELECT new fr.eni.bookhub.dto.LivreDTO(
        l.id,
        l.isbn,
        l.titre,
        l.resume,
        l.imageCouverture,
        l.nbPage,
        l.dateParution,
        l.auteur.id,
        l.auteur.nom,
        l.auteur.prenom,
        l.categorie.id,
        l.categorie.libelle,
        COUNT(DISTINCT e.id),
        SUM(CASE WHEN e.estDisponible = true THEN 1L ELSE 0L END) OVER(PARTITION BY l.id),
        AVG(eval.note)
)
FROM Livre l 
LEFT JOIN Exemplaire e ON e.livre = l
LEFT JOIN Evaluation eval ON eval.livre = l
WHERE l.id = :id      
GROUP BY l.id, l.isbn, l.titre, l.resume, l.imageCouverture, l.nbPage, l.auteur.id, l.auteur.nom, l.auteur.prenom, 
         l.categorie.id, l.categorie.libelle, l.dateParution, e.estDisponible
""")
    Optional<LivreDTO> findByIdForDetails(@Param("id") Long id);

    /**
     * Récupère la liste des
     */
    @EntityGraph(attributePaths = {"auteur", "categorie"}) // On force le left join
    @Query("""
        SELECT new fr.eni.bookhub.dto.LivreDTO(
            l.id,
            l.isbn,
            l.titre,
            l.resume,
            l.imageCouverture,
            l.nbPage,
            l.dateParution,
            l.auteur.id,
            l.auteur.nom,
            l.auteur.prenom,
            l.categorie.id,
            l.categorie.libelle,
            COUNT(DISTINCT e.id),
            SUM(CASE WHEN e.estDisponible = true THEN 1L ELSE 0L END) OVER(PARTITION BY l.id),
            AVG(eval.note) 
        )
        FROM Livre l
        LEFT JOIN Exemplaire e ON e.livre = l
        LEFT JOIN Evaluation eval ON eval.livre = l
        WHERE (
              (:query IS NULL OR LOWER(l.titre) LIKE LOWER(CONCAT('%', :query, '%')))
              OR (:query IS NULL OR LOWER(l.auteur.prenom) LIKE LOWER(CONCAT('%', :query, '%')))
              OR (:query IS NULL OR LOWER(l.auteur.nom) LIKE LOWER(CONCAT('%', :query, '%')))
              OR (:query IS NULL OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', :query, '%')))
              )
          AND (:auteurId IS NULL OR l.auteur.id = :auteurId)
          AND (:catId IS NULL OR l.categorie.id = :catId)
       GROUP BY l.id, l.isbn, l.titre, l.resume, l.imageCouverture, l.nbPage, l.auteur.id, l.auteur.nom, l.auteur.prenom, 
         l.categorie.id, l.categorie.libelle, l.dateParution, e.estDisponible   
    """)
    Page<LivreDTO> findByCustomFilters(
            @Param("query") String queryFilter,
            @Param("auteurId") Long auteurFilter,
            @Param("catId") Long categorieFilter,
            Pageable pageable
    );

    Optional<Livre> findByIsbn(@NotBlank @Size(min = 3, max = 13) String isbn);

}
