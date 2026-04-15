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
    //Emprunts en cours
    List<Emprunt> findByUtilisateurAndStatut(Utilisateur utilisateur, StatutEnum statut);

    long countByUtilisateurAndStatut(Utilisateur utilisateur, StatutEnum statut);

    boolean existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
            Utilisateur utilisateur, StatutEnum statut, LocalDateTime date);

    //Emprunts historiques (statut = TERMINE ou RETARDE)
    List<Emprunt> findByUtilisateurAndStatutIn(Utilisateur utilisateur, List<StatutEnum> status);

    List<Emprunt> findByUtilisateurAndStatutAndDateRetourPrevueBefore(Utilisateur utilisateur, StatutEnum statutEnum, LocalDateTime now);

    List<Emprunt> findByStatut(StatutEnum statut);
}
