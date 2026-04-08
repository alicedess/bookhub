package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Utilisateur findUtilisateurById(Integer id);
}
