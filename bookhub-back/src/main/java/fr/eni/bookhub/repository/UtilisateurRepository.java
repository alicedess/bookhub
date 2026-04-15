package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Utilisateur;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Utilisateur findUtilisateurById(Long id);

    @Query("SELECT u FROM Utilisateur u WHERE u.id = :id AND u.dateSuppression IS NULL")
    Utilisateur findUtilisateurByIdWhereDateSuppressionIsNull(Long id);

    @EntityGraph(attributePaths = {"role"})
    Optional<Utilisateur> findByEmailAndDateSuppressionIsNull(String email);

    Optional<Utilisateur> findByEmail(String email);

    Utilisateur findByEmail(String email, Limit limit);

    List<Utilisateur> email(String email);
}
