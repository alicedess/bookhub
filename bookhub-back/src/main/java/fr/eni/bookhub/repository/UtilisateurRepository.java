package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Utilisateur;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    Utilisateur findUtilisateurById(Long id);

    @Query("SELECT u FROM Utilisateur u WHERE u.id = :id AND u.dateSuppression IS NULL")
    Utilisateur findUtilisateurByIdWhereDateSuppressionIsNull(Long id);

    @EntityGraph(attributePaths = {"role"})
    Optional<Utilisateur> findByEmailAndDateSuppressionIsNull(String email);
}
