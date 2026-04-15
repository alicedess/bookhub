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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpruntServiceTest {

    private static final Long ID_UTILISATEUR = 1L;
    private static final Long ID_LIVRE = 2L;
    private static final Long ID_EMPRUNT = 3L;

    @InjectMocks
    private EmpruntService empruntService;

    @Mock
    private UtilisateurRepository utilisateurRepository;
    @Mock
    private EmpruntRepository empruntRepository;
    @Mock
    private ExemplaireRepository exemplaireRepository;
    @Mock
    private EmpruntMapper empruntMapper;

    @Test
    void getEmpruntsEnCoursDTO_RetourneLaListeEnCoursAvecIndicationRetard() {
        Utilisateur utilisateur = new Utilisateur();

        Emprunt emprunt1 = new Emprunt();
        Emprunt emprunt2 = new Emprunt();
        emprunt1.setIdEmprunt(1L);
        emprunt2.setIdEmprunt(2L);

        emprunt1.setDateRetourPrevue(LocalDateTime.now().plusDays(5));
        emprunt2.setDateRetourPrevue(LocalDateTime.now().minusDays(1));

        EmpruntDTO dto1 = new EmpruntDTO();
        EmpruntDTO dto2 = new EmpruntDTO();

        when(empruntRepository.findByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS))
                .thenReturn(List.of(emprunt1, emprunt2));
        when(empruntMapper.convertToDto(emprunt1)).thenReturn(dto1);
        when(empruntMapper.convertToDto(emprunt2)).thenReturn(dto2);

        List<EmpruntDTO> resultat = empruntService.getEmpruntsEnCoursDTO(utilisateur);

        assertNotNull(resultat);
        assertEquals(2, resultat.size());
        assertFalse(resultat.get(0).isEnRetard());
        assertTrue(resultat.get(1).isEnRetard());

        verify(empruntRepository).findByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS);
        verify(empruntMapper).convertToDto(emprunt1);
        verify(empruntMapper).convertToDto(emprunt2);
    }

    @Test
    void getEmpruntsHistoriqueDTO_RetourneLaListeHistorique() {
        Utilisateur utilisateur = new Utilisateur();
        Emprunt emprunt1 = new Emprunt();
        Emprunt emprunt2 = new Emprunt();
        List<Emprunt> empruntsHistorique = List.of(emprunt1, emprunt2);
        List<StatutEnum> statutsHistorique = List.of(StatutEnum.RETOURNE, StatutEnum.RETARDE);
        List<EmpruntDTO> empruntsHistoriqueDto = List.of(new EmpruntDTO(), new EmpruntDTO());

        when(empruntRepository.findByUtilisateurAndStatutIn(utilisateur, statutsHistorique))
                .thenReturn(empruntsHistorique);
        when(empruntMapper.convertToDto(empruntsHistorique)).thenReturn(empruntsHistoriqueDto);

        List<EmpruntDTO> resultat = empruntService.getEmpruntsHistoriqueDTO(utilisateur);

        assertNotNull(resultat);
        assertEquals(2, resultat.size());
        assertEquals(empruntsHistoriqueDto, resultat);
        verify(empruntRepository).findByUtilisateurAndStatutIn(utilisateur, statutsHistorique);
        verify(empruntMapper).convertToDto(empruntsHistorique);
    }

    @Test
    void getEmpruntsEnRetard_RetourneLaListeRetard() {
        Utilisateur utilisateur = new Utilisateur();
        Emprunt emprunt = new Emprunt();
        List<Emprunt> empruntsRetard = List.of(emprunt);

        when(empruntRepository.findByUtilisateurAndStatutAndDateRetourPrevueBefore(
                eq(utilisateur), eq(StatutEnum.EN_COURS), any()))
                .thenReturn(empruntsRetard);

        List<Emprunt> resultat = empruntService.getEmpruntsEnRetard(utilisateur);

        assertNotNull(resultat);
        assertEquals(1, resultat.size());
        assertEquals(empruntsRetard, resultat);
        verify(empruntRepository).findByUtilisateurAndStatutAndDateRetourPrevueBefore(
                eq(utilisateur), eq(StatutEnum.EN_COURS), any());
    }

    @Test
    void retournerLivreDTO_MetAJourEmpruntEtExemplaire() {
        Emprunt emprunt = new Emprunt();
        emprunt.setStatut(StatutEnum.EN_COURS);
        emprunt.setDateRetourPrevue(LocalDateTime.now().plusDays(2));

        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setEstDisponible(false);
        emprunt.setExemplaire(exemplaire);

        when(empruntRepository.findById(ID_EMPRUNT)).thenReturn(Optional.of(emprunt));
        when(empruntRepository.save(any(Emprunt.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(empruntMapper.convertToDto(any(Emprunt.class))).thenAnswer(invocation -> {
            Emprunt saved = invocation.getArgument(0);
            EmpruntDTO dto = new EmpruntDTO();
            dto.setStatut(saved.getStatut());
            dto.setDateRetourEffective(saved.getDateRetourEffective());
            dto.setEnRetard(saved.isEnRetard());
            return dto;
        });

        EmpruntDTO resultat = empruntService.retournerLivreDTO(ID_EMPRUNT);

        assertNotNull(resultat);
        assertEquals(StatutEnum.RETOURNE, resultat.getStatut());
        assertFalse(resultat.isEnRetard());
        assertNotNull(resultat.getDateRetourEffective());
        assertTrue(exemplaire.getEstDisponible());

        verify(empruntRepository).findById(ID_EMPRUNT);
        verify(exemplaireRepository).save(exemplaire);
        verify(empruntRepository).save(emprunt);
        verify(empruntMapper).convertToDto(emprunt);
    }

    @Test
    void retournerLivreDTO_LeverExceptionQuandEmpruntIntrouvable() {
        when(empruntRepository.findById(ID_EMPRUNT)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> empruntService.retournerLivreDTO(ID_EMPRUNT)
        );

        assertEquals("Emprunt non trouvé", exception.getMessage());
        verify(exemplaireRepository, never()).save(any(Exemplaire.class));
        verify(empruntRepository, never()).save(any(Emprunt.class));
        verify(empruntMapper, never()).convertToDto(any(Emprunt.class));
    }

    @Test
    void emprunterLivre_CreerUnEmprunt() {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(ID_UTILISATEUR);
        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setId(10L);
        exemplaire.setEstDisponible(true);

        when(utilisateurRepository.findById(ID_UTILISATEUR)).thenReturn(Optional.of(utilisateur));
        when(exemplaireRepository.findFirstByLivreIdAndEstDisponibleTrue(ID_LIVRE)).thenReturn(Optional.of(exemplaire));
        when(empruntRepository.countByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS))
                .thenReturn(0L);
        when(empruntRepository.existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
                eq(utilisateur), eq(StatutEnum.EN_COURS), any()))
                .thenReturn(false);
        when(empruntRepository.save(any(Emprunt.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(empruntMapper.convertToDto(any(Emprunt.class))).thenAnswer(invocation -> {
            Emprunt emprunt = invocation.getArgument(0);
            EmpruntDTO dto = new EmpruntDTO();
            dto.setStatut(emprunt.getStatut());
            dto.setDateDebut(emprunt.getDateDebut());
            dto.setDateRetourPrevue(emprunt.getDateRetourPrevue());
            dto.setUtilisateurId(emprunt.getUtilisateur().getId());
            dto.setExemplaireId(emprunt.getExemplaire().getId());
            return dto;
        });

        EmpruntDTO resultat = empruntService.emprunterLivreDto(ID_UTILISATEUR, ID_LIVRE);

        assertNotNull(resultat);
        assertEquals(StatutEnum.EN_COURS, resultat.getStatut());
        assertEquals(ID_UTILISATEUR, resultat.getUtilisateurId());
        assertEquals(10L, resultat.getExemplaireId());
        assertFalse(exemplaire.getEstDisponible());
        assertNotNull(resultat.getDateDebut());
        assertNotNull(resultat.getDateRetourPrevue());

        verify(exemplaireRepository).save(exemplaire);
        verify(empruntRepository).save(any(Emprunt.class));
    }

    @Test
    void emprunterLivre_LeverUneExceptionQuandUtilisateurIntrouvable() {
        when(utilisateurRepository.findById(ID_UTILISATEUR)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> empruntService.emprunterLivreDto(ID_UTILISATEUR, ID_LIVRE)
        );

        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    static Stream<Arguments> reglesEmpruntCas() {
        return Stream.of(
                Arguments.of(3, false, true, "Vous avez déjà 3 emprunts en cours."),
                Arguments.of(0, true, true,
                        "Vous avez un emprunt en retard. Veuillez le retourner avant de pouvoir emprunter un nouveau livre."),
                Arguments.of(0, false, false, "L'exemplaire n'est pas disponible")
        );
    }

    @ParameterizedTest
    @MethodSource("reglesEmpruntCas")
    void emprunterLivre_LeverUneExceptionSelonReglesEmprunt(int nbEmpruntsEnCours,
                                                             boolean aUnRetard,
                                                             boolean exemplaireDisponible,
                                                             String messageAttendu) {
        Utilisateur utilisateur = new Utilisateur();
        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setEstDisponible(exemplaireDisponible);

        List<Emprunt> emprunts = new ArrayList<>();
        for (int i = 0; i < nbEmpruntsEnCours; i++) {
            emprunts.add(new Emprunt());
        }

        when(utilisateurRepository.findById(ID_UTILISATEUR)).thenReturn(Optional.of(utilisateur));
        when(exemplaireRepository.findFirstByLivreIdAndEstDisponibleTrue(ID_LIVRE)).thenReturn(Optional.of(exemplaire));
        when(empruntRepository.countByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS))
                .thenReturn((long) emprunts.size());
        if (nbEmpruntsEnCours < 3) {
            when(empruntRepository.existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
                    eq(utilisateur), eq(StatutEnum.EN_COURS), any())).thenReturn(aUnRetard);
        }

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> empruntService.emprunterLivreDto(ID_UTILISATEUR, ID_LIVRE)
        );

        assertEquals(messageAttendu, exception.getMessage());
        verify(exemplaireRepository, never()).save(any(Exemplaire.class));
        verify(empruntRepository, never()).save(any(Emprunt.class));
        if (nbEmpruntsEnCours >= 3) {
            verify(empruntRepository, never()).existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
                    any(Utilisateur.class), any(StatutEnum.class), any());
        }
    }

}

