package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.ProfilDTO;
import fr.eni.bookhub.dto.UpdateProfilDTO;
import fr.eni.bookhub.dto.UtilisateurDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UtilisteurService utilisteurService;

    @Test
    @DisplayName("Test récupération d'un utilisateur actif par ID")
    public void test_getUtilisateurActif() {
        // Utilisateur ID 1 existe dans data.sql
        UtilisateurDTO result = utilisteurService.getUtilisateurActif(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("admin@bookhub.fr", result.getEmail());
        Assertions.assertEquals("nouveau_pseudo", result.getPseudo());
    }

    @Test
    @DisplayName("Test récupération d'un utilisateur actif avec ID inexistant")
    public void test_getUtilisateurActif_UserNotFound() {
        // ID 999 n'existe pas
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            utilisteurService.getUtilisateurActif(999L);
        });
        Assertions.assertEquals("Utilisateur non trouvé ou supprimé", exception.getMessage());
    }

    @Test
    @DisplayName("Test mise à jour du profil utilisateur (champs non vides)")
    @WithMockUser(username = "admin@bookhub.fr")
    public void test_updateUtilisateur() {
        UpdateProfilDTO updateDto = new UpdateProfilDTO();
        ProfilDTO profil = new ProfilDTO();
        profil.setPseudo("nouveau_pseudo");
        profil.setNom("NouveauNom");
        profil.setPrenom("NouveauPrenom");
        profil.setTelephone("0123456789");
        updateDto.setProfil(profil);

        ProfilDTO result = utilisteurService.updateUtilisateur(1L, updateDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("nouveau_pseudo", result.getPseudo());
        Assertions.assertEquals("NouveauNom", result.getNom());
        Assertions.assertEquals("NouveauPrenom", result.getPrenom());
        Assertions.assertEquals("0123456789", result.getTelephone());
    }

    /*@Test
    @DisplayName("Test mise à jour du mot de passe avec ancien mot de passe correct")
    @WithMockUser(username = "admin@bookhub.fr")
    public void test_updateUtilisateur_ChangePassword() {
        UpdateProfilDTO updateDto = new UpdateProfilDTO();
        ProfilDTO profil = new ProfilDTO();
        profil.setPassword("nouveau_password");
        updateDto.setProfil(profil);
        updateDto.setOldPassword("mon_super_password"); // Mot de passe hashé dans data.sql correspond

        ProfilDTO result = utilisteurService.updateUtilisateur(1L, updateDto);

        Assertions.assertNotNull(result);
        // Le mot de passe est hashé, on ne peut pas vérifier directement la valeur
    }

    @Test
    @DisplayName("Test mise à jour du mot de passe avec ancien mot de passe incorrect")
    @WithMockUser(username = "admin@bookhub.fr")
    public void test_updateUtilisateur_ChangePassword_WrongOldPassword() {
        UpdateProfilDTO updateDto = new UpdateProfilDTO();
        ProfilDTO profil = new ProfilDTO();
        profil.setPassword("nouveau_password");
        updateDto.setProfil(profil);
        updateDto.setOldPassword("ancien_password");

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            utilisteurService.updateUtilisateur(2L, updateDto);
        });
        Assertions.assertEquals("L'ancien mot de passe est incorrect", exception.getMessage());
    }

    @Test
    @DisplayName("Test mise à jour du mot de passe sans fournir l'ancien mot de passe")
    @WithMockUser(username = "admin@bookhub.fr")
    public void test_updateUtilisateur_ChangePassword_NoOldPassword() {
        UpdateProfilDTO updateDto = new UpdateProfilDTO();
        ProfilDTO profil = new ProfilDTO();
        profil.setPassword("nouveau_password");
        updateDto.setProfil(profil);
        // oldPassword est null

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            utilisteurService.updateUtilisateur(1L, updateDto);
        });
        Assertions.assertEquals("L'ancien mot de passe est requis pour changer le mot de passe", exception.getMessage());
    }*/

    @Test
    @DisplayName("Test récupération d'un utilisateur par email")
    public void test_getUtilisateurParEmail() {
        UtilisateurDTO result = utilisteurService.getUtilisateurParEmail("admin@bookhub.fr");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("admin@bookhub.fr", result.getEmail());
        Assertions.assertEquals("nouveau_pseudo", result.getPseudo());
    }

    @Test
    @DisplayName("Test récupération d'un utilisateur par email inexistant")
    public void test_getUtilisateurParEmail_UserNotFound() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            utilisteurService.getUtilisateurParEmail("inexistant@example.com");
        });
        Assertions.assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    @Test
    @DisplayName("Test suppression d'un utilisateur par email")
    @WithMockUser(username = "emma.l@test.com")
    public void test_deleteUtilisateur() {
        Assertions.assertDoesNotThrow(() -> {
            utilisteurService.softDeleteUtilisateur("emma.l@test.com");
        });

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            utilisteurService.getUtilisateurActif(7L);
        });
        Assertions.assertEquals("Utilisateur non trouvé ou supprimé", exception.getMessage());
    }

    @Test
    @DisplayName("Test suppression d'un utilisateur avec email inexistant")
    public void test_deleteUtilisateur_UserNotFound() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            utilisteurService.deleteUtilisateur("inexistant@example.com");
        });
        Assertions.assertEquals("Utilisateur non trouvé", exception.getMessage());
    }
}
