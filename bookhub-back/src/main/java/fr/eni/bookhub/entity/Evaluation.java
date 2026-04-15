package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Entity
@Table(name = "evaluation")
@Data
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "note")
    private Integer note;

    @Size(max = 1000)
    @Nationalized
    @Column(name = "commentaire", length = 1000)
    private String commentaire;

    @Column(name = "est_modere")
    private Boolean estModere = false;

    @ColumnDefault("getdate()")
    @Column(name = "date_publication")
    private Instant datePublication;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_livre", nullable = false)
    private Livre livre;
}