package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.AuteurDTO;
import fr.eni.bookhub.service.AuteurService;
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
@RequestMapping("/api/authors")
@AllArgsConstructor
@Tag(name = "Auteur", description = "Gestion et Affichage des auteurs")
public class AuteurController {

    private AuteurService auteurService;

    @Operation(summary = "Récupérer tous les auteurs", description = "Retourne une liste complète des auteurs disponibles en base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuteurDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content)
    })
    @GetMapping("")
    public ResponseEntity<?> getAuteurs(){
        try {
            Iterable<AuteurDTO> auteurs = auteurService.findAll();

            return ResponseEntity.ok().body(auteurs);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
