package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.EvaluationDTO;
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

        Boolean result = evaluationService.updateEvaluation(4, dto).getEstModere();
        Assertions.assertTrue(result);
    }
}
