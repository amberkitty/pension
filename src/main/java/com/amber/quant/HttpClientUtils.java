package com.amber.quant;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amber.quant.model.SolanaParams;
import com.amber.quant.utils.HeaderBuilderHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

    public static String doHttpGet(String url, Map<String,String> params, Map<String,String> headParams) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String result = null;
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        //1.获取httpclient
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
        //接口返回结果
        CloseableHttpResponse response = null;


        String paramStr = null;
        try {
//            List<BasicNameValuePair> paramsList = new ArrayList<BasicNameValuePair>();
//            for (String key : params.keySet()) {
//                paramsList.add(new BasicNameValuePair(key,params.get(key)));
//            }
//            paramStr = EntityUtils.toString(new UrlEncodedFormEntity(paramsList));
//            //拼接参数
//            StringBuffer sb = new StringBuffer();
//            sb.append(url);
//            sb.append("?");
//            sb.append(paramStr);

            //2.创建get请求
            HttpGet httpGet = new HttpGet(url);

            //3.设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
            httpGet.setConfig(requestConfig);

            /*此处可以添加一些请求头信息，例如：*/
            httpGet.addHeader("content-type","text/xml");
            for (String head : headParams.keySet()) {
                httpGet.addHeader(head,headParams.get(head));
            }

            //4.提交参数
            response = httpClient.execute(httpGet);





            //5.得到响应信息
            int statusCode = response.getStatusLine().getStatusCode();

            //6.判断响应信息是否正确
            if (HttpStatus.SC_OK != statusCode) {
                //终止并抛出异常
                //httpGet.abort();
                //throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            //7.转换成实体类
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                result = EntityUtils.toString(entity);
            }
            EntityUtils.consume(entity);
            } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //8.关闭所有资源连接
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String doPost(String url, String jsonString, Map<String,String> headParams) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException{
        String result = null;
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        //1. 获取httpclient对象
        //1.获取httpclient
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
        CloseableHttpResponse response = null;

        try {
            //2. 创建post请求
            HttpPost httpPost = new HttpPost(url);
            //3.设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(100000).setConnectTimeout(100000).build();
            httpPost.setConfig(requestConfig);

            //4.提交参数发送请求
//            List<BasicNameValuePair> paramsList = new ArrayList<>();
//            if (params != null && !params.isEmpty()) {
//                for (String key : params.keySet()) {
//                    paramsList.add(new BasicNameValuePair(key,params.get(key)));
//                }
//            }

            //UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(paramsList, HTTP.UTF_8);
            httpPost.setEntity(new StringEntity(jsonString));
            if (headParams != null && !headParams.isEmpty()) {
                //设置请求头
                for (String head : headParams.keySet()) {
                    httpPost.addHeader(head, headParams.get(head));
                }
            }

            response = httpClient.execute(httpPost);

            //5.得到响应信息
            int statusCode = response.getStatusLine().getStatusCode();

            //6. 判断响应信息是否正确
            if (HttpStatus.SC_OK != statusCode) {
                //结束请求并抛出异常
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + httpPost.toString());
            }

            //7. 转换成实体类
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            //8. 关闭所有资源连接
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public String getBalance() throws Exception{
        String baseUrl = "https://api.devnet.solana.com";
        HeaderBuilderHelper header = new HeaderBuilderHelper();
        Map<String, String> params = header.solanaHeader();

        HttpClientUtils httpClientUtils = new HttpClientUtils();
        SolanaParams solanaParams = new SolanaParams();
        solanaParams.setParams(new String[]{"9qMknujRc8eqBZ6gSrypjYyzNNpwiwASxocKdAfg563C"});
        solanaParams.setMethod("getBalance");
        solanaParams.setId(1);
        solanaParams.setJsonrpc("2.0");
        JSONObject jsonObject = JSON.parseObject(httpClientUtils.doPost(baseUrl, JSON.toJSONString(solanaParams), params));
        // 将Instant对象转换为UTC时区的ZonedDateTime对象
        System.out.println(jsonObject);
        return jsonObject.getJSONObject("result").get("value").toString();
    }

    public static void main(String args[]) throws Exception{
        //String baseUrl = "https://www.okx.com";

        //String endPoint = "/api/v5/account/instruments?instType=SWAP";
        //String endPoint = "/api/v5/trade/fills-history?instType=SWAP";
        //String endPoint = "/api/v5/asset/balances";
        //String endPoint = "/api/v5/asset/asset-valuation";
        //String endPoint = "/api/v5/account/balance";

        //获取精英交易员合约多空持仓人数比
        String endPoint = "/api/v5/rubik/stat/contracts/long-short-account-ratio-contract-top-trader?instId=BTC-USDT-SWAP&period=5m";

        //获取精英交易员合约多空持仓仓位比
        //String endPoint = "/api/v5/rubik/stat/contracts/long-short-position-ratio-contract-top-trader?instId=BTC-USDT-SWAP&period=15m";

        //获取合约多空持仓人数比
        //String endPoint = "/api/v5/rubik/stat/contracts/long-short-account-ratio-contract?instId=BTC-USDT-SWAP";

        //String endPoint = "/api/v5/trade/fills-archive";

        //获取当前带单员信息
        //String endPoint = "/api/v5/copytrading/lead-traders-history";

        //获取订单信息
        //String endPoint = APIParam.GET_TRADE_ORDERS + "?instId=BTC-USDT-SWAP";

        //
        //String endPoint = APIParam.GET_ACCOUNT_POSITIONS;

        //String endPoint = "/api/v5/trade/orders-history?instType=SWAP";

        //星辰社区 id：4CC714DF5A1691A0

        //String endPoint = "/priapi/v5/ecotrade/universals/relation/copier/list?instType=SWAP&timeZone=0&t="+System.currentTimeMillis();


        HeaderBuilderHelper header = new HeaderBuilderHelper();

        // Define the ISO 8601 formatter with milliseconds
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        String timestamp = Instant.now().atZone(ZoneId.of("UTC")).format(formatter);

        String sign = header.generate(timestamp, "GET", "", endPoint, false);
        Map<String, String> params = header.solanaHeader();

        HttpClientUtils httpClientUtils = new HttpClientUtils();

        //JSONObject jsonObject = JSON.parseObject(httpClientUtils.doHttpGet(baseUrl+endPoint, null, params));
//        Map<String, String> solanaParams = new HashMap<>();
//        solanaParams.put("jsonrpc", "2.0");
//        solanaParams.put("id", "1");
//        solanaParams.put("method", "getBalance");
//        solanaParams.put("address", JSONArray.toJSONString("A3uoP4PRRUc5Swnb3gqrKRSRKvcQXZmWa1gLLhqyb9m9"));
        String ss = httpClientUtils.getBalance();
        // 将Instant对象转换为UTC时区的ZonedDateTime对象
        //System.out.println(jsonObject);
        System.out.println(System.currentTimeMillis());
        //System.out.println(sign);


//        String address = "A3uoP4PRRUc5Swnb3gqrKRSRKvcQXZmWa1gLLhqyb9m9";
//        String jsonInputString = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"getBalance\",\"params\":[\"" + address + "\"]}";
//
//        URL url = new URL("https://api.devnet.solana.com");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setDoOutput(true);
//
//        try (OutputStream os = conn.getOutputStream()) {
//            os.write(jsonInputString.getBytes());
//        }
//
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
//            String responseLine;
//            StringBuilder response = new StringBuilder();
//            while ((responseLine = br.readLine()) != null) {
//                response.append(responseLine);
//            }
//            System.out.println("Balance: " + response.toString());
//        }
    }
}
