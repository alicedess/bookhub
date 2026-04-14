package fr.eni.bookhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class ProfilDTO {

    private String pseudo;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    private String password;

    @NotBlank
    private String telephone;
}
