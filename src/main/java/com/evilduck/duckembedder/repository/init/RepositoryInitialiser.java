package com.evilduck.duckembedder.repository.init;

import com.evilduck.duckembedder.model.ProxyMapping;
import com.evilduck.duckembedder.repository.ProxyMappingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class RepositoryInitialiser {

    private final Resource proxySiteConfigs;
    private final ObjectMapper jsonObjectMapper;
    private final ProxyMappingRepository proxyMappingRepository;

    public RepositoryInitialiser(@Value("classpath:proxy-site-configs.json") final Resource proxySiteConfigs,
                                 final ObjectMapper jsonObjectMapper,
                                 final ProxyMappingRepository proxyMappingRepository) {
        this.proxySiteConfigs = proxySiteConfigs;
        this.jsonObjectMapper = jsonObjectMapper;
        this.proxyMappingRepository = proxyMappingRepository;
    }

    // TODO: Make this merge with existing entries instead of overwriting.
    @PostConstruct
    public void initProxyMappings() throws IOException {
        final File file = proxySiteConfigs.getFile();
        final List<ProxyMapping> proxyMappings = jsonObjectMapper.readValue(file, new TypeReference<>() {});
        proxyMappingRepository.deleteAll();
        proxyMappingRepository.saveAll(proxyMappings);
    }

}
