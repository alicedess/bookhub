package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "LIVRES")
@Data
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String isbn;

    @NotNull
    private String titre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auteur")
    private Auteur auteur;

    private String resume;

    private String imageCouverture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;
}
