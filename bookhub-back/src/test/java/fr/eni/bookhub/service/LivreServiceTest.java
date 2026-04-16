package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.CreateLivreDTO;
import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.dto.LivreProjection;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Categorie;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.enumeration.StatutEnum;
import fr.eni.bookhub.exception.OperationException;
import fr.eni.bookhub.repository.*;
import fr.eni.bookhub.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivreServiceTest {

    @InjectMocks
    private LivreService livreService;

    @Mock private LivreRepository livreRepository;
    @Mock private AuteurRepository auteurRepository;
    @Mock private CategorieRepository categorieRepository;
    @Mock private EvaluationRepository evaluationRepository;
    @Mock private EmpruntRepository empruntRepository;
    @Mock private StorageService storageService;
    @Mock private ModelMapper modelMapper;

    private LivreProjection mockProjection(Long id, String isbn, String titre) {
        LivreProjection p = mock(LivreProjection.class);
        when(p.getId()).thenReturn(id);
        when(p.getIsbn()).thenReturn(isbn);
        when(p.getTitre()).thenReturn(titre);
        when(p.getResume()).thenReturn("Un résumé");
        when(p.getImageCouverture()).thenReturn(null);
        when(p.getNbPage()).thenReturn(300);
        //when(p.getDateParution()).thenReturn(new Date());
        when(p.getAuteurId()).thenReturn(1L);
        when(p.getAuteurNom()).thenReturn("Hugo");
        when(p.getAuteurPrenom()).thenReturn("Victor");
        when(p.getCategorieId()).thenReturn(1L);
        when(p.getCategorieLibelle()).thenReturn("Roman");
        when(p.getNbExemplaires()).thenReturn(3L);
        when(p.getNbExemplairesDisponibles()).thenReturn(2L);
        when(p.getMoyenneEvaluations()).thenReturn(4.0);
        return p;
    }

    @Test
    void searchLivres_retourneLaPageMappeeEnDTO() {
        LivreProjection proj = mockProjection(1L, "9782070360024", "Les Misérables");
        Page<LivreProjection> page = new PageImpl<>(List.of(proj));
        when(livreRepository.findByCustomFilters(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        Page<LivreDTO> result = livreService.searchLivres("misérables", null, null, 0, 0);

        assertEquals(1, result.getTotalElements());
        assertEquals("Les Misérables", result.getContent().get(0).getTitre());
        assertEquals("9782070360024", result.getContent().get(0).getIsbn());
    }

    @Test
    void searchLivres_queryBlancheTransmiseNullAuRepository() {
        when(livreRepository.findByCustomFilters(isNull(), isNull(), isNull(), any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<LivreDTO> result = livreService.searchLivres("   ", 0L, 0L, 0, 0);

        assertTrue(result.isEmpty());
        verify(livreRepository).findByCustomFilters(isNull(), isNull(), isNull(), any(), any(Pageable.class));
    }

    @Test
    void searchLivres_avecFiltreCategorieEtAuteur_passeLesIdsAuRepository() {
        LivreProjection proj = mockProjection(2L, "ISBN", "Titre");
        when(livreRepository.findByCustomFilters(eq("roman"), eq(5L), eq(3L), eq(1), any()))
                .thenReturn(new PageImpl<>(List.of(proj)));

        Page<LivreDTO> result = livreService.searchLivres("roman", 5L, 3L, 1, 0);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void searchLivres_pageVide_retournePageVide() {
        when(livreRepository.findByCustomFilters(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<LivreDTO> result = livreService.searchLivres(null, null, null, 0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void getById_livreExistant_retourneLDTO() {
        LivreProjection proj = mockProjection(1L, "9782070360024", "Les Misérables");
        when(livreRepository.findByIdForDetails(1L)).thenReturn(Optional.of(proj));

        Optional<LivreDTO> result = livreService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Les Misérables", result.get().getTitre());
        assertEquals("Hugo", result.get().getAuteurNom());
        assertEquals(2L, result.get().getNbExemplaireDispo());
    }

    @Test
    void getById_livreInexistant_retourneOptionalVide() {
        when(livreRepository.findByIdForDetails(99L)).thenReturn(Optional.empty());

        Optional<LivreDTO> result = livreService.getById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void createLivre_succes_retourneLivreDTO() {
        CreateLivreDTO dto = new CreateLivreDTO();
        dto.setIsbn("9782070360024");
        dto.setTitre("Les Misérables");
        dto.setAuteurId(1L);
        dto.setCategorieId(1L);
        dto.setResume("Un grand roman");

        Auteur auteur = new Auteur();
        Categorie categorie = new Categorie();
        Livre livreSauve = new Livre();
        livreSauve.setId(10L);

        LivreProjection proj = mockProjection(10L, dto.getIsbn(), dto.getTitre());

        when(livreRepository.findByIsbn("9782070360024")).thenReturn(Optional.empty());
        when(modelMapper.map(dto, Livre.class)).thenReturn(livreSauve);
        when(auteurRepository.findById(1L)).thenReturn(Optional.of(auteur));
        when(categorieRepository.findById(1L)).thenReturn(Optional.of(categorie));
        when(livreRepository.save(any(Livre.class))).thenReturn(livreSauve);
        when(livreRepository.findByIdForDetails(10L)).thenReturn(Optional.of(proj));

        LivreDTO result = livreService.createLivre(dto);

        assertNotNull(result);
        assertEquals("Les Misérables", result.getTitre());
        verify(livreRepository).save(livreSauve);
    }

    @Test
    void createLivre_isbnDejaExistant_leveOperationException() {
        CreateLivreDTO dto = new CreateLivreDTO();
        dto.setIsbn("9782070360024");
        when(livreRepository.findByIsbn("9782070360024")).thenReturn(Optional.of(new Livre()));

        OperationException ex = assertThrows(OperationException.class,
                () -> livreService.createLivre(dto));

        assertTrue(ex.getMessage().contains("ISBN"));
        verify(livreRepository, never()).save(any());
    }

    @Test
    void createLivre_auteurInexistant_leveOperationException() {
        CreateLivreDTO dto = new CreateLivreDTO();
        dto.setIsbn("9782070360024");
        dto.setAuteurId(99L);

        when(livreRepository.findByIsbn(any())).thenReturn(Optional.empty());
        when(modelMapper.map(dto, Livre.class)).thenReturn(new Livre());
        when(auteurRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> livreService.createLivre(dto));
        verify(categorieRepository, never()).findById(any());
    }

    @Test
    void createLivre_categorieInexistante_leveOperationException() {
        CreateLivreDTO dto = new CreateLivreDTO();
        dto.setIsbn("9782070360024");
        dto.setAuteurId(1L);
        dto.setCategorieId(99L);

        when(livreRepository.findByIsbn(any())).thenReturn(Optional.empty());
        when(modelMapper.map(dto, Livre.class)).thenReturn(new Livre());
        when(auteurRepository.findById(1L)).thenReturn(Optional.of(new Auteur()));
        when(categorieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> livreService.createLivre(dto));
        verify(livreRepository, never()).save(any());
    }

    @Test
    void updateLivre_champsMisAJour_retourneLivreModifie() {
        Livre livre = new Livre();
        livre.setId(1L);
        livre.setIsbn("ancien-isbn");
        livre.setTitre("Ancien titre");

        CreateLivreDTO dto = new CreateLivreDTO();
        dto.setTitre("Nouveau titre");
        dto.setIsbn("nouvel-isbn");

        LivreProjection proj = mockProjection(1L, "nouvel-isbn", "Nouveau titre");

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(livreRepository.save(any())).thenReturn(livre);
        when(livreRepository.findByIdForDetails(1L)).thenReturn(Optional.of(proj));

        LivreDTO result = livreService.updateLivre(1L, dto);

        assertEquals("Nouveau titre", result.getTitre());
        assertEquals("nouvel-isbn", livre.getIsbn());
    }

    @Test
    void updateLivre_changementCategorie_miseAJourCorrectement() {
        Livre livre = new Livre();
        livre.setId(1L);

        Categorie nouvelleCat = new Categorie();
        nouvelleCat.setId(2L);

        CreateLivreDTO dto = new CreateLivreDTO();
        dto.setCategorieId(2L);

        LivreProjection proj = mockProjection(1L, "ISBN", "Titre");

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(categorieRepository.findById(2L)).thenReturn(Optional.of(nouvelleCat));
        when(livreRepository.save(any())).thenReturn(livre);
        when(livreRepository.findByIdForDetails(1L)).thenReturn(Optional.of(proj));

        livreService.updateLivre(1L, dto);

        assertEquals(nouvelleCat, livre.getCategorie());
    }

    @Test
    void updateLivre_livreInexistant_leveOperationException() {
        when(livreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> livreService.updateLivre(99L, new CreateLivreDTO()));
    }

    @Test
    void updateLivre_categorieInexistante_leveOperationException() {
        Livre livre = new Livre();
        livre.setId(1L);

        CreateLivreDTO dto = new CreateLivreDTO();
        dto.setCategorieId(99L);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(categorieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> livreService.updateLivre(1L, dto));
        verify(livreRepository, never()).save(any());
    }

    @Test
    void updateLivre_auteurInexistant_leveOperationException() {
        Livre livre = new Livre();
        livre.setId(1L);

        CreateLivreDTO dto = new CreateLivreDTO();
        dto.setAuteurId(99L);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(auteurRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> livreService.updateLivre(1L, dto));
    }

    @Test
    void deleteLivre_sansEmpruntEnCours_supprimeLeLivre() {
        Livre livre = new Livre();
        livre.setId(1L);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(empruntRepository.existsByLivre(livre, StatutEnum.EN_COURS)).thenReturn(false);

        livreService.deleteLivre(1L);

        verify(livreRepository).deleteById(1L);
    }

    @Test
    void deleteLivre_avecEmpruntEnCours_neSupprimesPas() {
        Livre livre = new Livre();
        livre.setId(1L);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(empruntRepository.existsByLivre(livre, StatutEnum.EN_COURS)).thenReturn(true);

        livreService.deleteLivre(1L);

        verify(livreRepository, never()).deleteById(any());
    }

    @Test
    void deleteLivre_livreInexistant_leveOperationException() {
        when(livreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> livreService.deleteLivre(99L));
        verify(livreRepository, never()).deleteById(any());
    }

    @Test
    void updateCouvertureLivre_formatJpg_stockeFichierEtRetourne() {
        Livre livre = new Livre();
        livre.setId(1L);
        livre.setImageCouverture("ancienne.jpg");

        MultipartFile fichier = mock(MultipartFile.class);
        when(fichier.getOriginalFilename()).thenReturn("cover.jpg");
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(livreRepository.save(any())).thenReturn(livre);
        when(modelMapper.map(any(Livre.class), eq(LivreDTO.class))).thenReturn(new LivreDTO());

        LivreDTO result = livreService.updateCouvertureLivre(1L, fichier);

        assertNotNull(result);
        ArgumentCaptor<String> filenameCaptor = ArgumentCaptor.forClass(String.class);
        verify(storageService).store(eq(fichier), filenameCaptor.capture());
        assertTrue(filenameCaptor.getValue().endsWith(".jpg"));
        verify(storageService).delete("ancienne.jpg");
    }

    @Test
    void updateCouvertureLivre_ancienneCouvertureNull_neSupprimesPas() {
        Livre livre = new Livre();
        livre.setId(1L);
        livre.setImageCouverture(null);

        MultipartFile fichier = mock(MultipartFile.class);
        when(fichier.getOriginalFilename()).thenReturn("cover.png");
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(livreRepository.save(any())).thenReturn(livre);
        when(modelMapper.map(any(Livre.class), eq(LivreDTO.class))).thenReturn(new LivreDTO());

        livreService.updateCouvertureLivre(1L, fichier);

        verify(storageService, never()).delete(any());
    }

    @Test
    void updateCouvertureLivre_formatPdf_leveOperationException() {
        Livre livre = new Livre();
        livre.setId(1L);

        MultipartFile fichier = mock(MultipartFile.class);
        when(fichier.getOriginalFilename()).thenReturn("document.pdf");
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));

        OperationException ex = assertThrows(OperationException.class,
                () -> livreService.updateCouvertureLivre(1L, fichier));

        assertTrue(ex.getMessage().contains(".pdf"));
        verify(storageService, never()).store(any(), any());
    }

    @Test
    void updateCouvertureLivre_livreInexistant_leveOperationException() {
        when(livreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class,
                () -> livreService.updateCouvertureLivre(99L, mock(MultipartFile.class)));
    }
}
