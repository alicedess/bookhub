package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByLibelle(String libelle);

}
