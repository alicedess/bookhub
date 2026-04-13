package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.AuthDTO;
import fr.eni.bookhub.dto.LoginDTO;
import fr.eni.bookhub.dto.LoginResponse;
import fr.eni.bookhub.dto.RegisterResponse;
import fr.eni.bookhub.dto.UtilisateurDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.RoleRepository;
import fr.eni.bookhub.repository.UtilisateurRepository;
import fr.eni.bookhub.security.JwtUtil;
import fr.eni.bookhub.mapper.UtilisateurMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private UtilisateurRepository utilisateurRepository;
    private RoleRepository roleRepository;
    private UtilisateurMapper utilisateurMapper;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private RoleRepository roleRepository;

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

    public RegisterResponse createUser(UtilisateurDTO utilisateurDTO) {
        // 1. Vérifier que l'email n'existe pas déjà
        if (utilisateurRepository.findByEmailAndDateSuppressionIsNull(utilisateurDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        // 2. Créer l'entité Utilisateur à partir du DTO
        Utilisateur utilisateur = utilisateurMapper.convertToEntity(utilisateurDTO);

        // 3. Encoder le mot de passe
        utilisateur.setPassword(passwordEncoder.encode(utilisateurDTO.getPassword()));

        // 4. Assigner un rôle par défaut (USER/LECTEUR)
        utilisateur.setRole(roleRepository.findByLibelle("USER")
                .orElseThrow(() -> new RuntimeException("Rôle par défaut non trouvé")));

        // 5. Sauvegarder l'utilisateur
        Utilisateur nouvelUtilisateur = utilisateurRepository.save(utilisateur);

        // 6. Retourner une réponse appropriée
        return new RegisterResponse(
                nouvelUtilisateur.getId(),
                nouvelUtilisateur.getEmail(),
                nouvelUtilisateur.getNom(),
                nouvelUtilisateur.getPrenom(),
                nouvelUtilisateur.getRole().getLibelle(),
                "Inscription réussie"
        );
    }

    public void logout() {
        // La déconnexion est gérée côté client en supprimant le token JWT
    }
}

