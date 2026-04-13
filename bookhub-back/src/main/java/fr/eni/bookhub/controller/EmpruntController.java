package fr.eni.bookhub.controller;

import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.service.EmpruntService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/loans")
@AllArgsConstructor
public class EmpruntController {

    private EmpruntService empruntService;

    @PostMapping
    public ResponseEntity<?> emprunterLivre(
            @RequestParam Long userId,
            @RequestParam Long exemplaireId) {
        try {
            Emprunt emprunt = empruntService.emprunterLivre(userId, exemplaireId);
            return ResponseEntity.ok()
                    .body("Emprunt confirmé ! Retour prévu le " +
                            emprunt.getDateRetourPrevue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Erreur : " + e.getMessage());
        }
    }


}
