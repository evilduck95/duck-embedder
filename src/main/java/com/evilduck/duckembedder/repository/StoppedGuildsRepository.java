package com.evilduck.duckembedder.repository;

import com.evilduck.duckembedder.model.ProxyMapping;
import com.evilduck.duckembedder.model.StoppedGuild;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoppedGuildsRepository extends MongoRepository<StoppedGuild, String> {

    Optional<String> findByGuildId(String websiteName);

}
