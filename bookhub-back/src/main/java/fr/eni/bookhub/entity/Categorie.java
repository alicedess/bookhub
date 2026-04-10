package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "CATEGORIE")
@Data
@EqualsAndHashCode(of = {"id"})
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "libelle", unique = true, nullable = false, length = 50)
    private String libelle;

    /*
    @OneToMany(mappedBy = "categorie")
    private Set<Livre> livres = new LinkedHashSet<>();
    */
}
