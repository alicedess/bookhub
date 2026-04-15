package fr.eni.bookhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLivreDTO {
    @NotBlank
    @Size(min = 3, max = 13)
    private String isbn;

    @NotBlank
    @Size(min = 3, max = 50)
    private String titre;

    @NotBlank
    @Size(min = 3, max = 100)
    private String resume;

    private String imageCouverture;

    @NotBlank
    private Integer auteurId;

    @NotBlank
    private Long categorieId;
}
