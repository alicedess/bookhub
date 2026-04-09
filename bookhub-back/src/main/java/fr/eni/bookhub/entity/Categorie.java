package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "CATEGORIES")
@Data
@EqualsAndHashCode(of = {"id"})
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "libelle", unique = true)
    private String libelle;
}
