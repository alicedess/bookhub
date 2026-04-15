package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.EmpruntDTO;
import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Exemplaire;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.enumeration.StatutEnum;
import fr.eni.bookhub.mapper.EmpruntMapper;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.ExemplaireRepository;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EmpruntService {

    private final EmpruntRepository empruntRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EmpruntMapper empruntMapper;


    private Emprunt emprunterLivre(Long idUtilisateur, Long idLivre) {
        Utilisateur utilisateur = utilisateurRepository.findUtilisateurById(idUtilisateur);
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }

        Exemplaire exemplaire = exemplaireRepository.findFirstByLivreIdAndEstDisponibleTrue(idLivre)
                .orElseThrow(() -> new RuntimeException("Aucun exemplaire disponible pour ce livre."));

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

    @Transactional
    public EmpruntDTO emprunterLivreDto(Long idUtilisateur, Long idLivre) {
        Emprunt emprunt = emprunterLivre(idUtilisateur, idLivre);
        return empruntMapper.convertToDto(emprunt);
    }

    private void verifierReglesEmprunt(Utilisateur utilisateur, Exemplaire exemplaire) {
        // Max 3 emprunts simultanés
        long empruntsEnCours = empruntRepository.countByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS);
        if (empruntsEnCours >= 3) {
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

    public List<EmpruntDTO> getEmpruntsEnCoursDTO(Utilisateur utilisateur) {
        List<Emprunt> emprunts = empruntRepository.findByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS);
        List<Emprunt> empruntsEnRetard = getEmpruntsEnRetard(utilisateur);

        return emprunts.stream()
                .map(emprunt -> {
                    EmpruntDTO dto = empruntMapper.convertToDto(emprunt);
                    dto.setEnRetard(empruntsEnRetard.contains(emprunt));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<EmpruntDTO> getEmpruntsHistoriqueDTO(Utilisateur utilisateur) {
        List<StatutEnum> statutsHistorique = List.of(StatutEnum.TERMINE, StatutEnum.RETARDE, StatutEnum.RETURNED);
        List<Emprunt> emprunts = empruntRepository.findByUtilisateurAndStatutIn(utilisateur, statutsHistorique);

        return empruntMapper.convertToDto(emprunts);
    }

    public List<Emprunt> getEmpruntsEnRetard(Utilisateur utilisateur) {
        return empruntRepository.findByUtilisateurAndStatutAndDateRetourPrevueBefore(
                utilisateur, StatutEnum.EN_COURS, LocalDateTime.now());
    }

    @Transactional
    public EmpruntDTO retournerLivreDto(Long idEmprunt) {
        Emprunt emprunt = empruntRepository.findById(idEmprunt)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));

        if (emprunt.getStatut() != StatutEnum.EN_COURS) {
            throw new RuntimeException("Cet emprunt n'est pas en cours");
        }

        LocalDateTime now = LocalDateTime.now();
        emprunt.setDateRetourEffective(now);

        boolean enRetard = now.isAfter(emprunt.getDateRetourPrevue());
        emprunt.setEnRetard(enRetard);
        emprunt.setStatut(StatutEnum.RETURNED);

        Exemplaire exemplaire = emprunt.getExemplaire();
        exemplaire.setEstDisponible(true);
        exemplaireRepository.save(exemplaire);

        empruntRepository.save(emprunt);

        return empruntMapper.convertToDto(emprunt);
    }

    public List<EmpruntDTO> getAllEmpruntsEnCoursDTO() {
        List<Emprunt> emprunts = empruntRepository.findByStatut(StatutEnum.EN_COURS);

        return emprunts.stream()
                .map(emprunt -> {
                    EmpruntDTO dto = empruntMapper.convertToDto(emprunt);
                    dto.setEnRetard(emprunt.getDateRetourPrevue().isBefore(LocalDateTime.now()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
