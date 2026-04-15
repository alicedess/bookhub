package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.EmpruntDTO;
import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Exemplaire;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.enumeration.StatutEnum;
import fr.eni.bookhub.exception.EmpruntException;
import fr.eni.bookhub.exception.ExemplaireIndisponibleException;
import fr.eni.bookhub.mapper.EmpruntMapper;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.ExemplaireRepository;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class EmpruntService {

    private final EmpruntRepository empruntRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EmpruntMapper empruntMapper;

    private static final int DUREE_EMPRUNT_JOURS = 14;

    public Page<EmpruntDTO> getAllEmpruntsDTO(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return empruntRepository.findAll(pageable)
                .map(empruntMapper::convertToDto);
    }

    protected Emprunt emprunterLivre(Long idUtilisateur, Long idExemplaire) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Exemplaire exemplaire = exemplaireRepository.findById(idExemplaire)
                .orElseThrow(() -> new RuntimeException("Exemplaire non trouvé"));

        verifierReglesEmprunt(utilisateur, exemplaire);

        Emprunt emprunt = new Emprunt();
        emprunt.setUtilisateur(utilisateur);
        emprunt.setExemplaire(exemplaire);
        emprunt.setDateDebut(LocalDateTime.now());
        emprunt.setDateRetourPrevue(LocalDateTime.now().plusDays(DUREE_EMPRUNT_JOURS));
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
            throw new EmpruntException("Vous avez déjà 3 emprunts en cours.");
        }

        //Bloqué si retard
        boolean aUnRetard = empruntRepository.existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
                utilisateur, StatutEnum.EN_COURS, LocalDateTime.now());
        if (aUnRetard) {
            throw new EmpruntException("Vous avez un emprunt en retard. Veuillez le retourner avant de pouvoir emprunter un nouveau livre.");
        }

        //Exemplaire dispo
        if (!exemplaire.getEstDisponible()) {
            throw new ExemplaireIndisponibleException("L'exemplaire n'est pas disponible");
        }
    }

    public List<EmpruntDTO> getEmpruntsEnCoursDTO(Utilisateur utilisateur) {
        LocalDateTime maintenant = LocalDateTime.now();
        return empruntRepository.findByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS)
                .stream()
                .map(emprunt -> {
                    EmpruntDTO dto = empruntMapper.convertToDto(emprunt);
                    dto.setEnRetard(emprunt.getDateRetourPrevue().isBefore(maintenant));
                    return dto;
                })
                .toList();
    }

    public List<EmpruntDTO> getEmpruntsHistoriqueDTO(Utilisateur utilisateur) {
        List<StatutEnum> statutsHistorique = List.of(StatutEnum.TERMINE, StatutEnum.RETARDE);
        List<Emprunt> emprunts = empruntRepository.findByUtilisateurAndStatutIn(utilisateur, statutsHistorique);

        return empruntMapper.convertToDto(emprunts);
    }

    public List<Emprunt> getEmpruntsEnRetard(Utilisateur utilisateur) {
        return empruntRepository.findByUtilisateurAndStatutAndDateRetourPrevueBefore(
                utilisateur, StatutEnum.EN_COURS, LocalDateTime.now());
    }


}
