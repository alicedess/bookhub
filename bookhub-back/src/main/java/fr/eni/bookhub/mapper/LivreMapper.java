package fr.eni.bookhub.mapper;

import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.entity.Livre;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@AllArgsConstructor
public class LivreMapper {

    private ModelMapper modelMapper;

    public LivreDTO convertToDto(Livre livre)
    {
        return modelMapper.map(livre, LivreDTO.class);
    }

    public Iterable<LivreDTO> convertToDto(Iterable<Livre> livres)
    {
        return StreamSupport.stream(livres.spliterator(), false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Livre convertToEntity(LivreDTO livreDTO)
    {
        return modelMapper.map(livreDTO, Livre.class);
    }

    public Iterable<Livre> convertToEntity(Iterable<LivreDTO> livres)
    {
        return StreamSupport.stream(livres.spliterator(), false)
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }
}
