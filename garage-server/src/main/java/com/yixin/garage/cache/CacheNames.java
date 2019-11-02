
package com.yixin.garage.cache;

import org.springframework.data.redis.cache.RedisCacheManager;


/**
 * 所有缓存名称的集合
 * 缓存名，名称暗示了缓存时长 注意： 如果添加了新的缓存名，需要同时在下面的RedisCacheCustomizer#RedisCacheCustomizer里配置名称对应的缓存时长
 * ，时长为0代表永不过期；缓存名最好公司内部唯一，因为可能多个项目共用一个redis。
 *
 * @see RedisCacheCustomizer#customize(RedisCacheManager)
 */
public interface CacheNames {

    /**
     * 常量缓存
     */
    String CONSTANT = "garage:CONSTANT";
    
    /** 15分钟缓存组 */
    String CACHE_15MINS = "garage:cache:15m";
    /** 30分钟缓存组 */
    String CACHE_30MINS = "garage:cache:30m";
    /** 60分钟缓存组 */
    String CACHE_60MINS = "garage:cache:60m";
    /** 180分钟缓存组 */
    String CACHE_180MINS = "garage:cache:180m";
    
    
}
