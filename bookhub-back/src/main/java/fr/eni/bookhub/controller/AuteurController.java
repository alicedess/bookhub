package fr.eni.bookhub.controller;

import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.service.AuteurService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController("/api/author")
@AllArgsConstructor
public class AuteurController {

    private AuteurService auteurService;

    @GetMapping("/all")
    public ResponseEntity<List<Auteur>> getAuteurs(){
        List<Auteur> allAuteur = auteurService.getAllAuteur();
        return ResponseEntity.ok().body(allAuteur);
    }
}
