package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AUTEUR")
@Data
public class Auteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    /*
    @OneToMany(mappedBy = "auteur")
    private Set<Livre> livres = new LinkedHashSet<>();
    */
}
