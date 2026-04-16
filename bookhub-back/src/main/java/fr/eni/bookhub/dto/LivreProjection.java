package fr.eni.bookhub.dto;

import java.time.LocalDate;

public interface LivreProjection {
    Long getId();
    String getIsbn();
    String getTitre();
    String getResume();
    String getImageCouverture();
    Integer getNbPage();
    LocalDate getDateParution();
    Long getAuteurId();
    String getAuteurNom();
    String getAuteurPrenom();
    Long getCategorieId();
    String getCategorieLibelle();
    Long getNbExemplaires();
    Long getNbExemplairesDisponibles();
    Double getMoyenneEvaluations();
}
