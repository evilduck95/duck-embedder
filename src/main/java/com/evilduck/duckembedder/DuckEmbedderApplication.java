package com.evilduck.duckembedder;

import com.evilduck.duckembedder.configuration.properties.JdaConfigProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JdaConfigProps.class)
public class DuckEmbedderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DuckEmbedderApplication.class, args);
    }

}
