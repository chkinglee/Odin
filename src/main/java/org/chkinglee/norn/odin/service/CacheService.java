package org.chkinglee.norn.odin.service;

import com.github.pagehelper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/31
 **/
@Service
public class CacheService {
    private static final Logger LOG = LoggerFactory.getLogger(CacheService.class);
    @Autowired
    StringRedisService stringRedisService;

    public String get(String key) {
        String value = null;
        long t1 = System.currentTimeMillis();
        try {
            if (stringRedisService.exists(key)) {
                value = stringRedisService.get(key);
            }
        } catch (Exception e) {
            LOG.error("[cache get] exception:", e);
        }
        long t2 = System.currentTimeMillis();
        LOG.info("[cache get] latency=" + (t2 - t1) + " ms");
        return value;
    }

    public void set(String key, String value, int ttl) {
        long t1 = System.currentTimeMillis();
        try {
            stringRedisService.set(key, value, ttl);
        } catch (Exception e) {
            LOG.error("[cache set] exception:", e);
        }
        long t2 = System.currentTimeMillis();
        LOG.info("[cache set] latency=" + (t2 - t1) + " ms");
    }

}
