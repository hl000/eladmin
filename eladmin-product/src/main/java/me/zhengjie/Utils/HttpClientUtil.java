package me.zhengjie.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zaizhongyang
 * @description
 * @date 2017/3/5
 */
@Component
public class HttpClientUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);
    //最大连接数
    private static int maxTotal = 400;
    //并发数
    private static int defaultMaxPerRoute = 200;
    //创建连接的最长时间
    private static int connectTimeout = 3000;
    //从连接池中获取到连接的最长时间
    private static int connectionRequestTimeout = 1000;
    //数据传输的最长时间，5秒
    private static int socketTimeout = 250*1000;
//    private static int socketTimeout = 5*1000;
    //数据传输的最长时间，25秒
    private static int socketTimeoutLong = 250*1000;
//    private static int socketTimeoutLong = 25*1000;
    //连接空闲，调整为15秒，小于kdls的20秒，单位：秒
    private static int connectionIdleTimeout = 15;
    private static CloseableHttpClient httpClient;
    private static CloseableHttpClient httpClientLong;  ///数据传输的最长时间为25秒
    private static CloseableHttpClient httpsClient;
    private static CloseableHttpClient httpsClientUnsafe;
    private static IdleConnectionMonitorThread connectionMonitorThread;

    //http请求url开始
    private static final String HTTP_PRE = "http://";
    //https请求url开始
    private static final String HTTPS_PRE = "https://";

    static {
        LOG.info("Http Client init, maxTotal:{}, defaultMaxPerRoute:{}, connectionIdleTimeout:{}", maxTotal, defaultMaxPerRoute, connectionIdleTimeout);
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //最大连接数
        httpClientConnectionManager.setMaxTotal(maxTotal);
        //并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeout)
                .setExpectContinueEnabled(true).build();
        RequestConfig requestConfigLong = RequestConfig.custom().setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeoutLong)
                .setExpectContinueEnabled(true).build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(httpClientConnectionManager).build();
        httpClientLong = HttpClients.custom().setDefaultRequestConfig(requestConfigLong)
                .setConnectionManager(httpClientConnectionManager).build();
        httpsClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
                .setDefaultRequestConfig(requestConfig).setConnectionManager(httpClientConnectionManager).build();
        httpsClientUnsafe = HttpClients.custom().setSSLSocketFactory(createUnsafeSSLConnSocketFactory())
                .setDefaultRequestConfig(requestConfig).setMaxConnTotal(maxTotal).setMaxConnPerRoute(defaultMaxPerRoute).build();
        // 单独线程做空闲连接监控和处理
        connectionMonitorThread = new IdleConnectionMonitorThread(httpClientConnectionManager,
                connectionIdleTimeout);
        connectionMonitorThread.start();
    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params, boolean longFlag) {
        return doGet(url, params, null, longFlag);
    }
    /**
     * 发送 GET 请求（HTTP），K-V形式
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, params, null);
    }
    /**
     * 发送 GET 请求（HTTP），K-V形式，可以添加头部
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params, Map<String, String> headerMap) {
        return doGet(url, params, headerMap, false);
    }
    /**
     * 发送 GET 请求（HTTP），K-V形式，可以添加头部
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params, Map<String, String> headerMap, boolean longFlag) {
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(params.get(key));
                i++;
            }
        }
        apiUrl += param;
        String result = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(apiUrl);
        if (headerMap != null && headerMap.size() > 0) {
            for (Map.Entry<String, String> header : headerMap.entrySet()) {
                httpGet.setHeader(header.getKey(), header.getValue());
            }
        }
        try {
            if (longFlag) response = httpClientLong.execute(httpGet);
            else response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            LOG.error("----http get请求失败！", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("----http post关闭流失败！", e);
                }
            }
        }
        return result;
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式，返回字节数组
     * @param url
     * @param params
     * @return
     */
    public static byte[] doGetToStream(String url, Map<String, Object> params) {
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        byte[] result = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(apiUrl);
        try {
            response = httpClient.execute(httpGet);
            result = readInputStream(response.getEntity().getContent());
        } catch (IOException e) {
            LOG.error("----http get请求失败！", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("----http post关闭流失败！", e);
                }
            }
        }
        return result;
    }

    //读取InputStream，转为byte数组
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inputStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
    /**
     * 根据url的开始决定是发送http还是https，发送 POST 请求，JSON形式
     */
    public static String doPostByUrlPre(String apiUrl, String json, Map<String, String> headerMap){
        if (apiUrl.startsWith(HTTP_PRE)) return doPost(apiUrl, json, headerMap);
        if (apiUrl.startsWith(HTTPS_PRE)) return doPostSSL(apiUrl, json, headerMap);
        LOG.error("url:[{}] is error", apiUrl);
        return null;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
        return doPost(apiUrl, params, null, false);
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params, Map<String, String> headerMap, boolean longFlag) {
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        if (headerMap != null && headerMap.size() > 0) {
            for (Map.Entry<String, String> header : headerMap.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }
        try {
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            if (longFlag) response = httpClientLong.execute(httpPost);
            else response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            LOG.error("----http post请求失败！", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("----http post关闭流失败！", e);
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     * @param apiUrl
     * @param json json对象
     * @param headerMap 请求头
     * @return
     */
    public static String doPost(String apiUrl, String json, Map<String, String> headerMap) {
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        if (headerMap != null && headerMap.size() > 0) {
            for (Map.Entry<String, String> header : headerMap.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }
        try {
            StringEntity stringEntity = new StringEntity(json, "UTF-8");//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            LOG.error("----http post json请求失败！请求url为：{}，请求json为：{}", apiUrl, json, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("----http post json关闭流失败！", e);
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     * @param apiUrl
     * @param params 请求体对象
     * @param headerMap 请求头
     * @return
     */
    public static File doPostToStream(String apiUrl, Map<String, Object> params, Map<String, String> headerMap, String fileUrl) {
        byte[] result = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        if (headerMap != null && headerMap.size() > 0) {
            for (Map.Entry<String, String> header : headerMap.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }
        try {
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(null == params ? 0 : params.size());
            if (null != params && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (null != entry.getValue()) {
                        NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                        pairList.add(pair);
                    }
                }
            }

            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = readInputStream(entity.getContent());

            AtomicReference<String> fileName = new AtomicReference<>("");
            Arrays.stream(response.getAllHeaders()).forEach(header -> {
                if(header.getName().equals("Content-Disposition")) {
                    fileName.set(header.getValue().split("=")[1]);
                }
            });
            File a = new File(fileUrl + fileName.get());
            if(a.exists()) {
                a.delete();
            }
            a.createNewFile();
            OutputStream is = new FileOutputStream(a);
            BufferedOutputStream bos = new BufferedOutputStream(is);
            bos.write(result);
            bos.close();
            is.close();
            return a;
        } catch (Exception e) {
            LOG.error("----http post json请求失败！请求url为：{}，请求params为：{}", apiUrl, params, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("----http post 关闭流失败！", e);
                }
            }
        }
        return null;
    }


    /**
     * 发送 POST 请求（HTTP），K-V形式，参数中有文件
     * @param apiUrl API接口URL
     * @return
     */
    public static String doPostFile(String apiUrl, Map<String, Object> params) {
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("UTF-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //	字节传输http请求头(application/json)
            MultipartFile multipartFile = null;
            ContentType stringContentType = ContentType.create("application/json", Charset.forName("UTF-8"));
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() instanceof MultipartFile) {
                    multipartFile = (MultipartFile)entry.getValue();
                    //文件传输http请求头(multipart/form-data)
                    builder.addBinaryBody(entry.getKey(), multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA,
                            multipartFile.getOriginalFilename());// 文件流
                } else {
                    if (null != entry.getValue()) {
                        builder.addTextBody(entry.getKey(), entry.getValue().toString(), ContentType.APPLICATION_FORM_URLENCODED);
                    }
                }
            }
            httpPost.setEntity(builder.build());
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            LOG.error("----http post请求失败！", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("----http post关闭流失败！", e);
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPostSSL(String apiUrl, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(null == params ? 0 : params.size());
            if (null != params && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (null != entry.getValue()) {
                        NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                        pairList.add(pair);
                    }
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpsClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            LOG.error("----https post请求失败！", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("----https post 关闭流失败！", e);
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     * @param apiUrl API接口URL
     * @param json JSON对象
     * @param headerMap 请求头
     * @return
     */
    public static String doPostSSL(String apiUrl, Object json, Map<String, String> headerMap) {

        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;
        if (headerMap != null && headerMap.size() > 0) {
            for (Map.Entry<String, String> header : headerMap.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }
        try {
            //解决中文乱码问题
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpsClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            LOG.error("----https post json请求失败！", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("----http post json关闭流失败！", e);
                }
            }
        }
        return httpStr;
    }
    /**
     * 发送 不安全的https请求，SSL POST 请求（HTTPS），JSON形式
     * 当https没有证书时
     * @param apiUrl API接口URL
     * @param json JSON对象
     * @param headerMap 请求头
     * @return
     */
    public static String doPostUnSafeSSL(String apiUrl, Object json, Map<String, String> headerMap) {

        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;
        if (headerMap != null && headerMap.size() > 0) {
            for (Map.Entry<String, String> header : headerMap.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }
        try {
            //解决中文乱码问题
            StringEntity stringEntity = new StringEntity(null == json ? "" : json.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpsClientUnsafe.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            LOG.error("----https post json请求失败！", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("----http post json关闭流失败！", e);
                }
            }
        }
        return httpStr;
    }

    /**
     * 创建一个SSL信任所有证书的httpClient对象
     *
     * @return
     */
    public static SSLConnectionSocketFactory createUnsafeSSLConnSocketFactory() {
        try {
            // 信任所有
            SSLContext sslContext = new org.apache.http.ssl.SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            return new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        } catch (Exception e) {
            LOG.error("createUnsafeSSLConnSocketFactory appear error:", e);
        }
        return SSLConnectionSocketFactory.getSystemSocketFactory();
    }

    /**
     * 创建SSL安全连接，信任所有
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory factory = null;
        try {
            SSLContext sslcontext = SSLContexts.custom().useSSL().build();
            sslcontext.init(null, new X509TrustManager[] { new HttpsTrustManager() }, new SecureRandom());
            factory = new SSLConnectionSocketFactory(sslcontext,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        } catch (GeneralSecurityException e) {
            LOG.error("createSSLConnSocketFactory appear error:", e);
        }
        return factory;
    }

    /**
     * Shutdown the httpUtil
     *
     * @throws IOException IOException
     */
    public static void shutdown() throws IOException {
        connectionMonitorThread.shutdown();
        httpClient.close();
        httpClientLong.close();
        httpsClient.close();
        httpsClientUnsafe.close();
    }

    /**
     * A Connection monitor thread which close the idle connections in HttpClientConnectionManager
     */
    public static class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connectionManager;
        private final int connectionIdleTimeout;
        private volatile boolean shutdown;

        IdleConnectionMonitorThread(HttpClientConnectionManager connectionManager, int connectionIdleTimeout) {
            super();
            this.connectionManager = connectionManager;
            this.connectionIdleTimeout = connectionIdleTimeout;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(500);
                        // close expired connections
                        connectionManager.closeExpiredConnections();
                        // close connections that have been idle longer than specific sec
                        connectionManager.closeIdleConnections(connectionIdleTimeout, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException e) {
                LOG.error("----httpclient连接池空闲连接管理出现异常。");
            }
        }

        void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
