package com.greengrim.green.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongodbConfig {

  @Value("${spring.data.mongodb.uri}")
  private String uri;

  @Bean
  public MongoDatabaseFactory mongoDatabaseFactory() {
    return new SimpleMongoClientDatabaseFactory(uri);
  }

  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongoDatabaseFactory());
  }
}
