package com.evilduck.duckembedder.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jda")
public class JdaConfigProps {

    private String token;

}
