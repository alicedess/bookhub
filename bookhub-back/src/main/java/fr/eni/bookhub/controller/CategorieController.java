package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.CategorieDTO;
import fr.eni.bookhub.service.CategorieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Récupérer toutes les catégories", description = "Retourne une liste complète des catégories disponibles en base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategorieDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content)
    })
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
