package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.ExemplaireDTO;
import fr.eni.bookhub.entity.Exemplaire;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.enumeration.EtatExemplaireEnum;
import fr.eni.bookhub.repository.ExemplaireRepository;
import fr.eni.bookhub.repository.LivreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExemplaireServiceTest {

    @InjectMocks
    private ExemplaireService exemplaireService;

    @Mock private ExemplaireRepository exemplaireRepository;
    @Mock private LivreRepository livreRepository;

    @Test
    void getExemplairesParLivreId_retourneLaListeDesDTO() {
        Livre livre = new Livre();
        livre.setId(1L);

        Exemplaire e1 = new Exemplaire();
        e1.setId(10L);
        e1.setCodeBarre("CB-001");
        e1.setEtat(EtatExemplaireEnum.BON);
        e1.setEstDisponible(true);
        e1.setLivre(livre);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(exemplaireRepository.findByLivre(livre)).thenReturn(List.of(e1));

        List<ExemplaireDTO> result = exemplaireService.getExemplairesParLivreId(1L);

        assertEquals(1, result.size());
        assertEquals("CB-001", result.get(0).getCodeBarre());
        assertEquals(EtatExemplaireEnum.BON, result.get(0).getEtat());
        assertTrue(result.get(0).getEstDisponible());
        assertEquals(1L, result.get(0).getIdLivre());
    }

    @Test
    void getExemplairesParLivreId_plusieursExemplaires_retourneTousLesDTO() {
        Livre livre = new Livre();
        livre.setId(1L);

        Exemplaire e1 = new Exemplaire(1L, "CB-001", EtatExemplaireEnum.NEUF, true, livre);
        Exemplaire e2 = new Exemplaire(2L, "CB-002", EtatExemplaireEnum.ABIME, false, livre);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(exemplaireRepository.findByLivre(livre)).thenReturn(List.of(e1, e2));

        List<ExemplaireDTO> result = exemplaireService.getExemplairesParLivreId(1L);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getEstDisponible());
        assertFalse(result.get(1).getEstDisponible());
    }

    @Test
    void getExemplairesParLivreId_aucunExemplaire_retourneListeVide() {
        Livre livre = new Livre();
        livre.setId(1L);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(exemplaireRepository.findByLivre(livre)).thenReturn(List.of());

        List<ExemplaireDTO> result = exemplaireService.getExemplairesParLivreId(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getExemplairesParLivreId_livreInexistant_leveEntityNotFoundException() {
        when(livreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> exemplaireService.getExemplairesParLivreId(99L));
    }

    @Test
    void updateExemplairesParLivreId_livreInexistant_leveEntityNotFoundException() {
        when(livreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> exemplaireService.updateExemplairesParLivreId(99L, List.of()));
    }

    @Test
    void updateExemplairesParLivreId_creerNouvelExemplaire_sansId() {
        Livre livre = new Livre();
        livre.setId(1L);

        ExemplaireDTO dto = new ExemplaireDTO(null, "CB-NEUF", EtatExemplaireEnum.NEUF, true, null);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(exemplaireRepository.findByLivre(livre)).thenReturn(List.of());

        exemplaireService.updateExemplairesParLivreId(1L, List.of(dto));

        ArgumentCaptor<Exemplaire> captor = ArgumentCaptor.forClass(Exemplaire.class);
        verify(exemplaireRepository).save(captor.capture());
        assertEquals("CB-NEUF", captor.getValue().getCodeBarre());
        assertEquals(EtatExemplaireEnum.NEUF, captor.getValue().getEtat());
        assertTrue(captor.getValue().getEstDisponible());
        assertEquals(livre, captor.getValue().getLivre());
    }

    @Test
    void updateExemplairesParLivreId_miseAJourExemplaireExistant() {
        Livre livre = new Livre();
        livre.setId(1L);

        Exemplaire existant = new Exemplaire(10L, "ANCIEN-CB", EtatExemplaireEnum.BON, true, livre);

        ExemplaireDTO dto = new ExemplaireDTO(10L, "NOUVEAU-CB", EtatExemplaireEnum.ABIME, false, null);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(exemplaireRepository.findByLivre(livre)).thenReturn(List.of(existant));
        when(exemplaireRepository.findById(10L)).thenReturn(Optional.of(existant));

        exemplaireService.updateExemplairesParLivreId(1L, List.of(dto));

        ArgumentCaptor<Exemplaire> captor = ArgumentCaptor.forClass(Exemplaire.class);
        verify(exemplaireRepository).save(captor.capture());
        assertEquals("NOUVEAU-CB", captor.getValue().getCodeBarre());
        assertEquals(EtatExemplaireEnum.ABIME, captor.getValue().getEtat());
        assertFalse(captor.getValue().getEstDisponible());
    }

    @Test
    void updateExemplairesParLivreId_supprimeExemplairesAbsentsDuPayload() {
        Livre livre = new Livre();
        livre.setId(1L);

        Exemplaire aSupprimer = new Exemplaire(5L, "CB-OLD", EtatExemplaireEnum.BON, true, livre);

        // Le payload ne contient pas l'ID 5 → doit être supprimé
        ExemplaireDTO dto = new ExemplaireDTO(null, "CB-NOUVEAU", EtatExemplaireEnum.NEUF, true, null);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(exemplaireRepository.findByLivre(livre)).thenReturn(List.of(aSupprimer));

        exemplaireService.updateExemplairesParLivreId(1L, List.of(dto));

        ArgumentCaptor<List<Exemplaire>> deleteCaptor = ArgumentCaptor.forClass(List.class);
        verify(exemplaireRepository).deleteAll(deleteCaptor.capture());
        assertEquals(1, deleteCaptor.getValue().size());
        assertEquals(5L, deleteCaptor.getValue().get(0).getId());
    }

    @Test
    void updateExemplairesParLivreId_payloadVide_supprimeTousLesExemplaires() {
        Livre livre = new Livre();
        livre.setId(1L);

        Exemplaire e1 = new Exemplaire(1L, "CB-001", EtatExemplaireEnum.BON, true, livre);
        Exemplaire e2 = new Exemplaire(2L, "CB-002", EtatExemplaireEnum.NEUF, true, livre);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(exemplaireRepository.findByLivre(livre)).thenReturn(List.of(e1, e2));

        exemplaireService.updateExemplairesParLivreId(1L, List.of());

        ArgumentCaptor<List<Exemplaire>> deleteCaptor = ArgumentCaptor.forClass(List.class);
        verify(exemplaireRepository).deleteAll(deleteCaptor.capture());
        assertEquals(2, deleteCaptor.getValue().size());
        verify(exemplaireRepository, never()).save(any());
    }

    @Test
    void updateExemplairesParLivreId_estDisponibleNull_traitéCommefalse() {
        Livre livre = new Livre();
        livre.setId(1L);

        ExemplaireDTO dto = new ExemplaireDTO(null, "CB", EtatExemplaireEnum.NEUF, null, null);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(exemplaireRepository.findByLivre(livre)).thenReturn(List.of());

        exemplaireService.updateExemplairesParLivreId(1L, List.of(dto));

        ArgumentCaptor<Exemplaire> captor = ArgumentCaptor.forClass(Exemplaire.class);
        verify(exemplaireRepository).save(captor.capture());
        assertFalse(captor.getValue().getEstDisponible());
    }
}
