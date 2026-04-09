package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.sql.DataSource;
import java.util.Date;

@Entity
@Table(name = "UTILISATEUR")
@Data
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "telephone")
    private Integer telephone;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    @DateTimeFormat
    @Column(name = "dateNaissance")
    private Date dateNaissance;

    @DateTimeFormat
    @Column(name = "dateSuppression")
    private Date dateSuppression;
}
