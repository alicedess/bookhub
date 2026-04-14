package fr.eni.bookhub.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProfilDTO {
    private String pseudo;
    private String nom;
    private String prenom;
    private String password;
    private String telephone;
    private Date dateNaissance;
}
