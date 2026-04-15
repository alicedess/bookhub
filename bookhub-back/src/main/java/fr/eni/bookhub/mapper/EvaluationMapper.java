package fr.eni.bookhub.mapper;

import fr.eni.bookhub.dto.EvaluationDTO;
import fr.eni.bookhub.entity.Evaluation;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EvaluationMapper {

    private ModelMapper modelMapper;

    public EvaluationDTO toEvaluationDTO(Evaluation eval) {
        return modelMapper.map(eval, EvaluationDTO.class);
    }

    public Evaluation toEntity(EvaluationDTO dto) {
        return modelMapper.map(dto, Evaluation.class);
    }
}
