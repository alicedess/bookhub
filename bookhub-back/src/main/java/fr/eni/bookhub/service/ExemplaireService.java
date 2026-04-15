package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.ExemplaireDTO;
import fr.eni.bookhub.entity.Exemplaire;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.repository.ExemplaireRepository;
import fr.eni.bookhub.repository.LivreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ExemplaireService {

    private ExemplaireRepository exemplaireRepository;
    private LivreRepository livreRepository;

    @Transactional
    public void updateExemplairesParLivreId(Long livreId, List<ExemplaireDTO> payload) {
        // 1. Récupérer le livre (parent) pour lier les nouveaux exemplaires
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new EntityNotFoundException("Livre non trouvé"));

        // 2. Récupérer les exemplaires actuellement en base pour ce livre
        List<Exemplaire> existants = exemplaireRepository.findByLivre(livre);

        // 3. Identifier les exemplaires à supprimer (ceux en base qui ne sont pas dans le payload)
        List<Long> idsDansPayload = payload.stream()
                .map(ExemplaireDTO::getId)
                .filter(Objects::nonNull)
                .toList();

        List<Exemplaire> aSupprimer = existants.stream()
                .filter(e -> !idsDansPayload.contains(e.getId()))
                .toList();

        exemplaireRepository.deleteAll(aSupprimer);

        // 4. Traiter le payload pour Ajout / Mise à jour
        for (ExemplaireDTO dto : payload) {
            Exemplaire exemplaire;

            if (dto.getId() != null) {
                // Mise à jour : on récupère l'existant
                exemplaire = exemplaireRepository.findById(dto.getId())
                        .orElse(new Exemplaire()); // Sécurité
            } else {
                // Création : nouvel objet
                exemplaire = new Exemplaire();
                exemplaire.setLivre(livre);
            }

            // Mapping des données
            exemplaire.setCodeBarre(dto.getCodeBarre());
            exemplaire.setEtat(dto.getEtat());
            exemplaire.setEstDisponible(null != dto.getEstDisponible() && dto.getEstDisponible());

            exemplaireRepository.save(exemplaire);
        }
    }

    public List<ExemplaireDTO> getExemplairesParLivreId(Long livreId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new EntityNotFoundException("Livre non trouvé"));


        List<Exemplaire> exemplaires = exemplaireRepository.findByLivre(livre);

        return exemplaires.stream().map(e -> new ExemplaireDTO(
                e.getId(),
                e.getCodeBarre(),
                e.getEtat(),
                e.getEstDisponible(),
                e.getLivre().getId()
        )).toList();
    }
}
