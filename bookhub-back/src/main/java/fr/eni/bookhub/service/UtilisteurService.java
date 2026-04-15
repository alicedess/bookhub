package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.ProfilDTO;
import fr.eni.bookhub.dto.UpdateProfilDTO;
import fr.eni.bookhub.dto.UtilisateurDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.mapper.UtilisateurMapper;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public ProfilDTO updateUtilisateur(Long id, UpdateProfilDTO profilchangement){
        try {
            Utilisateur user = utilisateurRepository.findUtilisateurByIdWhereDateSuppressionIsNull(id);
            if (user != null && profilchangement != null) {

                if (profilchangement.getProfil().getPseudo() != null && !profilchangement.getProfil().getPseudo().trim().isEmpty()) {
                    user.setPseudo(profilchangement.getProfil().getPseudo());
                }
                if (profilchangement.getProfil().getNom() != null && !profilchangement.getProfil().getNom().trim().isEmpty()) {
                    user.setNom(profilchangement.getProfil().getNom());
                }
                if (profilchangement.getProfil().getPrenom() != null && !profilchangement.getProfil().getPrenom().trim().isEmpty()) {
                    user.setPrenom(profilchangement.getProfil().getPrenom());
                }
                if (profilchangement.getProfil().getPassword() != null && !profilchangement.getProfil().getPassword().trim().isEmpty()) {
                    if (profilchangement.getOldPassword() == null || profilchangement.getOldPassword().trim().isEmpty()) {
                        throw new RuntimeException("L'ancien mot de passe est requis pour changer le mot de passe");
                    }

                    if (user.getPassword() == null) {
                        throw new RuntimeException("Mot de passe non défini en base de données. Contactez l'administrateur.");
                    }

                    if (!passwordEncoder.matches(profilchangement.getOldPassword(), user.getPassword())) {
                        throw new RuntimeException("L'ancien mot de passe est incorrect");
                    }

                    user.setPassword(passwordEncoder.encode(profilchangement.getProfil().getPassword()));
                }
                if (profilchangement.getProfil().getTelephone() != null && !profilchangement.getProfil().getTelephone().trim().isEmpty()) {
                    user.setTelephone(profilchangement.getProfil().getTelephone());
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

    public UtilisateurDTO getUtilisateurParEmail(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return userMap.convertToDto(user);
    }
}
