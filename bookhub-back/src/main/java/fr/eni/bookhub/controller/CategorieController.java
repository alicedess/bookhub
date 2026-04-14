package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.CategorieDTO;
import fr.eni.bookhub.service.CategorieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
@Tag(name = "Catégorie", description = "Gestion et Affichage des catégories")
public class CategorieController {

    private CategorieService categorieService;

    @GetMapping("")
    public ResponseEntity<?> getCategories(){
        try {
            Iterable<CategorieDTO> categories = categorieService.findAll();

            return ResponseEntity.ok().body(categories);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
