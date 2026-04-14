package fr.eni.bookhub.dto;

import fr.eni.bookhub.entity.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UtilisateurDTO {
    private Long id;
    private String email;
    private String password;
    private String nom;
    private String prenom;
    private String pseudo;
    private Integer telephone;
    private Role role;
    private Date dateNaissance;
    private Date dateSuppression;
}
