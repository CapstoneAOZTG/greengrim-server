package com.greengrim.green.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greengrim.green.common.serializer.NullToEmptyStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class JsonCustomConfig {
    @Bean
    public MappingJackson2HttpMessageConverter httpMessageConverter(ObjectMapper objectMapper){
        objectMapper.getSerializerProvider()
                .setNullValueSerializer(new NullToEmptyStringSerializer());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
