package fr.eni.bookhub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateParution;
    private Long auteurId;
    private String auteurNom;
    private String auteurPrenom;
    private Long categorieId;
    private String categorieLibelle;
    private Long nbExemplaire;
    private Long nbExemplaireDispo;
    private Double note;
}
