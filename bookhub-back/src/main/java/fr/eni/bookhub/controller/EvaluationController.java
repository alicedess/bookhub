package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.service.EvaluationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@AllArgsConstructor
public class EvaluationController {

    private EvaluationService evaluationService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvaluation(@PathVariable Integer id, @RequestBody EvaluationDTO evaluationDTO) {
        try{
            evaluationService.updateEvaluation(id, evaluationDTO);
            return ResponseEntity.ok().body("Evaluation mise à jour");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'update de l'évaluation");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvaluation(@PathVariable Integer id){
        try {
            evaluationService.deleteEvaluation(id);
            return ResponseEntity.ok().body("Evaluation supprimé");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression de l'évaluation");
        }
    }
}
