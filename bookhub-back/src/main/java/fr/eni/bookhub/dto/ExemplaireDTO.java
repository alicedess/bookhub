package fr.eni.bookhub.dto;

import fr.eni.bookhub.enumeration.EtatExemplaireEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExemplaireDTO {

    private Long id;

    private String codeBarre;

    private EtatExemplaireEnum etat;

    private Boolean estDisponible;

    private Long idLivre;

}
