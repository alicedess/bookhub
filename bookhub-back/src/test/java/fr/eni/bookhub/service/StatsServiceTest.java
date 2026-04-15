package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.StatsDTO;
import fr.eni.bookhub.enumeration.StatutEnum;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.LivreRepository;
import fr.eni.bookhub.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @InjectMocks
    private StatsService statsService;

    @Mock
    private UtilisateurRepository utilisateurRepository;
    @Mock
    private LivreRepository livreRepository;
    @Mock
    private EmpruntRepository empruntRepository;

    @Test
    void getStats_retourneLesBonnesValeurs() {
        when(utilisateurRepository.countByDateSuppressionIsNull()).thenReturn(42L);
        when(livreRepository.count()).thenReturn(150L);
        when(empruntRepository.countByStatut(StatutEnum.EN_COURS)).thenReturn(27L);
        when(empruntRepository.countByStatutAndDateRetourPrevueBefore(
                eq(StatutEnum.EN_COURS), any(LocalDateTime.class))).thenReturn(5L);

        StatsDTO resultat = statsService.getStats();

        assertNotNull(resultat);
        assertEquals(42L, resultat.getNombreUtilisateurs());
        assertEquals(150L, resultat.getNombreLivres());
        assertEquals(27L, resultat.getNombreEmpruntsEnCours());
        assertEquals(5L, resultat.getNombreEmpruntsEnRetard());

        verify(utilisateurRepository).countByDateSuppressionIsNull();
        verify(livreRepository).count();
        verify(empruntRepository).countByStatut(StatutEnum.EN_COURS);
        verify(empruntRepository).countByStatutAndDateRetourPrevueBefore(
                eq(StatutEnum.EN_COURS), any(LocalDateTime.class));
    }

    @Test
    void getStats_retourneZeroQuandAucuneDonnee() {
        when(utilisateurRepository.countByDateSuppressionIsNull()).thenReturn(0L);
        when(livreRepository.count()).thenReturn(0L);
        when(empruntRepository.countByStatut(StatutEnum.EN_COURS)).thenReturn(0L);
        when(empruntRepository.countByStatutAndDateRetourPrevueBefore(
                eq(StatutEnum.EN_COURS), any(LocalDateTime.class))).thenReturn(0L);

        StatsDTO resultat = statsService.getStats();

        assertNotNull(resultat);
        assertEquals(0L, resultat.getNombreUtilisateurs());
        assertEquals(0L, resultat.getNombreLivres());
        assertEquals(0L, resultat.getNombreEmpruntsEnCours());
        assertEquals(0L, resultat.getNombreEmpruntsEnRetard());
    }

    @Test
    void getStats_empruntsEnRetardToujoursInferieurOuEgalEnCours() {
        when(utilisateurRepository.countByDateSuppressionIsNull()).thenReturn(10L);
        when(livreRepository.count()).thenReturn(50L);
        when(empruntRepository.countByStatut(StatutEnum.EN_COURS)).thenReturn(15L);
        when(empruntRepository.countByStatutAndDateRetourPrevueBefore(
                eq(StatutEnum.EN_COURS), any(LocalDateTime.class))).thenReturn(4L);

        StatsDTO resultat = statsService.getStats();

        assertTrue(
                resultat.getNombreEmpruntsEnRetard() <= resultat.getNombreEmpruntsEnCours(),
                "Les emprunts en retard ne peuvent pas dépasser le total des emprunts en cours"
        );
    }

    @Test
    void getStats_utilisateursNePrendPasEnCompteLesComptesSupprimees() {
        // Seuls les utilisateurs sans date de suppression doivent être comptés
        when(utilisateurRepository.countByDateSuppressionIsNull()).thenReturn(8L);
        when(livreRepository.count()).thenReturn(20L);
        when(empruntRepository.countByStatut(StatutEnum.EN_COURS)).thenReturn(3L);
        when(empruntRepository.countByStatutAndDateRetourPrevueBefore(
                eq(StatutEnum.EN_COURS), any(LocalDateTime.class))).thenReturn(1L);

        StatsDTO resultat = statsService.getStats();

        // Vérification que la méthode filtrante est bien appelée (pas countAll)
        verify(utilisateurRepository).countByDateSuppressionIsNull();
        assertEquals(8L, resultat.getNombreUtilisateurs());
    }
}
