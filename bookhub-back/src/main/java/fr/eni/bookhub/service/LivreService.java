package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.CreateLivreDTO;
import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Categorie;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.exception.OperationException;
import fr.eni.bookhub.repository.AuteurRepository;
import fr.eni.bookhub.repository.CategorieRepository;
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

        Page<Livre> livresPage = livreRepository.findByCustomFilters(
                queryFilter,
                auteurFilter,
                categorieFilter,
                pageable
        );

        return livresPage.map(l -> modelMapper.map(l, LivreDTO.class));
    }

    /**
     * Récupère un livre par son ID.
     */
    public Optional<Livre> getById(Long id) {
        return livreRepository.findById(id).map(l -> modelMapper.map(l, Livre.class));
    }

    /**
     * Création d'un nouveau livre.
     */
    @Transactional
    public Boolean createLivre(CreateLivreDTO payload) {
        Livre livre = modelMapper.map(payload, Livre.class);

        Optional<Auteur> auteur = auteurRepository.findById(payload.getAuteurId());

        if (auteur.isEmpty()) {
            throw new OperationException("Impossible de trouver le Auteur");
        }

        livre.setAuteur(auteur.get());

        Optional<Categorie> categorie = categorieRepository.findById(payload.getCategorieId());

        if (categorie.isEmpty()) {
            throw new OperationException("Impossible de trouver la Categorie");
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
                .orElseThrow(() -> new OperationException("Impossible de trouver le Livre"));

        if (!Objects.equals(livre.getCategorie().getId(), payload.getCategorieId())) {
            Categorie categorie = categorieRepository.findById(payload.getCategorieId())
                    .orElseThrow(() -> new OperationException("Impossible de trouver la Categorie"));
            livre.setCategorie(categorie);
        }

        if (!Objects.equals(livre.getAuteur().getId(), payload.getAuteurId())) {
            Auteur auteur = auteurRepository.findById(payload.getAuteurId())
                    .orElseThrow(() -> new OperationException("Impossible de trouver l'Auteur"));
            livre.setAuteur(auteur);
        }

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
}
