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
@AllArgsConstructor
public class EvaluationServiceTest {

    @Autowired
    private final EvaluationService evaluationService;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Test
    @DisplayName("Test création d'une évaluation")
    @WithMockUser(username = "test@example.com")  // Simule un utilisateur authentifié
    public void test_createEvaluation() {
        // Préparation des données de test (suppose qu'un livre avec ID 1 existe)
        EvaluationDTO dto = new EvaluationDTO();
        dto.setNote(4);
        dto.setCommentaire("Excellent livre !");

        // Création de l'évaluation
        EvaluationDTO result = evaluationService.createEvaluation(1L, dto);

        // Vérifications
        Assertions.assertNotNull(result);
        Assertions.assertEquals(4, result.getNote());
        Assertions.assertEquals("Excellent livre !", result.getCommentaire());
    }

    @Test
    @DisplayName("Test récupération des évaluations par livre")
    public void test_getEvaluationsParLivre() {
        // Suppose qu'un livre avec ID 1 existe et a des évaluations
        List<EvaluationDTO> evaluations = evaluationService.getEvaluationsParLivre(1L);

        // Vérifications (ajustez selon les données de test)
        Assertions.assertNotNull(evaluations);
        // Assertions.assertTrue(evaluations.size() > 0);  // Si des évaluations existent
    }

    @Test
    @DisplayName("Test modification d'une évaluation")
    public void test_updateEvaluation() {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setNote(4);
        dto.setCommentaire("Trop bien le test");

        EvaluationDTO result = evaluationService.updateEvaluation(4, dto);  // Changement à Long
        Assertions.assertNotNull(result);
        Assertions.assertEquals(4, result.getNote());
        Assertions.assertEquals("Trop bien le test", result.getCommentaire());
    }

    @Test
    @DisplayName("Test modération d'une évaluation")
    public void test_modererEvaluation() {
        // Appeler la méthode de modération
        evaluationService.modererEvaluation(4);  // Changement à Long

        // Vérifier que l'évaluation est maintenant modérée
        Evaluation evaluationApresMod = evaluationRepository.findById(4L).orElse(null);  // Utilisation du repository pour vérifier
        Assertions.assertNotNull(evaluationApresMod);
        Assertions.assertTrue(evaluationApresMod.getEstModere());
    }

    @Test
    @DisplayName("Test suppression d'une évaluation")
    public void test_deleteEvaluation() {
        // Le test suppose que l'évaluation avec l'ID 4 existe
        evaluationService.deleteEvaluation(4);  // Changement à Long

        // Vérifier que l'évaluation a été supprimée
        Evaluation resultAfterDelete = evaluationRepository.findById(4L).orElse(null);
        Assertions.assertNull(resultAfterDelete);
    }
}
