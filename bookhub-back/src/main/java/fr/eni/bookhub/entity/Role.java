package fr.eni.bookhub.entity;

import fr.eni.bookhub.enumeration.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "libelle", unique = true)
    private String libelle;
}
