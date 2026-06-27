package com.evilduck.duckembedder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class ProxyMapping {

    @Id
    private String websiteName;
    private Set<Proxy> proxyWebsiteNames;

    @Data
    public static class Proxy {

        private String name;
        private boolean active;

    }

}
