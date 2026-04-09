package fr.eni.bookhub.config;

import fr.eni.bookhub.dto.CreateLivreDTO;
import fr.eni.bookhub.entity.Livre;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(CreateLivreDTO.class, Livre.class).addMappings(mapper -> {
            mapper.skip(Livre::setId);
            mapper.map(CreateLivreDTO::getAuteurId, (dest, v) -> dest.getAuteur().setId((Integer) v));
            mapper.map(CreateLivreDTO::getCategorieId, (dest, v) -> dest.getCategorie().setId((Long) v));
        });

        return modelMapper;
    }
}
