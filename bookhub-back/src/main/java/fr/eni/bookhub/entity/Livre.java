package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "LIVRE")
@Data
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 13, nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String titre;

    private String resume;

    private String imageCouverture;

    private Integer nbPage;

    @DateTimeFormat
    @Column(name = "date_parution")
    private Date dateParution;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_auteur")
    private Auteur auteur;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;
}
