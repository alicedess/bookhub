package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.service.ILivreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class LivreController {

    private ILivreService livreService;

    @GetMapping("")
    public ResponseEntity<?> getAll()
    {
        try {
            Iterable<LivreDTO> livres = livreService.getAll();

            return ResponseEntity.ok().body(livres);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
