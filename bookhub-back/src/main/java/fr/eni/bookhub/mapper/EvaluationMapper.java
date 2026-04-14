package fr.eni.bookhub.mapper;

import fr.eni.bookhub.dto.EvaluationDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EvaluationMapper {

    private ModelMapper modelMapper;

    /**
     * Fonction générique pour les mappers
     * @param entity
     * @param outClass
     * @return
     * @param <D>
     * @param <T>
     */

    public <D, T> D map(T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public EvaluationDTO toEvaluationDTO(Object entity) {
        return map(entity, EvaluationDTO.class);
    }

    public Object toEntity(EvaluationDTO dto, Class<?> entityClass) {
        return map(dto, entityClass);
    }
}
