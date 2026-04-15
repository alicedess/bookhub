package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.StatsDTO;
import fr.eni.bookhub.enumeration.StatutEnum;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.LivreRepository;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class StatsService {

    private final UtilisateurRepository utilisateurRepository;
    private final LivreRepository livreRepository;
    private final EmpruntRepository empruntRepository;

    @Transactional(readOnly = true)
    public StatsDTO getStats() {
        long nombreUtilisateurs = utilisateurRepository.countByDateSuppressionIsNull();
        long nombreLivres = livreRepository.count();
        long nombreEmpruntsEnCours = empruntRepository.countByStatut(StatutEnum.EN_COURS);
        long nombreEmpruntsEnRetard = empruntRepository.countByStatutAndDateRetourPrevueBefore(
                StatutEnum.EN_COURS, LocalDateTime.now()
        );

        return new StatsDTO(nombreUtilisateurs, nombreLivres, nombreEmpruntsEnCours, nombreEmpruntsEnRetard);
    }
}
