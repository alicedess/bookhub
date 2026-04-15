package fr.eni.bookhub.dto;

import java.util.Date;

public interface LivreProjection {
    Long getId();
    String getIsbn();
    String getTitre();
    String getResume();
    String getImageCouverture();
    Long getNbPage();
    Date getDateParution();
    Long getAuteurId();
    String getAuteurNom();
    String getAuteurPrenom();
    Long getCategorieId();
    String getCategorieLibelle();
    Long getNbExemplaires();
    Long getNbExemplairesDisponibles();
    Double getMoyenneEvaluations();
}
