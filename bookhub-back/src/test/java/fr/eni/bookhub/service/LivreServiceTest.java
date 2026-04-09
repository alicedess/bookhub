package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.CreateLivreDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LivreServiceTest {

    @Autowired
    private LivreService livreService;

    @Test
    @DisplayName("Test création d'un livre")
    public void test_createLivre()
    {
        CreateLivreDTO dto = new CreateLivreDTO();
        dto.setIsbn("123456789");
        dto.setTitre("Titre Livre");
        dto.setAuteurId(1);
        dto.setCategorieId(1L);
        dto.setResume("Resume Livre");
        dto.setImageCouverture("Image Livre");

        Boolean result = livreService.createLivre(dto);
        Assertions.assertTrue(result);
    }

}
