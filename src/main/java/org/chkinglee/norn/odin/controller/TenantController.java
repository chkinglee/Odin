package org.chkinglee.norn.odin.controller;

import org.chkinglee.norn.odin.controller.dto.ResultResponse;
import org.chkinglee.norn.odin.model.Tenant;
import org.chkinglee.norn.odin.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.chkinglee.norn.odin.utils.Constant.*;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/31
 **/
@RestController
@RequestMapping(value = API_V1 + "/tenant")
public class TenantController {

    @Autowired
    TenantService tenantService;

    @GetMapping("/{tenant}/{module}")
    public ResultResponse<List<Tenant>> getIndexAndTypeByTenantAndModule(@PathVariable("tenant") String tenant,
                                                                         @PathVariable("module") String module) {
        List<Tenant> tenantList = tenantService.findTenant(tenant, module);
        return new ResultResponse<>(SUCCESS_CODE, SUCCESS_MSG, tenantList);
    }


}
