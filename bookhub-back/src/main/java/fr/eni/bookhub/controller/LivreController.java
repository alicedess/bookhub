package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.CreateLivreDTO;
import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.dto.ExemplaireDTO;
import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.service.EvaluationService;
import fr.eni.bookhub.service.ExemplaireService;
import fr.eni.bookhub.service.LivreService;
import fr.eni.bookhub.storage.StorageFileNotFoundException;
import fr.eni.bookhub.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
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
    private ExemplaireService exemplaireService;

    @Operation(
            summary = "Rechercher des livres",
            description = "Retourne une page de livres filtrée par mot-clé (titre, auteur, ISBN), auteur, catégorie et/ou disponibilité. 20 résultats par page, triés par titre."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste paginée de livres",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @GetMapping({"", "/search"})
    public ResponseEntity<Page<LivreDTO>> searchLivres(
        @Parameter(description = "Texte recherché dans le titre, le nom/prénom de l'auteur ou l'ISBN")
        @RequestParam(required = false) String query,
        @Parameter(description = "Identifiant de l'auteur pour filtrer les résultats")
        @RequestParam(required = false) Long auteurId,
        @Parameter(description = "Identifiant de la catégorie pour filtrer les résultats")
        @RequestParam(required = false, name = "categorie") Long catId,
        @Parameter(description = "Filtre de disponibilité : 0 = tous, 1 = disponibles uniquement, 2 = indisponibles uniquement",
                schema = @Schema(allowableValues = {"0", "1", "2"}, defaultValue = "0"))
        @RequestParam(required = false, name = "disponibilite") Integer estDisponible,
        @Parameter(description = "Numéro de la page (commence à 0)")
        @RequestParam(defaultValue = "0") int page
    )
    {
        return ResponseEntity.ok().body(livreService.searchLivres(
                query,
                auteurId,
                catId,
                estDisponible,
                page
        ));
    }

    @Operation(
            summary = "Récupérer un livre par son identifiant",
            description = "Retourne le détail complet d'un livre (auteur, catégorie, nombre d'exemplaires, note moyenne)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre trouvé",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LivreDTO.class))),
            @ApiResponse(responseCode = "404", description = "Livre introuvable", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<LivreDTO> getLivreById(
            @Parameter(description = "Identifiant du livre", required = true)
            @PathVariable Long id)
    {
        return livreService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Créer un livre",
            description = "Crée un nouveau livre. L'ISBN doit être unique. Requiert le rôle LIBRARIAN ou ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livre créé avec succès",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LivreDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides ou ISBN déjà existant", content = @Content),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
            @ApiResponse(responseCode = "403", description = "Droits insuffisants", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @PostMapping("")
    public ResponseEntity<?> createLivre(@Valid @RequestBody CreateLivreDTO payload)
    {
        LivreDTO createdLivre = livreService.createLivre(payload);

        if (null != createdLivre) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLivre);
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(
            summary = "Modifier un livre",
            description = "Met à jour les champs d'un livre existant. Seuls les champs fournis sont modifiés. Requiert le rôle LIBRARIAN ou ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre mis à jour",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LivreDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
            @ApiResponse(responseCode = "403", description = "Droits insuffisants", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livre introuvable", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<LivreDTO> updateLivre(
            @Parameter(description = "Identifiant du livre", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CreateLivreDTO payload)
    {
        LivreDTO updatedLivre = livreService.updateLivre(id, payload);

        if (null != updatedLivre) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedLivre);
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(
            summary = "Supprimer un livre",
            description = "Supprime un livre uniquement s'il n'a aucun emprunt en cours. Requiert le rôle ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livre supprimé", content = @Content),
            @ApiResponse(responseCode = "400", description = "Suppression impossible (emprunts en cours)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
            @ApiResponse(responseCode = "403", description = "Droits insuffisants", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livre introuvable", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLivre(
            @Parameter(description = "Identifiant du livre", required = true)
            @PathVariable Long id)
    {
        livreService.deleteLivre(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Mettre à jour la couverture d'un livre",
            description = "Téléverse une nouvelle image de couverture (JPG, JPEG ou PNG). Remplace l'image précédente si elle existe. Requiert le rôle LIBRARIAN ou ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Couverture mise à jour, livre retourné",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LivreDTO.class))),
            @ApiResponse(responseCode = "400", description = "Format de fichier non supporté ou livre introuvable", content = @Content),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
            @ApiResponse(responseCode = "403", description = "Droits insuffisants", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @PutMapping("/{id}/cover")
    public ResponseEntity<LivreDTO> handleFileUpload(
            @Parameter(description = "Identifiant du livre", required = true)
            @PathVariable Long id,
            @Parameter(description = "Fichier image (JPG, JPEG ou PNG)", required = true)
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes)
    {
        LivreDTO livre = livreService.updateCouvertureLivre(id, file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return ResponseEntity.ok().body(livre);
    }

    @Operation(
            summary = "Récupérer la couverture d'un livre",
            description = "Retourne l'image de couverture du livre au format JPEG/PNG. Retourne la couverture par défaut si aucune image n'est associée."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image de couverture",
                    content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "404", description = "Livre introuvable", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @GetMapping("/{id}/cover")
    public ResponseEntity<?> getImage(
            @Parameter(description = "Identifiant du livre", required = true)
            @PathVariable Long id) throws Exception {
        Optional<LivreDTO> livre = livreService.getById(id);

        if (livre.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        LivreDTO livreDTO = livre.get();

        if (null != livreDTO.getImageCouverture()) {
            try {
                Resource resource = storageService.loadAsResource(livreDTO.getImageCouverture());

                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } catch (StorageFileNotFoundException e) {
            }
        }

        File file = ResourceUtils.getFile("classpath:assets/default_cover.png");
        Resource resource = storageService.loadAsResource(file.toPath());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @Operation(
            summary = "Ajouter une évaluation",
            description = "Publie une note et/ou un commentaire sur un livre. Requiert d'être authentifié.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Évaluation enregistrée",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EvaluationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Impossible d'évaluer ce livre", content = @Content),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livre introuvable", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> addRating(
            @Parameter(description = "Identifiant du livre", required = true)
            @PathVariable Long id,
            @RequestBody EvaluationDTO payload) {
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

    @Operation(
            summary = "Lister les évaluations d'un livre",
            description = "Retourne toutes les évaluations publiées et modérées associées à un livre."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des évaluations",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = EvaluationDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Impossible de récupérer les évaluations", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livre introuvable", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @GetMapping("/{id}/ratings")
    public ResponseEntity<?> lesEvaluationsParLivre(
            @Parameter(description = "Identifiant du livre", required = true)
            @PathVariable Long id) {
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

    @Operation(
            summary = "Lister les exemplaires d'un livre",
            description = "Retourne tous les exemplaires associés à un livre avec leur état et leur disponibilité.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des exemplaires",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ExemplaireDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
            @ApiResponse(responseCode = "403", description = "Droits insuffisants", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livre introuvable", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @GetMapping("/{id}/exemplaires")
    public ResponseEntity<List<ExemplaireDTO>> getExemplaires(
            @Parameter(description = "Identifiant du livre", required = true)
            @PathVariable Long id)
    {
        return ResponseEntity.ok().body(exemplaireService.getExemplairesParLivreId(id));
    }

    @Operation(
            summary = "Mettre à jour les exemplaires d'un livre",
            description = "Crée ou modifie les exemplaires d'un livre (code-barre, état, disponibilité). Requiert le rôle LIBRARIAN ou ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exemplaires mis à jour", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
            @ApiResponse(responseCode = "403", description = "Droits insuffisants", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livre introuvable", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    @PostMapping("/{id}/exemplaires")
    public ResponseEntity<?> updateExemplaires(
            @Parameter(description = "Identifiant du livre", required = true)
            @PathVariable Long id,
            @Valid @RequestBody List<ExemplaireDTO> payload)
    {
        exemplaireService.updateExemplairesParLivreId(id,  payload);

        return ResponseEntity.ok().build();
    }
}
