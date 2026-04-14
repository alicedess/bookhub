package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.ProfilDTO;
import fr.eni.bookhub.dto.UpdateProfilDTO;
import fr.eni.bookhub.dto.UtilisateurDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.service.UtilisteurService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UtilisateurController {

    private UtilisteurService utilisteurService;

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable Long id){
        try {
            UtilisateurDTO utilisateur = utilisteurService.getUtilisateurActif(id);
            return ResponseEntity.ok().body(utilisateur);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfilDTO> updateUtilisateur(@PathVariable Long id, @RequestBody UpdateProfilDTO request) {
        try{
            ProfilDTO updatedUtilisateur = utilisteurService.updateUtilisateur(id, request);
            return ResponseEntity.ok().body(updatedUtilisateur);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
