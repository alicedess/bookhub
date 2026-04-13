package fr.eni.bookhub.dto;

import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Categorie;
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
    private Long auteurId;
    private String auteurNom;
    private String auteurPrenom;
    private Long categorieId;
    private String categorieLibelle;
    private Long nbExemplaire;
    private Long nbExemplaireDispo;
    private Double note;
}
