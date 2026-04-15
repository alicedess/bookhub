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
        List<Emprunt> empruntsEnCours = List.of(emprunt1, emprunt2);
        List<Emprunt> empruntsEnRetard = List.of(emprunt2);

        EmpruntDTO dto1 = new EmpruntDTO();
        EmpruntDTO dto2 = new EmpruntDTO();

        when(empruntRepository.findByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS))
                .thenReturn(empruntsEnCours);
        when(empruntRepository.findByUtilisateurAndStatutAndDateRetourPrevueBefore(
                eq(utilisateur), eq(StatutEnum.EN_COURS), any()))
                .thenReturn(empruntsEnRetard);
        when(empruntMapper.convertToDto(emprunt1)).thenReturn(dto1);
        when(empruntMapper.convertToDto(emprunt2)).thenReturn(dto2);

        List<EmpruntDTO> resultat = empruntService.getEmpruntsEnCoursDTO(utilisateur);

        assertNotNull(resultat);
        assertEquals(2, resultat.size());
        assertFalse(resultat.get(0).isEnRetard());
        assertTrue(resultat.get(1).isEnRetard());
        verify(empruntRepository).findByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS);
        verify(empruntRepository).findByUtilisateurAndStatutAndDateRetourPrevueBefore(
                eq(utilisateur), eq(StatutEnum.EN_COURS), any());
        verify(empruntMapper).convertToDto(emprunt1);
        verify(empruntMapper).convertToDto(emprunt2);
    }

    @Test
    void getEmpruntsHistoriqueDTO_RetourneLaListeHistorique() {
        Utilisateur utilisateur = new Utilisateur();
        Emprunt emprunt1 = new Emprunt();
        Emprunt emprunt2 = new Emprunt();
        List<Emprunt> empruntsHistorique = List.of(emprunt1, emprunt2);
        List<StatutEnum> statutsHistorique = List.of(StatutEnum.TERMINE, StatutEnum.RETARDE, StatutEnum.RETURNED);
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
    void emprunterLivre_CreerUnEmprunt() {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(ID_UTILISATEUR);
        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setId(10L);
        exemplaire.setEstDisponible(true);

        when(utilisateurRepository.findUtilisateurById(ID_UTILISATEUR)).thenReturn(utilisateur);
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
        when(utilisateurRepository.findUtilisateurById(ID_UTILISATEUR)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> empruntService.emprunterLivreDto(ID_UTILISATEUR, ID_LIVRE)
        );

        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    static Stream<Arguments> reglesEmpruntCases() {
        return Stream.of(
                Arguments.of(3, false, true, "Vous avez déjà 3 emprunts en cours."),
                Arguments.of(0, true, true,
                        "Vous avez un emprunt en retard. Veuillez le retourner avant de pouvoir emprunter un nouveau livre."),
                Arguments.of(0, false, false, "L'exemplaire n'est pas disponible")
        );
    }

    @ParameterizedTest
    @MethodSource("reglesEmpruntCases")
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

        when(utilisateurRepository.findUtilisateurById(ID_UTILISATEUR)).thenReturn(utilisateur);
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

    @Test
    void retournerLivre_RetourDansLesDelais() {
        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setId(10L);
        exemplaire.setEstDisponible(false);

        Emprunt emprunt = new Emprunt();
        emprunt.setIdEmprunt(1L);
        emprunt.setStatut(StatutEnum.EN_COURS);
        emprunt.setDateDebut(LocalDateTime.now().minusDays(7));
        emprunt.setDateRetourPrevue(LocalDateTime.now().plusDays(7));
        emprunt.setExemplaire(exemplaire);

        EmpruntDTO dto = new EmpruntDTO();
        dto.setStatut(StatutEnum.RETURNED);
        dto.setEnRetard(false);

        when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt));
        when(empruntRepository.save(any(Emprunt.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(empruntMapper.convertToDto(any(Emprunt.class))).thenReturn(dto);

        EmpruntDTO resultat = empruntService.retournerLivreDto(1L);

        assertNotNull(resultat);
        assertEquals(StatutEnum.RETURNED, resultat.getStatut());
        assertFalse(resultat.isEnRetard());
        assertEquals(StatutEnum.RETURNED, emprunt.getStatut());
        assertNotNull(emprunt.getDateRetourEffective());
        assertTrue(exemplaire.getEstDisponible());

        verify(exemplaireRepository).save(exemplaire);
        verify(empruntRepository).save(emprunt);
    }

    @Test
    void retournerLivre_RetourEnRetard() {
        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setId(10L);
        exemplaire.setEstDisponible(false);

        Emprunt emprunt = new Emprunt();
        emprunt.setIdEmprunt(2L);
        emprunt.setStatut(StatutEnum.EN_COURS);
        emprunt.setDateDebut(LocalDateTime.now().minusDays(21));
        emprunt.setDateRetourPrevue(LocalDateTime.now().minusDays(7));
        emprunt.setExemplaire(exemplaire);

        EmpruntDTO dto = new EmpruntDTO();
        dto.setStatut(StatutEnum.RETURNED);
        dto.setEnRetard(true);

        when(empruntRepository.findById(2L)).thenReturn(Optional.of(emprunt));
        when(empruntRepository.save(any(Emprunt.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(empruntMapper.convertToDto(any(Emprunt.class))).thenReturn(dto);

        EmpruntDTO resultat = empruntService.retournerLivreDto(2L);

        assertNotNull(resultat);
        assertTrue(emprunt.isEnRetard());
        assertEquals(StatutEnum.RETURNED, emprunt.getStatut());
        assertNotNull(emprunt.getDateRetourEffective());
        assertTrue(exemplaire.getEstDisponible());

        verify(exemplaireRepository).save(exemplaire);
        verify(empruntRepository).save(emprunt);
    }

    @Test
    void retournerLivre_LeverUneExceptionQuandEmpruntIntrouvable() {
        when(empruntRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> empruntService.retournerLivreDto(99L)
        );

        assertEquals("Emprunt non trouvé", exception.getMessage());
        verify(exemplaireRepository, never()).save(any(Exemplaire.class));
        verify(empruntRepository, never()).save(any(Emprunt.class));
    }

    @Test
    void retournerLivre_LeverUneExceptionQuandEmpruntPasEnCours() {
        Emprunt emprunt = new Emprunt();
        emprunt.setIdEmprunt(3L);
        emprunt.setStatut(StatutEnum.RETURNED);

        when(empruntRepository.findById(3L)).thenReturn(Optional.of(emprunt));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> empruntService.retournerLivreDto(3L)
        );

        assertEquals("Cet emprunt n'est pas en cours", exception.getMessage());
        verify(exemplaireRepository, never()).save(any(Exemplaire.class));
        verify(empruntRepository, never()).save(any(Emprunt.class));
    }

    @Test
    void getAllEmpruntsEnCoursDTO_RetourneTousLesEmpruntsEnCours() {
        Emprunt emprunt1 = new Emprunt();
        emprunt1.setIdEmprunt(1L);
        emprunt1.setDateRetourPrevue(LocalDateTime.now().plusDays(7));

        Emprunt emprunt2 = new Emprunt();
        emprunt2.setIdEmprunt(2L);
        emprunt2.setDateRetourPrevue(LocalDateTime.now().minusDays(3));

        List<Emprunt> emprunts = List.of(emprunt1, emprunt2);

        EmpruntDTO dto1 = new EmpruntDTO();
        EmpruntDTO dto2 = new EmpruntDTO();

        when(empruntRepository.findByStatut(StatutEnum.EN_COURS)).thenReturn(emprunts);
        when(empruntMapper.convertToDto(emprunt1)).thenReturn(dto1);
        when(empruntMapper.convertToDto(emprunt2)).thenReturn(dto2);

        List<EmpruntDTO> resultat = empruntService.getAllEmpruntsEnCoursDTO();

        assertNotNull(resultat);
        assertEquals(2, resultat.size());
        assertFalse(resultat.get(0).isEnRetard());
        assertTrue(resultat.get(1).isEnRetard());
        verify(empruntRepository).findByStatut(StatutEnum.EN_COURS);
    }

}

