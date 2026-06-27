package com.evilduck.duckembedder.repository;

import com.evilduck.duckembedder.model.ProxyMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxyMappingRepository extends MongoRepository<ProxyMapping, String> {

}
