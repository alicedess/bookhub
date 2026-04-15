package fr.eni.bookhub.dto;

import lombok.Data;

@Data
public class UpdateProfilDTO {
    private ProfilDTO profil;
    private String oldPassword;
}