package fr.eni.bookhub.mapper;

import fr.eni.bookhub.dto.UtilisateurDTO;
import fr.eni.bookhub.entity.Utilisateur;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UtilisateurMapper {
    private ModelMapper modelMapper;

    public UtilisateurDTO convertToDto(Utilisateur utilisateur)
    {
        return modelMapper.map(utilisateur, UtilisateurDTO.class);
    }

    public Utilisateur convertToEntity(UtilisateurDTO utilisateurDTO){
        return modelMapper.map(utilisateurDTO, Utilisateur.class);
    }
}
