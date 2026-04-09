package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.UtilisateurDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UtilisteurService {
    private UtilisateurRepository utilisateurRepository;

    /** Fonction générique des Entity en DTO */
    private UtilisateurDTO toDTO(Utilisateur user){
        UtilisateurDTO userDTO = new UtilisateurDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setNom(user.getNom());
        userDTO.setPrenom(user.getPrenom());
        userDTO.setTelephone(user.getTelephone());
        userDTO.setRole(user.getRole());
        userDTO.setDateNaissance(user.getDateNaissance());
        userDTO.setDateSuppression(user.getDateSuppression());
        return userDTO;
    }

    public UtilisateurDTO getUtilisateurById(Long id){
        Utilisateur user = utilisateurRepository.findUtilisateurById(id);
        return toDTO(user);
    }

    public UtilisateurDTO getUtilisateurActif(Long id){
        Utilisateur user = utilisateurRepository.findUtilisateurByIdWhereDateSuppressionIsNull(id);
        return toDTO(user);
    }
}
