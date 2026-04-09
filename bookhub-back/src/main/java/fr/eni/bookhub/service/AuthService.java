package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.LoginRequest;
import fr.eni.bookhub.dto.LoginDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private UtilisateurRepository utilisateurRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    public LoginDTO login(LoginRequest loginRequest) {
        Utilisateur user = utilisateurRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        if (user.getDateSuppression() != null) {
            throw new RuntimeException("Cet utilisateur a été supprimé");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId());

        return new LoginDTO(
                token,
                user.getId(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                "Connexion réussie"
        );
    }

    public Utilisateur createUser(Utilisateur user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return utilisateurRepository.save(user);
    }

    public void logout() {
        // La déconnexion est gérée côté client en supprimant le token JWT
    }
}

