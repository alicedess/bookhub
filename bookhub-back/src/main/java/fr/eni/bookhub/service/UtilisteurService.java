package fr.eni.bookhub.service;

import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UtilisteurService {
    private UtilisateurRepository utilisateurRepository;

    public Utilisateur getUtilisateurById(Integer id){
        return utilisateurRepository.findUtilisateurById(id);
    }
}
