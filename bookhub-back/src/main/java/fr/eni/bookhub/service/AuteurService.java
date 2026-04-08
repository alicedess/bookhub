package fr.eni.bookhub.service;

import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.repository.AuteurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuteurService {

    private AuteurRepository auteurRepository;

    public List<Auteur> getAllAuteur(){
        return auteurRepository.findAll();
    }
}
