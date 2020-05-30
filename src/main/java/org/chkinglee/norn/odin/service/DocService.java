package org.chkinglee.norn.odin.service;

import org.chkinglee.norn.odin.mapper.TenantRepository;
import org.chkinglee.norn.odin.model.Tenant;
import org.chkinglee.norn.odin.utils.EsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean createOrUpdateDoc(String tenant, String module, String id, String docContent) {

        List<Tenant> tenantList = tenantRepository.findByTenantAndModule(tenant, module);
        if (tenantList == null ) {
            LOG.info("Find Tenant ERROR!");
            return false;
        } else {
            if (tenantList.size() != 1) {
                LOG.info("No Tenant or More than one Tenant: {}", tenantList.size());
                return false;
            }
        }

        Tenant tenantEntity = tenantList.get(0);
        String indexName = tenantEntity.getIndexName();
        String typeName = tenantEntity.getTypeName();
        LOG.info("Create doc to {}/{}", indexName, typeName);
        // TODO 检查docContent是否能转换为json
        return EsUtil.saveDoc(false, indexName, typeName, id, docContent);
    }
}
