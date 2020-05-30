package org.chkinglee.norn.odin.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.chkinglee.norn.odin.configuration.ConstantProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * ES工具
 *
 * @Author: lilinzhen
 * @Version: 2020/5/30
 **/
@Component
public class EsUtil {
    private static final Logger LOG = LoggerFactory.getLogger(EsUtil.class);

    private static String ES_URL;

    @Autowired
    private ConstantProperties constantPropertiesNew;

    @PostConstruct
    private void init() {
        ConstantProperties constantProperties = constantPropertiesNew;
        ES_URL = constantProperties.getEsUrl();
        LOG.debug("ES_URL: " + ES_URL);
    }

    public static boolean saveDoc(boolean ifDailyDB, String indexPrefix, String type, String id, String jsonStr) {
        String response = null;
        boolean rs = false;
        String index;
        try {
            if (ifDailyDB) {
                index = indexPrefix + "-" + Util.getCurrentDateStr();
            } else {
                index = indexPrefix;
            }
            String url = ES_URL + "/" + index + "/" + type + "/" + id; // 自定义存储_id

            Map<String, String> headerMap = new HashMap<>();
            // headerMap.put("Authorization", ES_ACCESS_TOKEN);

            response = HttpClientUtil.postJson(url, jsonStr, headerMap);
            LOG.info("[saveDoc] id=" + id + "    jsonStr:" + jsonStr + "    response:" + response);

            JSONObject jsonObject = JSONObject.parseObject(response);
            if (jsonObject != null && jsonObject.get("_id") != null) {
                if (StringUtils.isNotEmpty(jsonObject.get("_id").toString())) { // 成功
                    rs = true;
                } else {
                    rs = false;
                }
            } else {
                rs = false;
            }
            return rs;
        } catch (Exception e) {
            LOG.error("[Doc] error: " + e.getMessage(), e);
            return false;
        }
    }

}
