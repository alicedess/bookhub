package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.UtilisateurDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.service.UtilisteurService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/api/user")
@AllArgsConstructor
public class UtilisateurController {

    private UtilisteurService utilisteurService;

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable Long id){
        UtilisateurDTO utilisateur = utilisteurService.getUtilisateurActif(id);
        return ResponseEntity.ok().body(utilisateur);
    }
}
