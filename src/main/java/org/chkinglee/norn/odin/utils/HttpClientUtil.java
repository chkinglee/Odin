package org.chkinglee.norn.odin.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * 发起http请求的工具
 * 网上类似的Util很多
 *
 * @Author: lilinzhen
 * @Version: 2020/5/30
 **/

public class HttpClientUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final HttpClient CLIENT = new HttpClient(new MultiThreadedHttpConnectionManager());

    static {
        CLIENT.getParams().setUriCharset("UTF-8");
        CLIENT.getParams().setContentCharset("UTF-8");
        CLIENT.getParams().setConnectionManagerTimeout(3000); // 该值就是连接不够用的时候等待超时时间，不能太大
        CLIENT.getParams().setSoTimeout(20000); // 数据读取超时时间
        CLIENT.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));
        CLIENT.getHttpConnectionManager().getParams().setConnectionTimeout(10000); // 建立连接的超时时间
        CLIENT.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(300); // 每个目标机器的默认最大连接数
        CLIENT.getHttpConnectionManager().getParams().setMaxTotalConnections(900); // 最大连接数
        CLIENT.getHttpConnectionManager().closeIdleConnections(60000);
    }

    public static String get(String url) throws Exception {
        GetMethod get = new GetMethod(url);
        return executeMethod(get);
    }

    public static String get(String url, Map<String, String> headerMap) throws Exception {
        GetMethod get = new GetMethod(url);
        if (headerMap != null) {
            for (String headerName : headerMap.keySet()) {
                get.setRequestHeader(headerName, headerMap.get(headerName));
            }
        }

        return executeMethod(get);
    }

    public static String post(String url, Map<String, String> paramMap) throws Exception {
        PostMethod post = new PostMethod(url);
        if (paramMap != null) {
            for (String paramName : paramMap.keySet()) {
                post.addParameter(paramName, paramMap.get(paramName));
            }
        }
        return executeMethod(post);
    }

    public static String post(String url, Map<String, String> paramMap, Map<String, String> headerMap)
            throws Exception {
        PostMethod post = new PostMethod(url);
        if (paramMap != null) {
            for (String paramName : paramMap.keySet()) {
                post.addParameter(paramName, paramMap.get(paramName));
            }
        }
        if (headerMap != null) {
            for (String headerName : headerMap.keySet()) {
                post.setRequestHeader(headerName, headerMap.get(headerName));
            }
        }
        // DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy
        // .IGNORE_COOKIES);
        post.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        return executeMethod(post);
    }

    public static String postJson(String url, String json) throws Exception {
        PostMethod post = new PostMethod(url);
        RequestEntity entity = new StringRequestEntity(json, "application/json", "UTF-8");
        post.setRequestEntity(entity);
        return executeMethod(post);
    }

    public static String postJson(String url, String json, Map<String, String> headerMap)
            throws Exception {
        PostMethod post = new PostMethod(url);
        RequestEntity entity = new StringRequestEntity(json, "application/json", "UTF-8");
        post.setRequestEntity(entity);

        if (headerMap != null) {
            for (String headerName : headerMap.keySet()) {
                post.setRequestHeader(headerName, headerMap.get(headerName));
            }
        }

        return executeMethod(post);
    }

    public static String putJson(String url, String json, Map<String, String> headerMap)
            throws Exception {
        PutMethod put = new PutMethod(url);
        RequestEntity entity = new StringRequestEntity(json, "application/json", "UTF-8");
        put.setRequestEntity(entity);

        if (headerMap != null) {
            for (String headerName : headerMap.keySet()) {
                put.setRequestHeader(headerName, headerMap.get(headerName));
            }
        }

        return executeMethod(put);
    }

    public static String postBody(String url, Map<String, String> paramMap) throws Exception {
        PostMethod post = new PostMethod(url);
        String response = null;
        if (paramMap != null) {
            int i = 0;
            NameValuePair[] paramPair = new NameValuePair[paramMap.size()];
            for (String paramName : paramMap.keySet()) {
                NameValuePair nameValuePair = new NameValuePair(paramName, paramMap.get(paramName));
                paramPair[i++] = nameValuePair;
            }
            post.setRequestBody(paramPair);
            response = executeMethod(post);
        }
        return response;
    }

    public static String delete(String url) throws Exception {
        DeleteMethod delete = new DeleteMethod(url);
        return executeMethod(delete);
    }

    public static String executeMethod(HttpMethod method) throws Exception {
        // CLIENT.getHttpConnectionManager().closeIdleConnections(60000);
        String url = method.getURI().getURI();
        InputStream is = null;
        try {
            int statusCode = CLIENT.executeMethod(method);
            LOG.info(method.getName() + " url(" + url + ") : " + method.getStatusLine());
            // if (statusCode != HttpStatus.SC_OK) {
            //     LOG.warn(statusCode+method.getName() + " url(" + url + ") failed: " + method.getStatusLine());
            // }
            String charset = null;
            if (method instanceof HttpMethodBase) {

                charset = ((HttpMethodBase) method).getResponseCharSet();
            }
            charset = StringUtils.defaultIfEmpty(charset, "UTF-8");
            is = method.getResponseBodyAsStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            IOUtils.copy(is, bos);
            return bos.toString(charset);
        } catch (Exception e) {
            LOG.error(method.getName() + " url(" + url + ") error: " + e.getMessage(), e);
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            method.releaseConnection();
        }
    }

}

