package org.chkinglee.norn.odin.service;

import org.chkinglee.norn.odin.model.Tenant;
import org.chkinglee.norn.odin.utils.EsUtil;
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
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Autowired
    TenantService tenantService;


    public boolean createOrUpdateDoc(String tenantName, String module, String id, String docContent) {

        Tenant tenant = tenantService.getTenantWithMem(tenantName, module);
        if (tenant == null) {
            return false;
        }
        String indexName = tenant.getIndexName();
        String typeName = tenant.getTypeName();
        LOG.info("[Doc] save doc to {}/{}", indexName, typeName);
        // TODO 检查docContent是否能转换为json
        return EsUtil.saveDoc(false, indexName, typeName, id, docContent);
    }

}
