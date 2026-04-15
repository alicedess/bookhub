package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Exemplaire;
import fr.eni.bookhub.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
	Optional<Exemplaire> findFirstByLivreIdAndEstDisponibleTrue(Long livreId);

    List<Exemplaire> findByLivre(Livre livre);
}
