package com.dbtest.yashwith.config;

import org.redisson.config.Config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.cache.CacheManager;
import javax.cache.Caching;

@Configuration
public class RedisConfig
{

    /**
     * Redis start config {server start address.}
     *
     * // TODO: Move it to application.properties
     * @return
     */
    @Bean
    public Config config()
    {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return config;
    }

    /**
     * Configure cache management for redis.
     * @param config redis config
     * @return
     */
    @Bean(name = "redis-manager")
    public CacheManager cacheManager(Config config) {
        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        manager.createCache("cache", RedissonConfiguration.fromConfig(config));
        return manager;
    }

    /**
     * TODO Learn: JCache Proxy manager
     * @param cacheManager
     * @return
     */
    @Bean
    ProxyManager<String> proxyManager(CacheManager cacheManager) {
        return new JCacheProxyManager<>(cacheManager.getCache("cache"));
    }

}

