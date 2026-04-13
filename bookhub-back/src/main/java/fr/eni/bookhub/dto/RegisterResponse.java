package fr.eni.bookhub.dto;

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
    private String role;
    private String message;
}
