package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.ProfilDTO;
import fr.eni.bookhub.dto.UtilisateurDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.mapper.UtilisateurMapper;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UtilisteurService {

    private UtilisateurRepository utilisateurRepository;
    private PasswordEncoder passwordEncoder;
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
            if (user != null && profilchangement != null) {
                // Only update if the field is not null and not blank
                if (profilchangement.getPseudo() != null && !profilchangement.getPseudo().trim().isEmpty()) {
                    user.setPseudo(profilchangement.getPseudo());
                }
                if (profilchangement.getNom() != null && !profilchangement.getNom().trim().isEmpty()) {
                    user.setNom(profilchangement.getNom());
                }
                if (profilchangement.getPrenom() != null && !profilchangement.getPrenom().trim().isEmpty()) {
                    user.setPrenom(profilchangement.getPrenom());
                }
                if (profilchangement.getPassword() != null && !profilchangement.getPassword().trim().isEmpty()) {
                    user.setPassword(passwordEncoder.encode(profilchangement.getPassword()));
                }
                if (profilchangement.getTelephone() != null && !profilchangement.getTelephone().trim().isEmpty()) {
                    user.setTelephone(profilchangement.getTelephone());
                }
                utilisateurRepository.save(user);
                return userMap.convertToProfilDto(user);
            } else {
                throw new RuntimeException("Utilisateur ne peut pas être vide");
            }
        } catch (Exception e) {
            throw new RuntimeException("Utilisateur non trouvé ou supprimé");
        }
    }
}
