package fr.eni.bookhub.dto;

import fr.eni.bookhub.enumeration.StatutEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpruntDTO {
    private Long idEmprunt;
    private LocalDateTime dateDebut;
    private LocalDateTime dateRetourPrevue;
    private LocalDateTime dateRetourEffective;
    private StatutEnum statut;
    private boolean enRetard;

    private Long utilisateurId;
    private String nomUtilisateur;
    private Long exemplaireId;
    private Long livreId;
    private String titreLivre;
    private String auteurNom;
    private String auteurPrenom;
}
