package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "LIVRE")
@Data
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    private String isbn;

    @NotNull
    private String titre;

    @ManyToOne
    @JoinColumn(name = "AUTEUR")
    private Auteur auteur;

    private String resume;

    private String imgCouverture;

    @ManyToOne
    @JoinColumn(name = "CATEGORIE")
    private Categorie categorie;
}
