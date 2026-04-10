package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.CreateLivreDTO;
import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.service.LivreService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class LivreController {

    private LivreService livreService;

    /**
     * Récupère la liste des livres paginée par 20
     */
    @GetMapping({"", "/search"})
    public ResponseEntity<?> searchLivres(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) Long auteurId,
        @RequestParam(required = false) Long catId,
        @RequestParam(defaultValue = "0") int page
    )
    {
        try {
            Page<LivreDTO> livres = livreService.searchLivres(
                    query,
                    auteurId,
                    catId,
                    page
            );

            return ResponseEntity.ok().body(livres);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    /**
     * Récupère les données d'un livre
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getLivreById(@PathVariable Long id)
    {
        try {
            return livreService.getById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    /**
     *  Création d'un nouveau livre
     */
    @PostMapping("")
    public ResponseEntity<?> createLivre(@RequestBody CreateLivreDTO payload)
    {
        try {
            if (livreService.createLivre(payload)) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }

            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    /**
     * Modification d'une livre
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLivre(@PathVariable Long id, @RequestBody CreateLivreDTO payload)
    {
        try {
            if (livreService.updateLivre(id, payload)) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }

            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    /**
     * Suppression d'un livre
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLivre(@PathVariable Long id)
    {
        try {
            livreService.deleteLivre(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
