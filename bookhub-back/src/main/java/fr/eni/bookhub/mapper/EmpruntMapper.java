package fr.eni.bookhub.mapper;

import fr.eni.bookhub.dto.EmpruntDTO;
import fr.eni.bookhub.entity.Emprunt;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EmpruntMapper {

    private ModelMapper modelMapper;

    public EmpruntDTO convertToDto(Emprunt emprunt) {
        if (emprunt == null) {
            return null;
        }

        EmpruntDTO dto = modelMapper.map(emprunt, EmpruntDTO.class);

        if (emprunt.getUtilisateur() != null) {
            dto.setUtilisateurId(emprunt.getUtilisateur().getId());
        }

        if (emprunt.getExemplaire() != null) {
            dto.setExemplaireId(emprunt.getExemplaire().getId());
            if (emprunt.getExemplaire().getLivre() != null) {
                dto.setLivreId(emprunt.getExemplaire().getLivre().getId());
                dto.setTitreLivre(emprunt.getExemplaire().getLivre().getTitre());
            }
        }

        return dto;
    }

    public List<EmpruntDTO> convertToDto(List<Emprunt> emprunts) {
        if (emprunts == null) {
            return Collections.emptyList();
        }
        return emprunts.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
