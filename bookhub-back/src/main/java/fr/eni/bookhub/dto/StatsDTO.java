package fr.eni.bookhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Statistiques globales de l'application")
public class StatsDTO {

    @Schema(description = "Nombre d'utilisateurs actifs (non supprimés)", example = "42")
    private long nombreUtilisateurs;

    @Schema(description = "Nombre total de livres référencés", example = "150")
    private long nombreLivres;

    @Schema(description = "Nombre d'emprunts actuellement en cours (statut EN_COURS)", example = "27")
    private long nombreEmpruntsEnCours;

    @Schema(description = "Nombre d'emprunts en cours dont la date de retour prévue est dépassée", example = "5")
    private long nombreEmpruntsEnRetard;
}
