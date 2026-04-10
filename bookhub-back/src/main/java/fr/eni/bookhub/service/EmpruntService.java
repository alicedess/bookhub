package fr.eni.bookhub.service;

import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.ExemplaireRepository;
import fr.eni.bookhub.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmpruntService {

    private EmpruntRepository empruntRepository;
    private ExemplaireRepository exemplaireRepository;
    private UtilisateurRepository utilisateurRepository;

    public Emprunt emprunterLivre(Long idUtilisateur, Long idExemplaire) {
        return null;
    }
}
