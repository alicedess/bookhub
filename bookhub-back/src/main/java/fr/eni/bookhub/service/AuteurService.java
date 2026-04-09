package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.AuteurDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.repository.AuteurRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuteurService {

    private AuteurRepository auteurRepository;
    private final ModelMapper modelMapper;

    public List<Auteur> getAllAuteur()
    {

        return auteurRepository.findAll();
    }

    /**
     * Retourne l'ensemble des Auteur.
     */
    public Iterable<AuteurDTO> findAll() {
        return auteurRepository.findAll().stream()
                .map(c -> modelMapper.map(c, AuteurDTO.class))
                .toList();
    }
}
