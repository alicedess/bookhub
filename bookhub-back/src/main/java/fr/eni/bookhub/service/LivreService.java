package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.LivreDTO;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.mapper.LivreMapper;
import fr.eni.bookhub.repository.LivreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LivreService implements ILivreService {

    private LivreRepository livreRepository;

    private LivreMapper livreMapper;

    @Override
    public Iterable<LivreDTO> getAll()
    {
        Iterable<Livre> livres = livreRepository.findAll();

        return livreMapper.convertToDto(livres);
    }
}
