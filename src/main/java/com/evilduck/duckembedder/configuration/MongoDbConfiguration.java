package com.evilduck.duckembedder.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoDbConfiguration {

    @Bean
    public MappingMongoConverter mappingMongoConverter(final MongoDatabaseFactory mongoDatabaseFactory,
                                                       final MongoMappingContext mongoMappingContext) {
        final DefaultDbRefResolver defaultDbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        final MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(defaultDbRefResolver, mongoMappingContext);
        mappingMongoConverter.setMapKeyDotReplacement("_DOT");
        return mappingMongoConverter;
    }

}
