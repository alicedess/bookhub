package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.AuthDTO;
import fr.eni.bookhub.dto.LoginDTO;
import fr.eni.bookhub.dto.RegisterResponse;
import fr.eni.bookhub.dto.UtilisateurDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.security.JwtUtil;
import fr.eni.bookhub.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthentificationController {

    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO loginRequest) {
        try {
            return ResponseEntity.ok(authService.login(loginRequest));
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
    public ResponseEntity<?> register(@RequestBody UtilisateurDTO utilisateur) {
        try {
            RegisterResponse response = authService.createUser(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de l'inscription"));
        }
    }
}
