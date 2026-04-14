package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.enumeration.StatutEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    List<Emprunt> findByUtilisateurAndStatut(Utilisateur utilisateur, StatutEnum statut);

    boolean existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
            Utilisateur utilisateur, StatutEnum statut, LocalDateTime date);
}
