package org.chkinglee.norn.odin.controller;

import org.chkinglee.norn.odin.controller.dto.ResultResponse;
import org.chkinglee.norn.odin.service.DocService;
import org.chkinglee.norn.odin.utils.EsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.chkinglee.norn.odin.utils.Constant.*;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/30
 **/
@RestController
@RequestMapping(value = API_V1 + "/docs")
public class DocController {
    private static final Logger LOG = LoggerFactory.getLogger(DocController.class);

    @Autowired
    DocService docService;

    @PostMapping("/{tenant}/{module}/{id}")
    public ResultResponse<Boolean> createOrUpdateDoc(@PathVariable("tenant") String tenant,
                                                     @PathVariable("module") String module,
                                                     @PathVariable("id") String id,
                                                     @RequestBody String docContent) {
        // LOG.info("[Odin] tenant: {}, module: {}, docContent: {}.", tenant, module, docContent);

        // TODO 检查docContent是否能转换为json
        boolean isSuccess = docService.createOrUpdateDoc(tenant, module, id, docContent);

        return new ResultResponse<>(SUCCESS_CODE, SUCCESS_MSG, isSuccess);
    }
}
