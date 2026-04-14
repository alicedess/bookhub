package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.entity.Evaluation;
import fr.eni.bookhub.exception.OperationException;
import fr.eni.bookhub.repository.EvaluationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EvaluationService {

    private EvaluationRepository evaluationRepository;

    public EvaluationDTO updateEvaluation(Integer id, EvaluationDTO evaluationDTO) {
        try{
            Evaluation evaluation = evaluationRepository.findById(id);
            if (evaluation == null) {
                throw new OperationException("Evaluation non trouvée");
            }
            evaluation.setNote(evaluationDTO.getNote());
            evaluation.setCommentaire(evaluationDTO.getCommentaire());
            evaluationRepository.save(evaluation);
            return evaluationDTO;
        } catch (OperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteEvaluation(Integer id){
        try{
            Evaluation evaluation = evaluationRepository.findById(id);
            if (evaluation == null) {
                throw new OperationException("Evaluation non trouvée");
            }
            evaluationRepository.delete(evaluation);
        } catch (OperationException e) {
            throw new RuntimeException(e);
        }
    }
}
