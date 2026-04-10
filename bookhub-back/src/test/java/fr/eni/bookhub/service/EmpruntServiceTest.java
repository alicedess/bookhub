package fr.eni.bookhub.service;

import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Exemplaire;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.enumeration.StatutEnum;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.ExemplaireRepository;
import fr.eni.bookhub.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpruntServiceTest {

    private static final Long ID_UTILISATEUR = 1L;
    private static final Long ID_EXEMPLAIRE = 2L;

    @InjectMocks
    private EmpruntService empruntService;

    @Mock
    private UtilisateurRepository utilisateurRepository;
    @Mock
    private EmpruntRepository empruntRepository;
    @Mock
    private ExemplaireRepository exemplaireRepository;

    @Test
    void emprunterLivre_CreerUnEmprunt() {

        Utilisateur utilisateur = new Utilisateur();
        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setEstDisponible(true);

        when(utilisateurRepository.findUtilisateurById(ID_UTILISATEUR)).thenReturn(utilisateur);
        when(exemplaireRepository.findById(ID_EXEMPLAIRE)).thenReturn(Optional.of(exemplaire));
        when(empruntRepository.findByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS))
                .thenReturn(Collections.emptyList());
        when(empruntRepository.existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
                eq(utilisateur), eq(StatutEnum.EN_COURS), any()))
                .thenReturn(false);
        when(empruntRepository.save(any(Emprunt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Emprunt resultat = empruntService.emprunterLivre(ID_UTILISATEUR, ID_EXEMPLAIRE);

        assertNotNull(resultat);
        assertEquals(StatutEnum.EN_COURS, resultat.getStatut());
        assertEquals(utilisateur, resultat.getUtilisateur());
        assertEquals(exemplaire, resultat.getExemplaire());
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
                () -> empruntService.emprunterLivre(ID_UTILISATEUR, ID_EXEMPLAIRE)
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
        when(exemplaireRepository.findById(ID_EXEMPLAIRE)).thenReturn(Optional.of(exemplaire));
        when(empruntRepository.findByUtilisateurAndStatut(utilisateur, StatutEnum.EN_COURS)).thenReturn(emprunts);
        if (nbEmpruntsEnCours < 3) {
            when(empruntRepository.existsByUtilisateurAndStatutAndDateRetourPrevueBefore(
                    eq(utilisateur), eq(StatutEnum.EN_COURS), any())).thenReturn(aUnRetard);
        }

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> empruntService.emprunterLivre(ID_UTILISATEUR, ID_EXEMPLAIRE)
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