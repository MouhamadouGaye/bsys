package com.mgaye.bsys.conf;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mgaye.bsys.dto.UserRegistrationDto;
import com.mgaye.bsys.model.User;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Basic configuration
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true);

        // Custom mappings
        modelMapper.typeMap(UserRegistrationDto.class, User.class).addMappings(mapper -> {
            mapper.map(UserRegistrationDto::getFirstName, User::setFirstName);
            mapper.map(UserRegistrationDto::getLastName, User::setLastName);
            mapper.skip(User::setId);
            mapper.skip(User::setRoles);
            mapper.skip(User::setPassword);
        });

        // Password should be handled separately
        modelMapper.typeMap(UserRegistrationDto.class, User.class).setPostConverter(context -> {
            User user = context.getDestination();
            user.setPassword("<handled in service>"); // Placeholder, actual hashing in service
            return user;
        });

        return modelMapper;
    }
}