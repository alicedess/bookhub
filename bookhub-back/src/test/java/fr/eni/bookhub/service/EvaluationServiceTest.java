package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.entity.Evaluation;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AllArgsConstructor
public class EvaluationServiceTest {

    @Autowired
    private final EvaluationService evaluationService;

    @Test
    @DisplayName("Test modification d'une évaluation")
    public void test_updateEvaluation()
    {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setNote(4);
        dto.setCommentaire("Trop bien le test");

        Boolean result = evaluationService.updateEvaluation(4, dto) != null;
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Test modération d'une évaluation")
    public void test_modererEvaluation()
    {
        // Appeler la méthode de modération
        evaluationService.modererEvaluation(4);

        // Vérifier que l'évaluation est maintenant modérée
        Evaluation evaluationApresMod = evaluationService.getEvaluation(4);
        Assertions.assertNotNull(evaluationApresMod);
        Assertions.assertTrue(evaluationApresMod.getEstModere());
    }

    @Test
    @DisplayName("Test suppression d'une évaluation")
    public void test_deleteEvaluation()
    {
        // Le test suppose que l'évaluation avec l'ID 4 existe
        evaluationService.deleteEvaluation(4);

        // Vérifier que l'évaluation a été supprimée
        Evaluation resultAfterDelete = evaluationService.getEvaluation(4); // À adapter selon votre API
        Assertions.assertNull(resultAfterDelete);
    }
}
