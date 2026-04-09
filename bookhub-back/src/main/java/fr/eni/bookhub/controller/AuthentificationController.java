package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.LoginDTO;
import fr.eni.bookhub.dto.LoginRequest;
import fr.eni.bookhub.dto.LoginDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthentificationController {

    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginDTO response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur newUser = authService.createUser(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur créé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
