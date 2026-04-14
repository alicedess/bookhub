package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.CreateLivreDTO;
import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Categorie;
import fr.eni.bookhub.entity.Evaluation;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.exception.OperationException;
import fr.eni.bookhub.repository.AuteurRepository;
import fr.eni.bookhub.repository.CategorieRepository;
import fr.eni.bookhub.repository.EvaluationRepository;
import fr.eni.bookhub.repository.LivreRepository;
import fr.eni.bookhub.storage.StorageService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@AllArgsConstructor
public class LivreService {

    private final LivreRepository livreRepository;
    private final AuteurRepository auteurRepository;
    private final CategorieRepository categorieRepository;
    private final EvaluationRepository evaluationRepository;
    private final StorageService storageService;
    private final ModelMapper modelMapper;

    public Page<LivreDTO> searchLivres(
            String query,
            Long auteurId,
            Long catId,
            int page
    ) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by("titre").ascending());

        String queryFilter = (query != null && !query.isBlank()) ? query : null;
        Long auteurFilter = (auteurId != null && auteurId > 0) ? auteurId : null;
        Long categorieFilter = (catId != null && catId > 0) ? catId : null;

        return livreRepository.findByCustomFilters(
                queryFilter,
                auteurFilter,
                categorieFilter,
                pageable
        );
    }

    /**
     * Récupère un livre par son ID.
     */
    public Optional<LivreDTO> getById(Long id) {
        return livreRepository.findByIdForDetails(id);
    }

    /**
     * Création d'un nouveau livre.
     */
    @Transactional
    public Boolean createLivre(CreateLivreDTO payload) {
        Optional<Livre> exists = livreRepository.findByIsbn(payload.getIsbn());

        if (exists.isPresent()) {
            throw new OperationException("Un livre avec cet ISBN existe déjà");
        }

        Livre livre = modelMapper.map(payload, Livre.class);

        Optional<Auteur> auteur = auteurRepository.findById(payload.getAuteurId());

        if (auteur.isEmpty()) {
            throw new OperationException("Impossible de trouver l'auteur");
        }

        livre.setAuteur(auteur.get());

        Optional<Categorie> categorie = categorieRepository.findById(payload.getCategorieId());

        if (categorie.isEmpty()) {
            throw new OperationException("Impossible de trouver la catégorie");
        }

        livre.setCategorie(categorie.get());

        livreRepository.save(livre);

        return livre.getId() != null;
    }

    /**
     * Modification d'un livre.
     */
    @Transactional
    public Boolean updateLivre(Long id, CreateLivreDTO payload) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new OperationException("Ce livre n'existe pas"));

        if (!Objects.equals(livre.getCategorie().getId(), payload.getCategorieId())) {
            Categorie categorie = categorieRepository.findById(payload.getCategorieId())
                    .orElseThrow(() -> new OperationException("Impossible de trouver la catégorie"));
            livre.setCategorie(categorie);
        }

        Auteur auteur = auteurRepository.findById(payload.getAuteurId())
                .orElseThrow(() -> new OperationException("Impossible de trouver l'auteur"));
        livre.setAuteur(auteur);

        modelMapper.map(payload, livre);
        livreRepository.save(livre);

        return true;
    }

    /**
     * Supprime un livre
     * @todo Il faut vérifier si des emprunts / réservation sont en cours
     */
    @Transactional
    public void deleteLivre(Long id) {
        livreRepository.deleteById(id);
    }

    /**
     * Définit la couverture du livre
     */
    @Transactional
    public LivreDTO updateCouvertureLivre(Long id, MultipartFile file) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new OperationException("Impossible de trouver le Livre"));

        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        List<String> extensionsAutorisees = Arrays.asList(".jpg", ".jpeg", ".png");

        if (!extensionsAutorisees.contains(extension.toLowerCase())) {
            throw new OperationException("Format de fichier non supporté : " + extension);
        }

        String filename = UUID.randomUUID() + extension;

        storageService.store(file, filename);

        String oldFilename = livre.getImageCouverture();

        livre.setImageCouverture(filename);
        livreRepository.save(livre);

        if (oldFilename != null) {
            storageService.delete(oldFilename);
        }

        return modelMapper.map(livre, LivreDTO.class);
    }

    public EvaluationDTO addRating(Long id, EvaluationDTO ratingRequest){
        Optional<Livre> livre = livreRepository.findById(id);
        Evaluation evaluation = new Evaluation();
        evaluation.setNote(ratingRequest.getNote());
        evaluation.setCommentaire(ratingRequest.getCommentaire());
        modelMapper.map(livre, EvaluationDTO.class);
        evaluationRepository.save(evaluation);
        return ratingRequest;
    }
}
