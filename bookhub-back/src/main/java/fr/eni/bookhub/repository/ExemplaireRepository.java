package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
	Optional<Exemplaire> findFirstByLivreIdAndEstDisponibleTrue(Long livreId);
}
