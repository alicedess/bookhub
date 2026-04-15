package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.entity.Evaluation;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.exception.OperationException;
import fr.eni.bookhub.mapper.EvaluationMapper;
import fr.eni.bookhub.repository.EvaluationRepository;
import fr.eni.bookhub.repository.LivreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EvaluationService {

    private EvaluationMapper evaluationMapper;
    private EvaluationRepository evaluationRepository;
    private LivreRepository livreRepository;

    public List<EvaluationDTO> getEvaluationsParLivre(Long id){
        try{
            // Récupérez le Livre directement depuis le repository
            Livre livre = livreRepository.findById(id)
                    .orElseThrow(() -> new OperationException("Livre non trouvé"));

            List<Evaluation> lesevals = evaluationRepository.findByLivre(livre);

            return lesevals.stream()
                    .map(evaluationMapper::toEvaluationDTO)
                    .collect(Collectors.toList());
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public EvaluationDTO createEvaluation(Integer id, EvaluationDTO evalDTO){
        try{
            Evaluation evaluation = evaluationMapper.toEntity(evalDTO);
            evaluationRepository.save(evaluation);
            return evaluationMapper.toEvaluationDTO(evaluation);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Mettre à jour une évaluation
     * @param id
     * @param evaluationDTO
     * @return
     */
    public EvaluationDTO updateEvaluation(Integer id, EvaluationDTO evaluationDTO) {
        try{
            Evaluation evaluation = evaluationRepository.findById(id);
            if (evaluation == null) {
                throw new OperationException("Evaluation non trouvée");
            }
            evaluation.setNote(evaluationDTO.getNote());
            evaluation.setCommentaire(evaluationDTO.getCommentaire());
            evaluationRepository.save(evaluation);
            return evaluationMapper.toEvaluationDTO(evaluation);
        } catch (OperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cache une evaluation (soft delete)
     * @param id
     */
    public void modererEvaluation(Integer id){
        try {
            Evaluation evaluation = evaluationRepository.findById(id);
            if (evaluation == null) {
                throw new OperationException("Evaluation non trouvée");
            }
            evaluation.setEstModere(true);
            evaluationRepository.save(evaluation);
        } catch (OperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Supprimer une evaluation (hard delete)
     * @param id
     */
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
