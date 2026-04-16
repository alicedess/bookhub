package fr.eni.bookhub.entity;

import fr.eni.bookhub.enumeration.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ROLE")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "libelle", unique = true, nullable = false, length = 20)
    private RoleEnum libelle;
}
