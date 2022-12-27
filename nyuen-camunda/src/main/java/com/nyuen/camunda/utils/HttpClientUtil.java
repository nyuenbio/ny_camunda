package com.nyuen.camunda.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public class HttpClientUtil {

    public static String httpPut(String url, List<NameValuePair> nvps) {
        StringBuffer response = new StringBuffer("");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPut httpPut = new HttpPut(url);
            if (nvps != null) {
                httpPut.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            }
            CloseableHttpResponse httpPutResponse = httpClient.execute(httpPut);
            try {
                org.apache.http.HttpEntity entity = httpPutResponse.getEntity();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        entity.getContent(), Charset.forName("UTF-8")));
                String s;
                while ((s = br.readLine()) != null) {
                    response.append(s);
                }
                EntityUtils.consume(entity);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpPutResponse.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response.toString();
    }

    public static String httpPost(String url, List<NameValuePair> nvps) {
        StringBuilder serverResponse = new StringBuilder();
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpPost httpPost = new HttpPost(url);
                if (nvps != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps,
                            HTTP.UTF_8));
                }
                CloseableHttpResponse response = httpclient.execute(httpPost);
                try {
                    HttpEntity entity = response.getEntity();

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(entity.getContent(),
                                    Charset.forName("UTF-8")));
                    String s;
                    while ((s = br.readLine()) != null) {
                        serverResponse.append(s);
                    }
                    EntityUtils.consume(entity);

                } catch (Exception e) {
                    // e.printStackTrace();

                    System.out.println(e.getMessage());

                } finally {
                    response.close();

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println(e.getMessage());

            } finally {

                httpclient.close();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());

        }

        return serverResponse.toString();
    }

    public static String httpPostJson(String url, JSONObject params) {
        StringBuilder serverResponse = new StringBuilder();
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-Type", "application/json");
                if(params.get("Authorization") != null){
                    httpPost.setHeader("Authorization", params.get("Authorization").toString());
                }
                if (params != null) {
                    StringEntity entity = new StringEntity(params.toString(),"utf-8");
                    httpPost.setEntity(entity);
                }
                CloseableHttpResponse response = httpclient.execute(httpPost);
                try {
                    HttpEntity entity = response.getEntity();

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(entity.getContent(),
                                    Charset.forName("UTF-8")));
                    String s;
                    while ((s = br.readLine()) != null) {
                        serverResponse.append(s);
                    }
                    EntityUtils.consume(entity);

                } catch (Exception e) {
                    // e.printStackTrace();

                    System.out.println(e.getMessage());

                } finally {
                    response.close();

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println(e.getMessage());

            } finally {

                httpclient.close();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());

        }

        return serverResponse.toString();
    }

    public static String httpGet(String url) {

        StringBuilder serverResponse = new StringBuilder();
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse response = httpclient.execute(httpGet);
                try {
                    HttpEntity entity = response.getEntity();

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(entity.getContent(),
                                    Charset.forName("UTF-8")));
                    String s;
                    while ((s = br.readLine()) != null) {
                        serverResponse.append(s);
                    }
                    EntityUtils.consume(entity);

                } catch (Exception e) {
                    // e.printStackTrace();

                } finally {
                    response.close();

                }
            } catch (Exception e) {
                // e.printStackTrace();

            } finally {

                httpclient.close();
            }
        } catch (Exception e) {
            // e.printStackTrace();

        }

        return serverResponse.toString();
    }

    public static boolean exists(String URLName) {

        try {

            // 设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。

            HttpURLConnection.setFollowRedirects(false);

            // 到 URL 所引用的远程对象的连接

            HttpURLConnection con = (HttpURLConnection) new URL(URLName)

                    .openConnection();

            /*
             * 设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE
             * 以上方法之一是合法的，具体取决于协议的限制。
             */

            con.setRequestMethod("HEAD");

            // 从 HTTP 响应消息获取状态码

            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);

        } catch (Exception e) {

            return false;

        }

    }

    public static boolean testInternet(String url) {
        boolean success = false;

        HttpURLConnection http;

        try {
            URL urlAddress = new URL(url);
            http = (HttpURLConnection) urlAddress.openConnection();
            http.setConnectTimeout(2500);
            if (http.getResponseCode() == 200) {
                success = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return success;

    }

    /**
     * post请求
     *
     * @param url
     * @param json
     * @return
     */
    public static String doPost(String url, String json) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        String response = null;
        try {
            StringEntity s = new StringEntity(json, "UTF-8");
            // s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = res.getEntity();
                response = EntityUtils.toString(res.getEntity());// 返回json格式：
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            post.abort();
        }
        return response;
    }

    public static String post(String url, String jsonString) {
        DefaultHttpClient client = new DefaultHttpClient();
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setConfig(requestConfig);

            httpPost.setEntity(new StringEntity(jsonString, Charset.forName("UTF-8")));
            response = client.execute(httpPost);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String SendHttpsPOST(String url, String json) throws URISyntaxException {
        String result = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost request;
        try {
            request = new HttpPost(new URI(url));
            if (json != null) {
                StringEntity s = new StringEntity(json.toString(), "utf-8");
                s.setContentType("application/json");
                request.setEntity(s);
            }

            //设置SSLContext
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, null);

            //打开连接
            //要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext);

            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            HttpResponse response = client.execute(request);
            int returncode = response.getStatusLine().getStatusCode();
            System.out.println("Response Code:" + returncode);

            if (returncode == 200) {
                Header[] header = response.getHeaders("token");
                if (header.length > 0) {
                    for (Header h : header) {
                        System.out.println(h.getName() + " " + h.getValue());
                    }
                }
                HttpEntity entity = response.getEntity();
                long contentLen = entity.getContentLength();
                if (contentLen == 0) {
                    return "User Has No Authority";
                }
                String charset = EntityUtils.getContentCharSet(entity);
                InputStreamReader isr = new InputStreamReader(entity.getContent(), charset);
                StringBuffer sb = new StringBuffer();
                char[] ct = new char[64];
                int len = 0;
                while ((len = isr.read(ct)) != -1) {
                    String sst = new String(ct);
                    sst = sst.substring(0, len).trim();
                    sb.append(sst);
                }
                result = sb.toString().replace("\\\"", "\"");
                result = result.substring(1, result.length() - 1);
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }


    public static void main(String[] args) throws Exception {
        System.out.println(HttpClientUtil.httpGet("https://putuo.opg-iot.com/brain/usersystem/user/login.do?loginName=gis&password=B12C645E186F93509B1C0C36552EC334"));
    }

    static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
