package com.example.textile.config;

import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@EnableCaching
public class CacheConfig {
    /*@Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES); // Cache expiry time
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        return new CaffeineCacheManager(Constants.NAVIGATION_CACHE, caffeine);
    }*/

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

