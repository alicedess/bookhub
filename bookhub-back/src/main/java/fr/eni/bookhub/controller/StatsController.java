package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.StatsDTO;
import fr.eni.bookhub.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@AllArgsConstructor
@Tag(name = "Statistiques", description = "Tableau de bord — métriques globales de l'application")
@SecurityRequirement(name = "bearerAuth")
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(
            summary = "Statistiques globales",
            description = """
                    Retourne les métriques clés de l'application :
                    - nombre d'utilisateurs actifs,
                    - nombre de livres référencés,
                    - nombre d'emprunts en cours,
                    - nombre d'emprunts en retard (date de retour dépassée).

                    Accessible uniquement aux rôles **ADMIN** et **LIBRARIAN**.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Statistiques récupérées avec succès",
                    content = @Content(schema = @Schema(implementation = StatsDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Token JWT manquant ou invalide", content = @Content),
            @ApiResponse(responseCode = "403", description = "Accès refusé — rôle ADMIN ou LIBRARIAN requis", content = @Content)
    })
    public ResponseEntity<StatsDTO> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
