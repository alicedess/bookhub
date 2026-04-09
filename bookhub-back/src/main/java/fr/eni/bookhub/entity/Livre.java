package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "LIVRES")
@Data
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String isbn;

    @NotNull
    private String titre;

    @ManyToOne
    @JoinColumn(name = "ID_AUTEUR")
    private Auteur auteur;

    private String resume;

    private String imageCouverture;

    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIE")
    private Categorie categorie;
}
