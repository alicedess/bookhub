package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.EmpruntDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.service.EmpruntService;
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
public class EmpruntController {

    private final EmpruntService empruntService;

    @PostMapping
    public ResponseEntity<?> emprunterLivre(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestParam Long idLivre) {
        try {
            EmpruntDTO emprunt = empruntService.emprunterLivreDto(utilisateur.getId(), idLivre);
            return ResponseEntity.status(201)
                    .body(Map.of(
                            "message", "Emprunt confirmé ! Retour prévu le " +
                                    emprunt.getDateRetourPrevue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            "emprunt", emprunt
                    ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/my")
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
    public ResponseEntity<Page<EmpruntDTO>> tousLesEmprunts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "dateDebut") String sortBy) {
        return ResponseEntity.ok(empruntService.getAllEmpruntsDTO(page, size, sortBy));
    }
}