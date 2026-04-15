package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.EmpruntDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.service.EmpruntService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@AllArgsConstructor
@Tag(name = "Emprunt", description = "Gestion des emprunts de livres")
public class EmpruntController {

    private final EmpruntService empruntService;

    @PostMapping
    @Operation(summary = "Emprunter un livre",
            description = "Permet à un utilisateur d'emprunter un exemplaire disponible d'un livre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Emprunt créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur métier (non disponible, limite atteinte, etc.)"),
    })
    public ResponseEntity<?> emprunterLivre(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestParam Long idLivre) {
        try {
            EmpruntDTO emprunt = empruntService.emprunterLivreDto(utilisateur.getId(), idLivre);
            return ResponseEntity.status(201)
                    .body(Map.of(
                            "message", "Emprunt créé avec succès ! Retour prévu le " +
                                    emprunt.getDateRetourPrevue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            "emprunt", emprunt
                    ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/my")
    @Operation(
            summary = "Mes emprunts",
            description = "Affiche la liste des emprunts en cours et de l'historique des emprunts de l'utilisateur connecté."
    )
    public ResponseEntity<Map<String, List<EmpruntDTO>>> mesEmprunts(@AuthenticationPrincipal Utilisateur utilisateur) {
        List<EmpruntDTO> empruntsEnCours = empruntService.getEmpruntsEnCoursDTO(utilisateur);
        List<EmpruntDTO> empruntsHistoriques = empruntService.getEmpruntsHistoriqueDTO(utilisateur);

        Map<String, List<EmpruntDTO>> response = new HashMap<>();
        response.put("enCours", empruntsEnCours);
        response.put("historique", empruntsHistoriques);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    @Operation(
            summary = "Tous les emprunts",
            description = "Affiche la liste de tous les emprunts avec pagination et tri pour les bibliothécaires."
    )
    public ResponseEntity<Page<EmpruntDTO>> tousLesEmprunts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "dateDebut") String sortBy) {
        return ResponseEntity.ok(empruntService.getAllEmpruntsDTO(page, size, sortBy));
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @Operation(
            summary = "Retourner un livre",
            description = "Permet à un bibliothécaire d'enregistrer le retour d'un livre emprunté, " +
                    "mettant à jour le statut de l'emprunt et la disponibilité de l'exemplaire."
    )
    public ResponseEntity<?> retournerLivre(@PathVariable Long id) {
        try {
            EmpruntDTO emprunt = empruntService.retournerLivreDTO(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Retour enregistré avec succès !",
                    "emprunt", emprunt
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Erreur : " + e.getMessage());
        }
    }
}