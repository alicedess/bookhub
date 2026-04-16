package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.AuteurDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.repository.AuteurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuteurServiceTest {

    @InjectMocks
    private AuteurService auteurService;

    @Mock private AuteurRepository auteurRepository;
    @Mock private ModelMapper modelMapper;

    @Test
    void getAllAuteur_retourneLesEntitesDirectement() {
        Auteur a1 = new Auteur();
        a1.setId(1L);
        a1.setNom("Hugo");
        a1.setPrenom("Victor");
        Auteur a2 = new Auteur();
        a2.setId(2L);
        a2.setNom("Zola");
        a2.setPrenom("Émile");

        when(auteurRepository.findAll()).thenReturn(List.of(a1, a2));

        List<Auteur> result = auteurService.getAllAuteur();

        assertEquals(2, result.size());
        assertEquals("Hugo", result.get(0).getNom());
        assertEquals("Zola", result.get(1).getNom());
        verify(auteurRepository).findAll();
    }

    @Test
    void getAllAuteur_aucunAuteur_retourneListeVide() {
        when(auteurRepository.findAll()).thenReturn(List.of());

        List<Auteur> result = auteurService.getAllAuteur();

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_retourneTousLesAuteursMappeesEnDTO() {
        Auteur a1 = new Auteur();
        a1.setId(1L);
        a1.setNom("Hugo");
        a1.setPrenom("Victor");

        AuteurDTO dto1 = new AuteurDTO(1, "Hugo", "Victor");

        when(auteurRepository.findAll()).thenReturn(List.of(a1));
        when(modelMapper.map(a1, AuteurDTO.class)).thenReturn(dto1);

        List<AuteurDTO> result = (List<AuteurDTO>) auteurService.findAll();

        assertEquals(1, result.size());
        assertEquals("Hugo", result.get(0).getNom());
        assertEquals("Victor", result.get(0).getPrenom());
    }

    @Test
    void findAll_aucunAuteur_retourneListeVide() {
        when(auteurRepository.findAll()).thenReturn(List.of());

        Iterable<AuteurDTO> result = auteurService.findAll();

        assertFalse(result.iterator().hasNext());
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void findAll_mapperAppelePourChaqueAuteur() {
        Auteur a1 = new Auteur();
        Auteur a2 = new Auteur();
        Auteur a3 = new Auteur();

        when(auteurRepository.findAll()).thenReturn(List.of(a1, a2, a3));
        when(modelMapper.map(any(Auteur.class), eq(AuteurDTO.class))).thenReturn(new AuteurDTO());

        List<AuteurDTO> result = (List<AuteurDTO>) auteurService.findAll();

        assertEquals(3, result.size());
        verify(modelMapper, times(3)).map(any(Auteur.class), eq(AuteurDTO.class));
    }
}
