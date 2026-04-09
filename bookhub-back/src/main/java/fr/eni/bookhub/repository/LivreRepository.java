package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Livre;
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

    /**
     * Récupère la liste des
     */
    @EntityGraph(attributePaths = {"auteur", "categorie"}) // On force le left join
    @Query("""
        SELECT l
        FROM Livre l
        WHERE (:query IS NULL OR LOWER(l.titre) LIKE LOWER(CONCAT('%', :query, '%')))
          AND (:auteurId IS NULL OR l.auteur.id = :auteurId)
          AND (:catId IS NULL OR l.categorie.id = :catId)
    """)
    Page<Livre> findByCustomFilters(
            @Param("query") String queryFilter,
            @Param("auteurId") Long auteurFilter,
            @Param("catId") Long categorieFilter,
            Pageable pageable
    );
}
