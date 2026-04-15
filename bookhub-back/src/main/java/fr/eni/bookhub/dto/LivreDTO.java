package fr.eni.bookhub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class LivreDTO {
    private Long id;                // l.id
    private String isbn;            // l.isbn
    private String titre;           // l.titre
    private String resume;          // l.resume
    private String imageCouverture; // l.imageCouverture
    private Long nbPage;         // l.nbPage
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateParution; // l.dateParution
    private Long auteurId;          // l.auteur.id
    private String auteurNom;       // l.auteur.nom
    private String auteurPrenom;    // l.auteur.prenom
    private Long categorieId;       // l.categorie.id
    private String categorieLibelle;// l.categorie.libelle
    private Long nbExemplaire;     // COUNT AS Long
    private Long nbExemplaireDispo;     // COUNT AS Long
    private Double note;                 // AVG AS Float

    public LivreDTO(Long id, String isbn, String titre, String resume, String imageCouverture,
                    Long nbPage, Date dateParution, Long auteurId, String auteurNom,
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
