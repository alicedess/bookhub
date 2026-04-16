package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.CategorieDTO;
import fr.eni.bookhub.entity.Categorie;
import fr.eni.bookhub.repository.CategorieRepository;
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
class CategorieServiceTest {

    @InjectMocks
    private CategorieService categorieService;

    @Mock private CategorieRepository categorieRepository;
    @Mock private ModelMapper modelMapper;

    @Test
    void findAll_retourneToutesLesCategoriesMappeesEnDTO() {
        Categorie c1 = new Categorie();
        c1.setId(1L);
        c1.setLibelle("Roman");
        Categorie c2 = new Categorie();
        c2.setId(2L);
        c2.setLibelle("Policier");

        CategorieDTO dto1 = new CategorieDTO(1, "Roman");
        CategorieDTO dto2 = new CategorieDTO(2, "Policier");

        when(categorieRepository.findAll()).thenReturn(List.of(c1, c2));
        when(modelMapper.map(c1, CategorieDTO.class)).thenReturn(dto1);
        when(modelMapper.map(c2, CategorieDTO.class)).thenReturn(dto2);

        List<CategorieDTO> result = (List<CategorieDTO>) categorieService.findAll();

        assertEquals(2, result.size());
        assertEquals("Roman", result.get(0).getLibelle());
        assertEquals("Policier", result.get(1).getLibelle());
        verify(categorieRepository).findAll();
    }

    @Test
    void findAll_aucuneCategorie_retourneListeVide() {
        when(categorieRepository.findAll()).thenReturn(List.of());

        Iterable<CategorieDTO> result = categorieService.findAll();

        assertFalse(result.iterator().hasNext());
        verify(categorieRepository).findAll();
    }

    @Test
    void findAll_mapperAppelePourChaqueCategorie() {
        Categorie c1 = new Categorie();
        c1.setId(1L);
        Categorie c2 = new Categorie();
        c2.setId(2L);
        Categorie c3 = new Categorie();
        c3.setId(3L);

        when(categorieRepository.findAll()).thenReturn(List.of(c1, c2, c3));
        when(modelMapper.map(any(Categorie.class), eq(CategorieDTO.class)))
                .thenReturn(new CategorieDTO());

        List<CategorieDTO> result = (List<CategorieDTO>) categorieService.findAll();

        assertEquals(3, result.size());
        verify(modelMapper, times(3)).map(any(Categorie.class), eq(CategorieDTO.class));
    }
}
