package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AVIS")
@Data
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

}
