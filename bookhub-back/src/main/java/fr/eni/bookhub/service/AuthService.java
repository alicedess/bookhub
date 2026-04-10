package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.AuthDTO;
import fr.eni.bookhub.dto.LoginDTO;
import fr.eni.bookhub.dto.LoginResponse;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.UtilisateurRepository;
import fr.eni.bookhub.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {

    private UtilisateurRepository utilisateurRepository;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public LoginResponse login(AuthDTO loginRequest) {
        // 1. On prépare la demande d'authentification
        Authentication authenticationRequest =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // 2. L'AuthenticationManager vérifie le login/password (appelle le UserDetailsService + BCrypt)
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        Utilisateur userPrincipal = (Utilisateur) authenticationResponse.getPrincipal();

        // 3. On génère le Token JWT à partir de l'authentification réussie
        String jwt = jwtUtil.generateJwtToken(authenticationResponse);

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setEmail(userPrincipal.getEmail());
        response.setPrenom(userPrincipal.getPrenom());
        response.setNom(userPrincipal.getNom());
        response.setRole(userPrincipal.getRole().getLibelle());

        return response;
    }

    public Utilisateur createUser(Utilisateur user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return utilisateurRepository.save(user);
    }

    public void logout() {
        // La déconnexion est gérée côté client en supprimant le token JWT
    }
}

