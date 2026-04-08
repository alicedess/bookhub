package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AUTEUR")
@Data
public class Auteur {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;
}
