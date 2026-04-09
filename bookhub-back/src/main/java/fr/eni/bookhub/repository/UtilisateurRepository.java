package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    Utilisateur findUtilisateurById(Long id);

    @Query("SELECT u FROM Utilisateur u WHERE u.id = :id AND u.dateSuppression IS NULL")
    Utilisateur findUtilisateurByIdWhereDateSuppressionIsNull(Long id);

    Utilisateur findByEmail(String email);
}
