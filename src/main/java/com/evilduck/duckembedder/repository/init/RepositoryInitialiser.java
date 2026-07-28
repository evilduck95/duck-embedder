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
import java.util.Set;

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

    @PostConstruct
    public void initProxyMappings() throws IOException {
        final File file = proxySiteConfigs.getFile();
        final List<ProxyMapping> proxyMappings = jsonObjectMapper.readValue(file, new TypeReference<>() {});
        for (ProxyMapping proxy : proxyMappings) {
            saveDefaults(proxy);
        }
    }

    private void saveDefaults(ProxyMapping proxy) {
        Set<ProxyMapping.Proxy> tempSet = proxy.getProxyWebsiteNames();
        tempSet.addAll(proxyMappingRepository.findByWebsiteName(proxy.getWebsiteName()).get().getProxyWebsiteNames());
        proxy.setProxyWebsiteNames(tempSet);
        proxyMappingRepository.save(proxy);
    }
}
