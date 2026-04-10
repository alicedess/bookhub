package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.UtilisateurDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.mapper.UtilisateurMapper;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UtilisteurService {
    private UtilisateurRepository utilisateurRepository;

    private UtilisateurMapper userMap;

    public UtilisateurDTO getUtilisateurById(Long id){
        Utilisateur user = utilisateurRepository.findUtilisateurById(id);
        return userMap.convertToDto(user);
    }

    public UtilisateurDTO getUtilisateurActif(Long id){
        Utilisateur user = utilisateurRepository.findUtilisateurByIdWhereDateSuppressionIsNull(id);
        return userMap.convertToDto(user);
    }
}
