package com.evilduck.duckembedder.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class SerdesConfiguration {

    @Bean
    public ObjectMapper jsonObjectMapper() {
        return new ObjectMapper();
    }

}
