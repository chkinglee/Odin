package org.chkinglee.norn.odin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import org.chkinglee.norn.odin.mapper.TenantRepository;
import org.chkinglee.norn.odin.model.Tenant;
import org.chkinglee.norn.odin.utils.EsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.chkinglee.norn.odin.utils.Constant.*;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/31
 **/
@Service
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Autowired
    TenantRepository tenantRepository;
    @Autowired
    CacheService cacheService;


    public boolean createOrUpdateDoc(String tenantName, String module, String id, String docContent) {

        Tenant tenant = getTenantWithMem(tenantName, module);
        if (tenant == null) {
            return false;
        }
        String indexName = tenant.getIndexName();
        String typeName = tenant.getTypeName();
        LOG.info("[Doc] save doc to {}/{}", indexName, typeName);
        // TODO 检查docContent是否能转换为json
        return EsUtil.saveDoc(false, indexName, typeName, id, docContent);
    }

    public Tenant getTenantWithMem(String tenantName, String module) {
        Tenant tenant = null;
        String key = ODIN_REDIS_KEY_TENANT_PREFIX + ":" + tenantName + ":" + module;
        // ActionParam memActionParam = Constant.memActionCache.getIfPresent(key);
        Tenant memTenant = null;
        // 本地mem中不存在或数据过老,需要更新到本地
        if (memTenant == null) {
            tenant = getTenantWithCache(tenantName, module);
            if (tenant != null) { // redis和policy-manager中可以查询到
                // actionParam.setMemCacheTime(curTime);
                // Constant.memActionCache.put(key, actionParam);
            } else { // redis和policy-manager中都没有或故障了,仍旧以本地为准
                tenant = memTenant;
            }
        } else {  // 本地mem中存在且数据比较新，则直接用
            tenant = memTenant;
        }
        return tenant;

    }

    public Tenant getTenantWithCache(String tenantName, String module) {
        String tenantCacheJson = getTenantFromCache(tenantName, module);
        Tenant tenant = null;
        if (StringUtil.isNotEmpty(tenantCacheJson)) {
            tenant = JSONObject.parseObject(tenantCacheJson, Tenant.class);
        } else {
            // 查询DB
            tenant = getTenantFromDB(tenantName, module);
            if (tenant != null) {
                // 添加到缓存
                setTenantToCache(tenantName, module, JSON.toJSONString(tenant));
            }
        }
        return tenant;
    }

    public Tenant getTenantFromDB(String tenantName, String module) {
        long t1 = System.currentTimeMillis();

        List<Tenant> tenantList = tenantRepository.findByTenantAndModule(tenantName, module);
        if (tenantList == null) {
            LOG.info("[DB] get Tenant ERROR !");
            return null;
        } else {
            if (tenantList.size() != 1) {
                LOG.info("[DB] No Tenant or more than one Tenant: {}", tenantList.size());
                return null;
            }
        }

        long t2 = System.currentTimeMillis();
        LOG.info("[DB] get Tenant latency=" + (t2 - t1) + " ms");
        LOG.info("[DB] get Tenant success: {}", JSON.toJSONString(tenantList.get(0)));
        return tenantList.get(0);
    }

    public String getTenantFromCache(String tenantName, String module) {
        String key = ODIN_REDIS_KEY_TENANT_PREFIX + ":" + tenantName + ":" + module;
        String jsonStr = cacheService.get(key);
        LOG.info("[Cache] get Tenant: {} = {}", key, (jsonStr == null ? "null" : jsonStr));
        return jsonStr;
    }

    public void setTenantToCache(String tenantName, String module, String jsonStr) {
        String key = ODIN_REDIS_KEY_TENANT_PREFIX + ":" + tenantName + ":" + module;
        int ttl = 120;
        if (ttl <= 0) {
            ttl = 60;
        }
        cacheService.set(key, jsonStr, ttl);
        LOG.info("[Cache] set Tenant: {} = {}", key, jsonStr);
    }
}
