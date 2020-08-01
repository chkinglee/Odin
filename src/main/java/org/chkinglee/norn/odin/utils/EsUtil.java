package org.chkinglee.norn.odin.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.chkinglee.norn.odin.configuration.ConstantProperties;
import org.chkinglee.norn.odin.model.es.EsResponse;
import org.chkinglee.norn.odin.model.es.Shards;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

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

    /**
     * 保存Doc
     *
     * @param ifDailyDB   是否按天分库
     * @param indexPrefix 索引前缀，如果按天分库，则索引全名例如：hogwarts-20200731
     * @param type        类型
     * @param id          docId，自定义id
     * @param jsonStr     doc内容
     * @return
     */
    public static boolean saveDoc(boolean ifDailyDB, String indexPrefix, String type, String id, String jsonStr) {
        String response;
        boolean rs = false;
        String index;
        try {
            // 根据需要进行分库
            if (ifDailyDB) {
                index = indexPrefix + "-" + Util.getCurrentDateStr();
            } else {
                index = indexPrefix;
            }
            // 构造url
            String url = format("%s/%s/%s/%s", ES_URL, index, type, id);
            // 构造header
            Map<String, String> headerMap = new HashMap<>();
            // 构造body
            // None
            // 发出request
            response = executeInEs(url, jsonStr, headerMap, "POST", "SaveDoc");
            LOG.info(format("[SaveDoc] id=%s, jsonStr=%s, response=%s", id, jsonStr, response));
            // 处理response
            JSONObject jsonObject = JSONObject.parseObject(response);
            if (jsonObject != null && jsonObject.get("_id") != null) {
                // 成功
                rs = StringUtils.isNotEmpty(jsonObject.get("_id").toString());
            }
            return rs;
        } catch (Exception e) {
            LOG.error("[SaveDoc] error: " + e.getMessage(), e);
            return false;
        }
    }


    public static boolean updateDoc(boolean ifDailyDB, String indexPrefix, String type, String id, String jsonStr) {
        String response;
        boolean rs = false;
        String index;
        try {
            // 根据需要进行分库
            if (ifDailyDB) {
                index = indexPrefix + "-" + Util.getCurrentDateStr();
            } else {
                index = indexPrefix;
            }
            // 构造url
            String url = format("%s/%s/%s/%s/_update", ES_URL, index, type, id);
            // 构造header
            Map<String, String> headerMap = new HashMap<>();
            // 构造body
            jsonStr = "{\"doc\":" + jsonStr + "}";
            // 发出request
            response = executeInEs(url, jsonStr, headerMap, "POST", "UpdateDoc");
            LOG.info(format("[UpdateDoc] id=%s, jsonStr=%s, response=%s", id, jsonStr, response));
            // 处理response
            JSONObject jsonObject = JSONObject.parseObject(response);
            if (jsonObject != null && jsonObject.get("_id") != null) {
                // 成功
                rs = StringUtils.isNotEmpty(jsonObject.get("_id").toString());
            }
            return rs;
        } catch (Exception e) {
            LOG.error("[UpdateDoc] error: " + e.getMessage(), e);
            return false;
        }
    }

    public static EsResponse searchDoc(String url, String jsonStr) throws Exception {
        try {
            Map<String, String> headerMap = new HashMap<>();
            String response = executeInEs(url, jsonStr, headerMap, "POST", "SearchDoc");
            EsResponse esResponse = JSON.parseObject(response, EsResponse.class);
            Shards shards = esResponse.getShards();
            if (shards != null) {
                int failed = shards.getFailed();
                if (failed > 0) {
                    int total = shards.getTotal();
                    int successful = shards.getSuccessful();
                    LOG.warn(format("Search Doc shards failed: [total=%d, success=%d, failed=%d]", total, successful,
                            failed));
                }
            } else {
                LOG.warn("[SearchDoc] error: no shards scan.");
            }
            return esResponse;
        } catch (Exception e) {
            LOG.error("[SearchDoc] error: " + e.getMessage(), e);
            throw e;
        }
    }

    private static String executeInEs(String url, String jsonStr, Map<String, String> headerMap,
                                      String requestType, String requestComment) throws Exception {
        String response = null;
        try {
            LOG.info(format("[Doc] [%s] [%s]", requestType, requestComment));
            LOG.info(format("[Doc] execute url: %s", url));
            LOG.info(format("[Doc] execute jsonStr: %s", jsonStr));
            switch (requestType) {
                case "POST":
                    response = HttpClientUtil.postJson(url, jsonStr, headerMap);
                    break;
                case "GET":
                    break;
                default:
                    LOG.error(format("[Doc] unknown request type: %s", requestType));
                    break;
            }
            LOG.info(format("[Doc] execute result: %s", response));
            return response;
        } catch (Exception e) {
            LOG.error("[Doc] error: " + e.getMessage(), e);
            throw e;
        }
    }
    //
    //public static EventData searchEvent(String esUrl, String index, String type, Map<String, Object> terms,
    //                                    long startTime, long endTime, int size, int from, String sortField,
    //                                    boolean asc,int interfaceVersion)
    //        throws Exception {
    //    EventData eventData = new EventData();
    //    Map<String, Object> query = buildEventQuery(startTime, endTime, terms, size, from, sortField, asc,
    //            interfaceVersion);
    //    String queryJsonStr = JSON.toJSONString(query);
    //    if (StringUtils.isBlank(index)) {
    //        index = getSearchRangeIndex("", startTime, endTime);
    //    }
    //    if (esUrl == null || "".equals(esUrl.trim())) {
    //        esUrl = ES_URL;
    //    }
    //    String url = esUrl + "/" + index + "/" + type + "/_search";
    //    EsResponse esResponse = searchEventResponse(url, queryJsonStr);
    //    setHits(eventData, type, esResponse.getHits());
    //    return eventData;
    //}
    //
    //
    //public static final String getSearchRangeIndex(String indexPrefix, long startTimeInMilliSeconds, long
    //        endTimeInMilliSeconds) {
    //    String err =
    //            format("end time[%d] must be after start time[%d].", startTimeInMilliSeconds, endTimeInMilliSeconds);
    //    Preconditions.checkArgument(endTimeInMilliSeconds >= startTimeInMilliSeconds, err);
    //    List<String> indexList = new ArrayList<>();
    //    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    //    Date end = new Date(endTimeInMilliSeconds);
    //    String et = indexPrefix + "-" + format.format(end);
    //    long time = startTimeInMilliSeconds;
    //    while (time <= endTimeInMilliSeconds) {
    //        Date t = new Date(time);
    //        indexList.add(indexPrefix + "-" + format.format(t));
    //        time += ONE_DAY_MILLISECOND;
    //    }
    //    if (!indexList.contains(et)) {
    //        indexList.add(et);
    //    }
    //    String index = StringUtils.join(indexList.iterator(), ",");
    //    return index;
    //}

}
