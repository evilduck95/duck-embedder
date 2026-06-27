package com.evilduck.duckembedder;

import com.evilduck.duckembedder.configuration.properties.JdaConfigProps;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JdaConfigProps.class)
public class DuckEmbedderApplication {

    @Value("${jda.token}")
    private String token;

    @PostConstruct
    public void check() {
        System.out.println(token);
    }

    public static void main(String[] args) {
        SpringApplication.run(DuckEmbedderApplication.class, args);
    }

}
