package org.chkinglee.norn.odin;

import org.chkinglee.norn.odin.utils.EsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OdinApplication {
    public static final Logger LOG = LoggerFactory.getLogger(OdinApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OdinApplication.class, args);
        LOG.info("[Application] Application Started ...chkinglee");
        EsUtil.saveDoc(false,"chkinglee_test","chkinglee_test_type","aaa1231231234","{\"name\":\"chkinglee\"}");

    }

}
