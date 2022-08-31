package com.soldier.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * 【配置redis的bean】
 *
 * @author soldier97
 * @date 2022/8/22 14:25
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        //redis默认的jdk序列化，这里改成String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
//
//    /**
//     * 设置CacheManager缓存规则
//     * @param factory
//     * @return
//     */
//    /**
//     * 自定义缓存管理器，注入RedisCacheManager（不使用springboot默认提供的），
//     * 下面缓存管理器配置了缓存失效时间，(如果有其他需求，需要重新定义缓存管理器，在使用缓存注解时指定对应的缓存管理器)
//     * 过期时间只对Cache的那几个注解有效比如（@Cacheable，@CachePut），跟redisTemplate对象添加的缓存无关
//     * 以及cache注解存取数据的序列化设置
//     */
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory factory) {
//        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//
//        //（取出数据序列化配置）
//        // 配置在使用 @Cacheable(value="'stus'",key="'stu:1'")注解，当第二次从缓存中读取
//        // 数据时，无法将json字符串转换为java Bean的问题，不配置则抛出异常
//        // （java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to
//        // com.springboot.enty.Stu）
//        //解决查询缓存转换异常的问题
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        // 配置序列化（解决乱码的问题）,过期时间600秒
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofSeconds(600))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))           .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
//                .disableCachingNullValues();
//
//        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
//                .cacheDefaults(config)
//                .build();
//        return cacheManager;
//    }
}
