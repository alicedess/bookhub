package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.entity.Evaluation;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.OperationException;
import fr.eni.bookhub.mapper.EvaluationMapper;
import fr.eni.bookhub.repository.EvaluationRepository;
import fr.eni.bookhub.repository.LivreRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    /**
     * Ajout evaluation
     * @param id Id du livre dont on ajoute le commentaire
     * @param evalDTO
     * @return
     */
    public EvaluationDTO createEvaluation(Long id, EvaluationDTO evalDTO) {
        // Validation des champs
        if (evalDTO.getNote() == null) {
            throw new OperationException("La note est obligatoire");
        }
        if (evalDTO.getNote() < 1 || evalDTO.getNote() > 5) {
            throw new OperationException("La note doit être comprise entre 1 et 5");
        }
        if (evalDTO.getCommentaire() != null && evalDTO.getCommentaire().length() > 1000) {
            throw new OperationException("Le commentaire ne peut pas dépasser 1000 caractères");
        }

        // Vérification de l'existence du livre
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new OperationException("Livre non trouvé"));

        // Récupération de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Utilisateur)) {
            throw new OperationException("Utilisateur non authentifié");
        }
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();

        // Création de l'évaluation
        Evaluation evaluation = evaluationMapper.toEntity(evalDTO);
        evaluation.setDatePublication(Instant.now());
        evaluation.setLivre(livre);
        evaluation.setUtilisateur(utilisateur);  // Ajout de l'utilisateur
        evaluation.setEstModere(false);  // Par défaut, non modérée

        evaluationRepository.save(evaluation);
        return evaluationMapper.toEvaluationDTO(evaluation);
    }

    /**
     * Mettre à jour une évaluation
     * @param id
     * @param evaluationDTO
     * @return
     */
    public EvaluationDTO updateEvaluation(Long id, EvaluationDTO evaluationDTO) {
        try{
            Evaluation evaluation = evaluationRepository.findById(id)
                    .orElseThrow(() -> new OperationException("Evaluation non trouvée"));
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
    public void modererEvaluation(Long id){
        try {
            Evaluation evaluation = evaluationRepository.findById(id)
                    .orElseThrow(() -> new OperationException("Evaluation non trouvée"));
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
    public void deleteEvaluation(Long id){
        try{
            Evaluation evaluation = evaluationRepository.findById(id)
                    .orElseThrow(() -> new OperationException("Evaluation non trouvée"));
            evaluationRepository.delete(evaluation);
        } catch (OperationException e) {
            throw new RuntimeException(e);
        }
    }
}
