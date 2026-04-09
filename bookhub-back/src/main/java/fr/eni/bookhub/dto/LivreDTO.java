package fr.eni.bookhub.dto;

import lombok.Data;

@Data
public class LivreDTO {
    private Long id;

    private String isbn;

    private String titre;

    private String resume;

    private String imageCouverture;
}
