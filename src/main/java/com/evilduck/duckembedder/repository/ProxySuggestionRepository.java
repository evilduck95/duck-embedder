package com.evilduck.duckembedder.repository;

import com.evilduck.duckembedder.model.ProxySuggestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxySuggestionRepository extends MongoRepository<ProxySuggestion, String> {
}
