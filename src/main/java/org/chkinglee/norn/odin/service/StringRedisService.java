package org.chkinglee.norn.odin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/31
 **/
@Service
public class StringRedisService {
    private static final Logger LOG = LoggerFactory.getLogger(StringRedisService.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void del(final String...keys) {
        List<String> keysList = new ArrayList<>(Arrays.asList(keys));
        stringRedisTemplate.delete(keysList);
    }

    public void del(final List<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    public Boolean set(String key, String value, long liveTimeInSeconds) {
        stringRedisTemplate.opsForValue().set(key, value);
        return expire(key, liveTimeInSeconds);
    }

    public Boolean set(String key, String value) {
        return this.set(key, value, 0L);
    }

    public String get(final String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Boolean setMap(final String key, final Map<String, String> value,
                          final long liveTimeInSeconds) {
        stringRedisTemplate.opsForHash().putAll(key, value);
        return expire(key, liveTimeInSeconds);
    }

    public Map<String, String> getMap(final String key) {
        Map<String, String> result;
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
        result = new LinkedHashMap<>();
        for (Object k : map.keySet()) {
            result.put((String) k, (String) map.get(k));
        }
        return result;
    }

    public Boolean setList(final String key, final List<String> value, final long liveTimeInSeconds) {
        stringRedisTemplate.opsForList().rightPushAll(key, value);
        return expire(key, liveTimeInSeconds);
    }

    public List<String> getList(final String key) {
        return stringRedisTemplate.opsForList().range(key, 0, stringRedisTemplate.opsForList().size(key));
    }

    public Boolean exists(final String key) {
        return stringRedisTemplate.hasKey(key);
    }

    private Boolean expire(final String key, final long liveTimeInSeconds) {
        Boolean r = null;
        if (liveTimeInSeconds > 0) {
            r = stringRedisTemplate.expire(key, liveTimeInSeconds, TimeUnit.SECONDS);
            LOG.debug("Redis key: [" + key + "] will expired in " + liveTimeInSeconds + " seconds, result: " + r);
        }
        return r;
    }
}
