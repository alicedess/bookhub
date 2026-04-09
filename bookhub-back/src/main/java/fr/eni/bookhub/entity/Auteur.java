package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AUTEURS")
@Data
public class Auteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;
}
