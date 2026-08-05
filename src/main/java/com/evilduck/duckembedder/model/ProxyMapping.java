package com.evilduck.duckembedder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Duration;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class ProxyMapping {

    @Id
    private String websiteName;
    private Duration maxEmbedTime;
    private Set<Proxy> proxyWebsiteNames;

    @Data
    public static class Proxy {

        private String name;
        private boolean active;

    }

}
