package com.yixin.garage.config.cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.google.common.collect.ImmutableMap;
import com.yixin.garage.cache.CacheNames;

/**
 * Cache 配置
 *
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "garage.cache", name = "enable", havingValue = "true",matchIfMissing = false)
public class CacheConfig {
	
	/**
	 * 创建ehCacheCacheManager, garage.cache.type 配置为ehCacheCacheManager或没有type 配置时。
	 */
	@Bean
	@ConditionalOnProperty(prefix = "garage.cache", name = "type", havingValue = CacheManagerNames.EHCACHE_CACHE_MAANGER,
		matchIfMissing = true)
	public EhCacheCacheManager ehCacheCacheManager() {
		Resource location = new ClassPathResource("ehcache.xml");
		return new EhCacheCacheManager(EhCacheManagerUtils.buildCacheManager(location));
	}
	
    /**
     * cacheManager名字
     */
    public interface CacheManagerNames {
        /**
         * redis
         */
        String REDIS_CACHE_MANAGER = "redisCacheManager";

        /**
         * ehCache
         */
        String EHCACHE_CACHE_MAANGER = "ehCacheCacheManager";
    }
   
    /**
     * 缓存名，名称暗示了缓存时长 注意： 如果添加了新的缓存名，需要同时在下面的RedisCacheCustomizer#RedisCacheCustomizer里配置名称对应的缓存时长
     * ，时长为0代表永不过期；缓存名最好公司内部唯一，因为可能多个项目共用一个redis。
     *
     * @see RedisCacheCustomizer#customize(RedisCacheManager)
     */
   /* public interface CacheNames {
        *//** 15分钟缓存组 *//*
        String CACHE_15MINS = "garage:cache:15m";
        *//** 30分钟缓存组 *//*
        String CACHE_30MINS = "garage:cache:30m";
        *//** 60分钟缓存组 *//*
        String CACHE_60MINS = "garage:cache:60m";
        *//** 180分钟缓存组 *//*
        String CACHE_180MINS = "garage:cache:180m";
    }*/

    /**
     * ehcache缓存名
     */
    public interface EhCacheNames {
        String CACHE_10MINS = "garage:cache:10m";

        String CACHE_20MINS = "garage:cache:20m";

        String CACHE_30MINS = "garage:cache:30m";
    }
    
    
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericFastJsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 设置为默认的CacheManager : redisCacheManager
     * @param redisTemplate 通过参数注入，这里没有手动给它做配置。在引入了redis的jar包，并且往
     * application.yml里添加了spring.redis的配置项，springboot的autoconfig会自动生成一个
     * redisTemplate的bean
     * @return
     */
    @Bean
    @Primary 
    @ConditionalOnProperty(prefix = "garage.cache", name = "type", havingValue = CacheManagerNames.REDIS_CACHE_MANAGER,
    		matchIfMissing = true)
    public RedisCacheManager redisCacheManager(RedisTemplate<Object, Object> redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setUsePrefix(true);
        this.redisCacheManagerCustomizer().customize(cacheManager);
        return cacheManager;
    }

    
    /** cache的一些自定义配置 */
    @Bean
    @ConditionalOnBean(RedisCacheManager.class)
    public RedisCacheCustomizer redisCacheManagerCustomizer() {
        return new RedisCacheCustomizer();
    }

    private static class RedisCacheCustomizer implements CacheManagerCustomizer<RedisCacheManager> {
        /** CacheManager缓存自定义初始化比较早，尽量不要@autowired 其他spring 组件 */
        @Override
        public void customize(RedisCacheManager cacheManager) {
            // 自定义缓存名对应的过期时间
            Map<String, Long> expires = ImmutableMap.<String, Long>builder()
                    .put(CacheNames.CACHE_15MINS, TimeUnit.MINUTES.toSeconds(15))
                    .put(CacheNames.CACHE_30MINS, TimeUnit.MINUTES.toSeconds(30))
                    .put(CacheNames.CACHE_60MINS, TimeUnit.MINUTES.toSeconds(60))
                    .put(CacheNames.CACHE_180MINS, TimeUnit.MINUTES.toSeconds(180))
                    .put(CacheNames.CONSTANT, 0L)
                    .build();
            // spring cache是根据cache name查找缓存过期时长的，如果找不到，则使用默认值
            cacheManager.setDefaultExpiration(TimeUnit.MINUTES.toSeconds(30));
            cacheManager.setExpires(expires);
        }
    }

    
}
