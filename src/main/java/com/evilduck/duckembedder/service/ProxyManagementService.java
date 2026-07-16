package com.evilduck.duckembedder.service;

import com.evilduck.duckembedder.model.ProxyMapping;
import com.evilduck.duckembedder.repository.ProxyMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProxyManagementService {

    private final ProxyMappingRepository proxyMappingRepository;

    public ProxyManagementService(final ProxyMappingRepository proxyMappingRepository) {
        this.proxyMappingRepository = proxyMappingRepository;
    }

    public boolean disableProxy(final String websiteName) {
        return updateProxyMapping(websiteName, false);
    }

    public boolean enableProxy(final String websiteName) {
        return updateProxyMapping(websiteName, true);
    }

    private boolean updateProxyMapping(final String websiteName,
                                       final boolean active) {
        boolean websiteFound = false;
        final List<ProxyMapping> proxyMappings = proxyMappingRepository.findAll();
        for (final ProxyMapping proxyMapping : proxyMappings) {
            for (final ProxyMapping.Proxy proxyWebsite : proxyMapping.getProxyWebsiteNames()) {
                if (proxyWebsite.getName().equals(websiteName)) {
                    proxyWebsite.setActive(active);
                    proxyMappingRepository.save(proxyMapping);
                    websiteFound = true;
                }
            }
        }
        return websiteFound;
    }

}
