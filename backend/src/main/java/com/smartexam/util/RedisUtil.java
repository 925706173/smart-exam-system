package com.smartexam.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 内存缓存工具类（替代Redis，开发环境使用）
 */
@Slf4j
@Component
public class RedisUtil {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    private final Map<String, Long> expireMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RedisUtil() {
        // 定时清理过期数据
        scheduler.scheduleAtFixedRate(this::cleanExpired, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        cache.put(key, value);
    }

    /**
     * 设置缓存（带过期时间）
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        cache.put(key, value);
        expireMap.put(key, System.currentTimeMillis() + unit.toMillis(timeout));
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        if (isExpired(key)) {
            cache.remove(key);
            expireMap.remove(key);
            return null;
        }
        return cache.get(key);
    }

    /**
     * 删除缓存
     */
    public Boolean delete(String key) {
        cache.remove(key);
        expireMap.remove(key);
        return true;
    }

    /**
     * 批量删除
     */
    public Long delete(Collection<String> keys) {
        keys.forEach(key -> {
            cache.remove(key);
            expireMap.remove(key);
        });
        return (long) keys.size();
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        if (cache.containsKey(key)) {
            expireMap.put(key, System.currentTimeMillis() + unit.toMillis(timeout));
            return true;
        }
        return false;
    }

    /**
     * 获取过期时间
     */
    public Long getExpire(String key) {
        Long expireTime = expireMap.get(key);
        if (expireTime == null) {
            return -1L;
        }
        return expireTime - System.currentTimeMillis();
    }

    /**
     * 判断key是否存在
     */
    public Boolean hasKey(String key) {
        if (isExpired(key)) {
            cache.remove(key);
            expireMap.remove(key);
            return false;
        }
        return cache.containsKey(key);
    }

    /**
     * 递增
     */
    public Long increment(String key) {
        Object value = cache.get(key);
        if (value instanceof Long) {
            Long newVal = (Long) value + 1;
            cache.put(key, newVal);
            return newVal;
        }
        cache.put(key, 1L);
        return 1L;
    }

    /**
     * Hash设置
     */
    @SuppressWarnings("unchecked")
    public void hashSet(String key, String hashKey, Object value) {
        Map<String, Object> hash = (Map<String, Object>) cache.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
        hash.put(hashKey, value);
    }

    /**
     * Hash获取
     */
    @SuppressWarnings("unchecked")
    public Object hashGet(String key, String hashKey) {
        Map<String, Object> hash = (Map<String, Object>) cache.get(key);
        return hash != null ? hash.get(hashKey) : null;
    }

    /**
     * Hash删除
     */
    @SuppressWarnings("unchecked")
    public void hashDelete(String key, String hashKey) {
        Map<String, Object> hash = (Map<String, Object>) cache.get(key);
        if (hash != null) {
            hash.remove(hashKey);
        }
    }

    /**
     * 判断是否过期
     */
    private boolean isExpired(String key) {
        Long expireTime = expireMap.get(key);
        return expireTime != null && System.currentTimeMillis() > expireTime;
    }

    /**
     * 清理过期数据
     */
    private void cleanExpired() {
        long now = System.currentTimeMillis();
        expireMap.entrySet().removeIf(entry -> {
            if (now > entry.getValue()) {
                cache.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }
}
