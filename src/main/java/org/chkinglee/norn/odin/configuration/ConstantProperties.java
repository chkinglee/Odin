package org.chkinglee.norn.odin.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/29
 **/
@Component
@ConfigurationProperties(prefix = "constant")
public class ConstantProperties {

    public String getEsUrl() {
        return esUrl;
    }

    public void setEsUrl(String esUrl) {
        this.esUrl = esUrl;
    }

    private String esUrl;

}
