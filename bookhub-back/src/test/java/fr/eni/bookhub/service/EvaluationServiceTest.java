package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.entity.Evaluation;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.EvaluationRepository;
import fr.eni.bookhub.repository.LivreRepository;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
public class EvaluationServiceTest {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Test
    @DisplayName("Test création d'une évaluation")
    @WithMockUser(username = "test@example.com")
    public void test_createEvaluation() {

        EvaluationDTO dto = new EvaluationDTO();
        dto.setNote(4);
        dto.setCommentaire("Excellent livre !");

        EvaluationDTO result = evaluationService.createEvaluation(1L, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(4, result.getNote());
        Assertions.assertEquals("Excellent livre !", result.getCommentaire());
    }

    @Test
    @DisplayName("Test récupération des évaluations par livre")
    public void test_getEvaluationsParLivre() {
        List<EvaluationDTO> evaluations = evaluationService.getEvaluationsParLivre(1L);

        Assertions.assertNotNull(evaluations);
        Assertions.assertTrue(!evaluations.isEmpty());
    }

    @Test
    @DisplayName("Test modification d'une évaluation")
    public void test_updateEvaluation() {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setNote(4);
        dto.setCommentaire("Trop bien le test");

        EvaluationDTO result = evaluationService.updateEvaluation(4L, dto);  // Changement à Long
        Assertions.assertNotNull(result);
        Assertions.assertEquals(4, result.getNote());
        Assertions.assertEquals("Trop bien le test", result.getCommentaire());
    }

    @Test
    @DisplayName("Test modération d'une évaluation")
    public void test_modererEvaluation() {
        // Appeler la méthode de modération
        evaluationService.modererEvaluation(4L);  // Changement à Long

        // Vérifier que l'évaluation est maintenant modérée
        Evaluation evaluationApresMod = evaluationRepository.findById(4L)
                .orElseThrow(RuntimeException::new);
        Assertions.assertNotNull(evaluationApresMod);
        Assertions.assertTrue(evaluationApresMod.getEstModere());
    }
}
