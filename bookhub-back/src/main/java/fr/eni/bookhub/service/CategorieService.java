package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.CategorieDTO;
import fr.eni.bookhub.entity.Categorie;
import fr.eni.bookhub.repository.CategorieRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategorieService {

    private CategorieRepository categorieRepository;
    private final ModelMapper modelMapper;

    /**
     * Retourne l'ensemble des catégories.
     */
    public Iterable<CategorieDTO> findAll() {
        return categorieRepository.findAll().stream()
                .map(c -> modelMapper.map(c, CategorieDTO.class))
                .toList();
    }

}
