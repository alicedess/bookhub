package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.enumeration.StatutEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    //Emprunts en cours
    List<Emprunt> findByUtilisateurAndStatut(Utilisateur utilisateur, StatutEnum statut);

    long countByUtilisateurAndStatut(Utilisateur utilisateur, StatutEnum statut);

    boolean existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
            Utilisateur utilisateur, StatutEnum statut, LocalDateTime date);

    //Emprunts historiques (statut = RETOURNE ou RETARDE)
    List<Emprunt> findByUtilisateurAndStatutIn(Utilisateur utilisateur, List<StatutEnum> status);

    List<Emprunt> findByUtilisateurAndStatutAndDateRetourPrevueBefore(Utilisateur utilisateur, StatutEnum statutEnum, LocalDateTime now);


    long countByStatut(StatutEnum statut);

    long countByStatutAndDateRetourPrevueBefore(StatutEnum statut, LocalDateTime date);

    /**
     * Retourne si des emprunts sont en cours sur le livre.
     */
    @Query(value = """
SELECT COUNT(e) > 0
FROM Emprunt e 
JOIN Exemplaire ex ON ex = e.exemplaire
JOIN Livre l ON ex.livre = l
WHERE l = :livre AND e.statut = :statut
""")
    boolean existsByLivre(
            @Param("livre") Livre livre,
            @Param("statut") StatutEnum statut
    );

    /**
     * Emprunts filtrés par statuts, paginés et triés via Pageable.
     */
    Page<Emprunt> findByStatutIn(List<StatutEnum> statuts, Pageable pageable);
}
