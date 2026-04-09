package fr.eni.bookhub.service;

import fr.eni.bookhub.dto.LivreDTO;

public interface ILivreService {
    Iterable<LivreDTO> getAll();
}
