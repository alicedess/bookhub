package fr.eni.bookhub.controller;

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
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Integer id){
        Utilisateur utilisateur = utilisteurService.getUtilisateurById(id);
        return ResponseEntity.ok().body(utilisateur);
    }

}
