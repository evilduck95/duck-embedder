package com.evilduck.duckembedder.repository;

import com.evilduck.duckembedder.model.ProxyMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProxyMappingRepository extends MongoRepository<ProxyMapping, String> {

    Optional<ProxyMapping> findByWebsiteName(String websiteName);

}
