package fr.eni.bookhub.service;

import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Exemplaire;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.enumeration.StatutEnum;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.ExemplaireRepository;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class EmpruntService {

    private EmpruntRepository empruntRepository;
    private ExemplaireRepository exemplaireRepository;
    private UtilisateurRepository utilisateurRepository;

    @Transactional
    public Emprunt emprunterLivre(Long idUtilisateur, Long idExemplaire) {
        Utilisateur utilisateur = utilisateurRepository.findUtilisateurById(idUtilisateur);
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }
        Exemplaire exemplaire = exemplaireRepository.findById(idExemplaire)
                .orElseThrow(() -> new RuntimeException("Exemplaire non trouvé"));

        verifierReglesEmprunt(utilisateur, exemplaire);

        Emprunt emprunt = new Emprunt();
        emprunt.setUtilisateur(utilisateur);
        emprunt.setExemplaire(exemplaire);
        emprunt.setDateDebut(LocalDateTime.now());
        emprunt.setDateRetourPrevue(LocalDateTime.now().plusDays(14));
        emprunt.setStatut(StatutEnum.EN_COURS);

        exemplaire.setEstDisponible(false);
        exemplaireRepository.save(exemplaire);

        return empruntRepository.save(emprunt);
    }

    private void verifierReglesEmprunt(Utilisateur utilisateur, Exemplaire exemplaire) {
        // Max 3 emprunts simultanés
        List<Emprunt> empruntsEnCours = empruntRepository.findByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS);
        if (empruntsEnCours.size() >= 3) {
            throw new RuntimeException("Vous avez déjà 3 emprunts en cours.");
        }

        //Bloqué si retard
        boolean aUnRetard = empruntRepository.existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
                utilisateur, StatutEnum.EN_COURS, LocalDateTime.now());
        if (aUnRetard) {
            throw new RuntimeException("Vous avez un emprunt en retard. Veuillez le retourner avant de pouvoir emprunter un nouveau livre.");
        }

        //Exemplaire dispo
        if (!exemplaire.getEstDisponible()) {
            throw new RuntimeException("L'exemplaire n'est pas disponible");
        }
    }



}
