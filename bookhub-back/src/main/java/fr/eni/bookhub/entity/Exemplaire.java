package fr.eni.bookhub.entity;

import fr.eni.bookhub.enumeration.EtatExemplaire;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "exemplaire")
public class Exemplaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long idExemplaire;

    @Column(name = "code_barre", nullable = false, length = 50)
    private String codeBarre;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = true, length = 50)
    private EtatExemplaire etat;

    @Column(name = "est_disponible")
    private Boolean estDisponible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livre", nullable = false)
    private Livre livre;
}
