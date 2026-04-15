package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Role;
import fr.eni.bookhub.enumeration.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByLibelle(RoleEnum libelle);
}
