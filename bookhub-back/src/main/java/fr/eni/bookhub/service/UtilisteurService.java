package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.ProfilDTO;
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

    public UtilisateurDTO getUtilisateurActif(Long id){
        try {
            Utilisateur user = utilisateurRepository.findUtilisateurByIdWhereDateSuppressionIsNull(id);
            return userMap.convertToDto(user);
        } catch (Exception e) {
            throw new RuntimeException("Utilisateur non trouvé ou supprimé");
        }
    }

    public ProfilDTO updateUtilisateur(Long id, ProfilDTO profilchangement){
        try {
            Utilisateur user = utilisateurRepository.findUtilisateurByIdWhereDateSuppressionIsNull(id);
            user.setPseudo(profilchangement.getPseudo());
            user.setNom(profilchangement.getNom());
            user.setPrenom(profilchangement.getPrenom());
            user.setPassword(profilchangement.getPassword());
            user.setTelephone(profilchangement.getTelephone());
            utilisateurRepository.save(user);
            return userMap.convertToProfilDto(user);
        } catch (Exception e) {
            throw new RuntimeException("Utilisateur non trouvé ou supprimé");
        }
    }
}
