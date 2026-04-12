package br.com.ipet.ordering.infrastructure.util.modelmapper;

import br.com.ipet.ordering.application.util.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public Mapper modelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return new Mapper() {
            @Override
            public <T> T convert(Object source, Class<T> destinationType) {
                return modelMapper.map(source, destinationType);
            }
        };
    }

}
