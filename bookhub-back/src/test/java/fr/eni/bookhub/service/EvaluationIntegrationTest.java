package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.EvaluationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EvaluationIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @DisplayName("Test intégration complète : création, récupération, mise à jour et suppression d'une évaluation")
    @WithMockUser(username = "admin@bookhub.fr", roles = {"ADMIN"})
    @Sql(statements = {
            "INSERT INTO utilisateur (id, pseudo, email, password, nom, prenom, date_naissance, telephone, id_role, date_suppression, commentaire_avec_pseudo) " +
                    "VALUES (100, 'testuser', 'test@example.com', '$2a$10$.QzCBRCaqi7e.uxSmdvrf.RJf.f8OBXAL1rOa78t1d5t3iPVVhE5i', 'Test', 'User', '1990-01-01', '0123456789', 2, NULL, 1) " +
                    "ON DUPLICATE KEY UPDATE id=id",
            "INSERT INTO livre (id, isbn, titre, resume, image_couverture, id_auteur, id_categorie, nb_page, date_parution) " +
                    "VALUES (100, '1234567890123', 'Livre Test', 'Résumé test', NULL, 1, 1, 100, '2020-01-01') " +
                    "ON DUPLICATE KEY UPDATE id=id"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM evaluation WHERE id_livre = 100",
            "DELETE FROM livre WHERE id = 100",
            "DELETE FROM utilisateur WHERE id = 100"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testEvaluationFullFlow() {
        // 1. Créer une évaluation
        EvaluationDTO newEvaluation = new EvaluationDTO();
        newEvaluation.setNote(4);
        newEvaluation.setCommentaire("Excellent livre !");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EvaluationDTO> requestEntity = new HttpEntity<>(newEvaluation, headers);

        ResponseEntity<EvaluationDTO> createResponse = restTemplate.exchange(
                "/api/books/100/ratings",
                HttpMethod.POST,
                requestEntity,
                EvaluationDTO.class
        );

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        EvaluationDTO createdEvaluation = createResponse.getBody();
        assertNotNull(createdEvaluation);
        assertEquals(4, createdEvaluation.getNote());
        assertEquals("Excellent livre !", createdEvaluation.getCommentaire());
        Long evaluationId = newEvaluation.getId();
        assertNotNull(evaluationId);

        // 2. Récupérer les évaluations du livre
        ResponseEntity<List> getResponse = restTemplate.getForEntity(
                "/api/books/100/ratings",
                List.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        List<?> evaluations = getResponse.getBody();
        assertNotNull(evaluations);
        assertFalse(evaluations.isEmpty());
        // Vérifier que notre évaluation est présente
        boolean found = evaluations.stream()
                .anyMatch(e -> ((Map<String, Object>) e).get("id").equals(evaluationId));
        assertTrue(found);

        // 3. Mettre à jour l'évaluation
        EvaluationDTO updateEvaluation = new EvaluationDTO();
        updateEvaluation.setNote(5);
        updateEvaluation.setCommentaire("Super livre, je recommande !");

        HttpEntity<EvaluationDTO> updateRequest = new HttpEntity<>(updateEvaluation, headers);
        ResponseEntity<String> updateResponse = restTemplate.exchange(
                "/api/ratings/" + evaluationId,
                HttpMethod.PUT,
                updateRequest,
                String.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Evaluation mise à jour", updateResponse.getBody());

        // 4. Vérifier la mise à jour en récupérant à nouveau
        ResponseEntity<List> getAfterUpdateResponse = restTemplate.getForEntity(
                "/api/books/100/ratings",
                List.class
        );
        assertEquals(HttpStatus.OK, getAfterUpdateResponse.getStatusCode());
        List<?> evaluationsAfterUpdate = getAfterUpdateResponse.getBody();
        EvaluationDTO updatedEval = evaluationsAfterUpdate.stream()
                .filter(e -> ((Map<String, Object>) e).get("id").equals(evaluationId))
                .map(e -> {
                    EvaluationDTO dto = new EvaluationDTO();
                    dto.setId(((Number) ((Map<String, Object>) e).get("id")).longValue());
                    dto.setNote(((Number) ((Map<String, Object>) e).get("note")).intValue());
                    dto.setCommentaire((String) ((Map<String, Object>) e).get("commentaire"));
                    return dto;
                })
                .findFirst()
                .orElse(null);
        assertNotNull(updatedEval);
        assertEquals(5, updatedEval.getNote());
        assertEquals("Super livre, je recommande !", updatedEval.getCommentaire());

        // 5. Supprimer l'évaluation
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/api/ratings/" + evaluationId,
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals("Evaluation supprimé", deleteResponse.getBody());

        // 6. Vérifier que l'évaluation a été supprimée
        ResponseEntity<List> getAfterDeleteResponse = restTemplate.getForEntity(
                "/api/books/100/ratings",
                List.class
        );
        assertEquals(HttpStatus.OK, getAfterDeleteResponse.getStatusCode());
        List<?> evaluationsAfterDelete = getAfterDeleteResponse.getBody();
        boolean stillFound = evaluationsAfterDelete.stream()
                .anyMatch(e -> ((Map<String, Object>) e).get("id").equals(evaluationId));
        assertFalse(stillFound);
    }

    @Test
    @DisplayName("Test création d'évaluation avec données invalides")
    @WithMockUser(username = "admin@bookhub.fr", roles = {"ADMIN"})
    public void testCreateEvaluationInvalidData() {
        // Test avec note invalide
        EvaluationDTO invalidEvaluation = new EvaluationDTO();
        invalidEvaluation.setNote(6); // Note > 5
        invalidEvaluation.setCommentaire("Test");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EvaluationDTO> requestEntity = new HttpEntity<>(invalidEvaluation, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "/api/books/1/ratings", // Livre existant
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(false, body.get("success"));
    }

    @Test
    @DisplayName("Test récupération d'évaluations pour un livre inexistant")
    public void testGetEvaluationsForNonExistentBook() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "/api/books/99999/ratings",
                Map.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(false, body.get("success"));
    }
}
