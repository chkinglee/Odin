package org.chkinglee.norn.odin.controller;

import org.chkinglee.norn.odin.controller.dto.ResultResponse;
import org.chkinglee.norn.odin.utils.Constant;
import org.chkinglee.norn.odin.utils.EsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/30
 **/
@RestController
@RequestMapping(value = "/api/v1/docs")
public class DocController {
    private static final Logger LOG = LoggerFactory.getLogger(DocController.class);

    @RequestMapping(value = "/{tenant}/{module}/{id}", method = RequestMethod.POST)
    public ResultResponse createOrUpdateDoc(@PathVariable("tenant") String tenant,
                                            @PathVariable("module") String module,
                                            @PathVariable("id") String id,
                                            @RequestBody String docContent) {
        LOG.info("[Odin] tenant: {}, module: {}, docContent: {}.", tenant, module, docContent);

        boolean isSuccess = EsUtil.saveDoc(false,tenant,module,id, docContent.toString());

        return new ResultResponse(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, isSuccess);
    }
}
