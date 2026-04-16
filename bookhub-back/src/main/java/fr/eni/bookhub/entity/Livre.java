package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "livre")
@Data
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "isbn", length = 13, nullable = false, unique = true)
    private String isbn;

    @Column(name = "titre", nullable = false, length = 255)
    private String titre;

    @Column(name = "resume", length = 255)
    private String resume;

    @Column(name = "image_couverture", length = 255)
    private String imageCouverture;

    @Column(name = "nb_page")
    private Integer nbPage;

    @Column(name = "date_parution")
    private LocalDate dateParution;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_auteur")
    private Auteur auteur;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;
}
