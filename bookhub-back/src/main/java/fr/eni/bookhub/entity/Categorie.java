package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CATEGORIE")
@Data
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "categorie", unique = true)
    private String categorie;
}
