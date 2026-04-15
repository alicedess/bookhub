package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.AuteurDTO;
import fr.eni.bookhub.service.AuteurService;
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
