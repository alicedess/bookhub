package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Evaluation;
import fr.eni.bookhub.entity.Livre;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @EntityGraph(attributePaths = {"livre", "utilisateur"})
    Optional<Evaluation> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"livre", "utilisateur"})
    List<Evaluation> findByLivre(Livre livre);
}
