package fr.eni.bookhub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LivreDTO {
    private Long id;
    private String isbn;
    private String titre;
    private String resume;
    private String imageCouverture;
    private Integer nbPage;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateParution;
    private Long auteurId;
    private String auteurNom;
    private String auteurPrenom;
    private Long categorieId;
    private String categorieLibelle;
    private Long nbExemplaire;
    private Long nbExemplaireDispo;
    private Double note;

    public LivreDTO(Long id, String isbn, String titre, String resume, String imageCouverture,
                    Integer nbPage, LocalDate dateParution, Long auteurId, String auteurNom,
                    String auteurPrenom, Long categorieId, String categorieLibelle,
                    Long nbExemplaires, Long nbExemplaireDispo, Double note) {
        this.id = id;
        this.isbn = isbn;
        this.titre = titre;
        this.resume = resume;
        this.imageCouverture = imageCouverture;
        this.nbPage = nbPage;
        this.dateParution = dateParution;
        this.auteurId = auteurId;
        this.auteurNom = auteurNom;
        this.auteurPrenom = auteurPrenom;
        this.categorieId = categorieId;
        this.categorieLibelle = categorieLibelle;
        this.nbExemplaire = nbExemplaires;
        this.nbExemplaireDispo = nbExemplaireDispo;
        this.note = note;
    }
}
