package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.CreateLivreDTO;
import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.dto.LivreProjection;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Categorie;
import fr.eni.bookhub.entity.Evaluation;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.enumeration.StatutEnum;
import fr.eni.bookhub.exception.OperationException;
import fr.eni.bookhub.repository.*;
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
    private final EmpruntRepository empruntRepository;
    private final StorageService storageService;
    private final ModelMapper modelMapper;

    /**
     * Recherche les livres
     */
    public Page<LivreDTO> searchLivres(
            String query,
            Long auteurId,
            Long catId,
            Integer estDisponible,
            int page
    ) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by("titre").ascending());

        String queryFilter = (query != null && !query.isBlank()) ? query : null;
        Long auteurFilter = (auteurId != null && auteurId > 0) ? auteurId : null;
        Long categorieFilter = (catId != null && catId > 0) ? catId : null;

        Page<LivreProjection> projections = livreRepository.findByCustomFilters(
                queryFilter,
                auteurFilter,
                categorieFilter,
                estDisponible,
                pageable
        );

        return projections.map(p -> new LivreDTO(
                p.getId(),
                p.getIsbn(),
                p.getTitre(),
                p.getResume(),
                p.getImageCouverture(),
                p.getNbPage(),
                p.getDateParution(),
                p.getAuteurId(),
                p.getAuteurNom(),
                p.getAuteurPrenom(),
                p.getCategorieId(),
                p.getCategorieLibelle(),
                p.getNbExemplaires(),
                p.getNbExemplairesDisponibles(),
                p.getMoyenneEvaluations()
        ));
    }

    /**
     * Récupère un livre par son ID.
     */
    public Optional<LivreDTO> getById(Long id) {
        Optional<LivreProjection> projectionOptional = livreRepository.findByIdForDetails(id);

        if (projectionOptional.isEmpty()) {
            return Optional.empty();
        }

        LivreProjection projection = projectionOptional.get();

        return Optional.of(new LivreDTO(
                projection.getId(),
                projection.getIsbn(),
                projection.getTitre(),
                projection.getResume(),
                projection.getImageCouverture(),
                projection.getNbPage(),
                projection.getDateParution(),
                projection.getAuteurId(),
                projection.getAuteurNom(),
                projection.getAuteurPrenom(),
                projection.getCategorieId(),
                projection.getCategorieLibelle(),
                projection.getNbExemplaires(),
                projection.getNbExemplairesDisponibles(),
                projection.getMoyenneEvaluations()
        ));
    }

    /**
     * Création d'un nouveau livre.
     */
    @Transactional
    public LivreDTO createLivre(CreateLivreDTO payload) {
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

        if (null == livre.getId()) {
            throw new OperationException("Le livre n'existe pas");
        }

        return getById(livre.getId()).orElseThrow(() -> new OperationException("Le livre n'existe pas"));
    }

    /**
     * Modification d'un livre.
     */
    @Transactional
    public LivreDTO updateLivre(Long id, CreateLivreDTO payload) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new OperationException("Ce livre n'existe pas"));

        if (null != payload.getCategorieId()) {
            Categorie categorie = categorieRepository.findById(payload.getCategorieId())
                    .orElseThrow(() -> new OperationException("Impossible de trouver la catégorie"));
            livre.setCategorie(categorie);
        }

        if (null != payload.getAuteurId()) {
            Auteur auteur = auteurRepository.findById(payload.getAuteurId())
                    .orElseThrow(() -> new OperationException("Impossible de trouver l'auteur"));
            livre.setAuteur(auteur);
        }

        if (payload.getIsbn() != null) {
            livre.setIsbn(payload.getIsbn());
        }

        if (payload.getTitre() != null) {
            livre.setTitre(payload.getTitre());
        }

        if (payload.getResume() != null) {
            livre.setResume(payload.getResume());
        }

        if (payload.getNbPage() != null) {
            livre.setNbPage(payload.getNbPage());
        }

        if (payload.getDateParution() != null) {
            livre.setDateParution(payload.getDateParution());
        }

        livreRepository.save(livre);

        return getById(livre.getId()).orElseThrow(() -> new OperationException("Le livre n'existe pas"));
    }

    /**
     * Supprime un livre
     * @todo Il faut vérifier si des emprunts / réservation sont en cours
     */
    @Transactional
    public void deleteLivre(Long id)
    {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new OperationException("Impossible de trouver le Livre"));

        boolean aDesEmpruntsEnCours = empruntRepository.existsByLivre(livre, StatutEnum.EN_COURS);

        if (!aDesEmpruntsEnCours) {
            livreRepository.deleteById(id);
        }
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
