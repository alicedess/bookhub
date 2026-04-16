package fr.eni.bookhub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLivreDTO {
    @NotBlank
    @Size(min = 3, max = 13)
    private String isbn;

    @NotBlank
    @Size(min = 3, max = 255)
    private String titre;

    @PositiveOrZero
    private Integer nbPage;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateParution;

    @Size(max = 255)
    private String resume;

    private String imageCouverture;

    @NotNull
    private Long auteurId;

    @NotNull
    private Long categorieId;
}
