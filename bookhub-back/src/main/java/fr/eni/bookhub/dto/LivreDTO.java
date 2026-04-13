package fr.eni.bookhub.dto;

import fr.eni.bookhub.entity.Auteur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivreDTO {
    private Long id;
    private String isbn;
    private String titre;
    private String resume;
    private String imageCouverture;
    private Integer nbPage;
    private AuteurDTO auteur;
    private CategorieDTO categorie;
}
