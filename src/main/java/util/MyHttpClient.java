package util;


import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import util.ssl.MySSLSocketFactory;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


public class MyHttpClient {

    private static Logger LOG = Logger.getLogger(MyHttpClient.class.getName());
    private static MyHttpClient INSTANCE = null;
    private static CookieStore defaultCookieStore = new BasicCookieStore();
    private static HttpClient httpClient;
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .build();

    static {
        httpClient = HttpClients.custom()
                .setSSLContext(MySSLSocketFactory.createSSLContext())
                .setProxy(new HttpHost("127.0.0.1", 8888))
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(defaultCookieStore)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                .build();
    }

    private MyHttpClient() {

    }

    public static MyHttpClient getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (MyHttpClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyHttpClient();
                }
            }
        }
        return INSTANCE;
    }

    public String getCookies() {
        StringBuilder ret = new StringBuilder(256);
        List<Cookie> cookies = defaultCookieStore.getCookies();
        if (cookies != null) {
            cookies.forEach(ck -> ret.append(ck.getName() + "=" + ck.getValue() + ";"));
        }
        return ret.toString();
    }

    /**
     * httpclient 默认自动跳转
     *
     * @param url
     * @param redirect
     * @return
     */
    public CloseableHttpResponse doGet(String url, boolean redirect) {
        HttpGet httpGet;
        CloseableHttpResponse httpResponse = null;
        try {
            httpGet = new HttpGet(url);
            if (!redirect)
                httpGet.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
            httpResponse = (CloseableHttpResponse) httpClient.execute(httpGet);
            LOG.info(httpGet.getURI().toString() + "\t" + httpResponse.getStatusLine());
            LOG.info("Cookie:" + getCookies());
            if (httpResponse.containsHeader("Location")) {
                //should redict ...
                String redict = httpResponse.getFirstHeader("Location").getValue().equals("/") ? "https://wx2.qq.com/"
                        : httpResponse.getFirstHeader("Location").getValue();
                LOG.info("Location:" + redict);
            }
            return httpResponse;
        } catch (Exception e) {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("Exception: " + url);
            System.out.println(e.getMessage());
        } finally {

        }
        return null;
    }


    public CloseableHttpResponse doPost(String url, List<NameValuePair> params, String paramStr, String contenttype) {
        HttpPost httpPost;
        CloseableHttpResponse httpResponse = null;
        try {
            httpPost = new HttpPost(url);
            if (params != null)
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            else if (contenttype != null) {
                httpPost.addHeader("Content-type", contenttype);
                httpPost.setEntity(new StringEntity(paramStr, ContentType.getByMimeType(contenttype)));
            } else {
                httpPost.addHeader("Content-type", "application/json; charset=utf-8");
                httpPost.setEntity(new StringEntity(paramStr, ContentType.getByMimeType("application/json;charset=UTF-8")));
            }
            httpResponse = (CloseableHttpResponse) httpClient.execute(httpPost);
            LOG.info(httpPost.getURI().toString() + "\t" + httpResponse.getStatusLine());
            LOG.info("Cookie:" + getCookies());
            return httpResponse;
        } catch (IOException e) {
            if (httpResponse != null)
                try {
                    httpResponse.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            e.printStackTrace();
        } finally {

        }
        return null;
    }
}
