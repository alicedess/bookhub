package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.CreateLivreDTO;
import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.exception.OperationException;
import fr.eni.bookhub.service.EvaluationService;
import fr.eni.bookhub.service.LivreService;
import fr.eni.bookhub.storage.StorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
@Tag(name = "Livre", description = "Gestion et Affichage des livres")
public class LivreController {

    private LivreService livreService;
    private StorageService storageService;
    private EvaluationService evaluationService;

    /**
     * Récupère la liste des livres paginée par 20
     */
    @GetMapping({"", "/search"})
    public ResponseEntity<?> searchLivres(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) Long auteurId,
        @RequestParam(required = false, name = "categorie") Long catId,
        @RequestParam(defaultValue = "0") int page
    )
    {
        try {
            return ResponseEntity.ok().body(livreService.searchLivres(
                    query,
                    auteurId,
                    catId,
                    page
            ));
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
        } catch (OperationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Impossible de créer ce livre"
            ));
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
        } catch (OperationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Impossible de modifier ce livre"
            ));
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
        } catch (OperationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Impossible de supprimer ce livre"
            ));
        }
    }

    /**
     * Upload de la couverture du Livre.
     */
    @PutMapping("/{id}/cover")
    public ResponseEntity<?> handleFileUpload(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes)
    {
        try {
            LivreDTO livre = livreService.updateCouvertureLivre(id, file);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");

            return ResponseEntity.ok().body(livre);
        } catch (OperationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Impossible de modifier la couverture du livre"
            ));
        }
    }

    /**
     * Récupération de la couverture du livre
     */
    @GetMapping("/{id}/cover")
    public ResponseEntity<?> getImage(@PathVariable Long id) throws Exception {
        try {
            Optional<LivreDTO> livre = livreService.getById(id);

            if (livre.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            LivreDTO livreDTO = livre.get();

            if (null == livreDTO.getImageCouverture()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = storageService.loadAsResource(livreDTO.getImageCouverture());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (OperationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Impossible de récupérer la couverture du livre"
            ));
        }
    }

    /**
     * Ajoute une évaluation
     * @param id id du livre
     * @param payload le commentaire et la note de l'évaluation
     * @return ok si l'évaluation a été ajoutée, sinon une erreur
     */
    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> addRating(@PathVariable Long id, @RequestBody EvaluationDTO payload){
        try{
            EvaluationDTO ajoutEval = evaluationService.createEvaluation(id, payload);
            return ResponseEntity.ok(ajoutEval);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Impossible de noter ce livre"
            ));
        }
    }

    /**
     * Les évaluations d'un livre
     * @param id Id du livre
     * @return La liste des évaluations du livre
     */
    @GetMapping("/{id}/ratings")
    public ResponseEntity<?> lesEvaluationsParLivre(@PathVariable Long id){
        try{
            List<EvaluationDTO> evaluations = evaluationService.getEvaluationsParLivre(id);
            return ResponseEntity.ok(evaluations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Impossible de récupérer les évaluations de ce livre"
            ));
        }
    }
}
