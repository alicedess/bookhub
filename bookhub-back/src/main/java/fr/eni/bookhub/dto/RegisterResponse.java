package fr.eni.bookhub.dto;

import fr.eni.bookhub.enumeration.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private RoleEnum role;
    private String message;
}
