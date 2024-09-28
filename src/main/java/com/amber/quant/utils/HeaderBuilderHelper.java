package com.amber.quant.utils;

import com.amber.quant.APIParam;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HeaderBuilderHelper {

    /**
     * OK-ACCESS-KEY字符串类型的APIKey。
     *
     * OK-ACCESS-SIGN使用HMAC SHA256哈希函数获得哈希值，再使用Base-64编码（请参阅签名）。
     *
     * OK-ACCESS-TIMESTAMP发起请求的时间（UTC），如：2020-12-08T09:08:57.715Z
     *
     * OK-ACCESS-PASSPHRASE您在创建API密钥时指定的Passphrase。
     *
     *
     */
    public static Map<String,String> build(String timestamp, String sign, boolean isSimluate){
        Map<String,String> headerMap = new HashMap<>();
        String apiKey = APIParam.API_KEY;
        if(isSimluate){
            headerMap.put("x-simulated-trading","1");
            apiKey = APIParam.SIMULATE_API_KEY;
        }
        headerMap.put("OK-ACCESS-KEY", apiKey);
        headerMap.put("OK-ACCESS-SIGN",sign);
        headerMap.put("OK-ACCESS-TIMESTAMP", timestamp);
        headerMap.put("OK-ACCESS-PASSPHRASE",APIParam.PASSWORD);
        headerMap.put("Content-Type", "application/json");


        return headerMap;
    }

    public static Map<String,String> solanaHeader(){
        Map<String,String> headerMap = new HashMap<>();
//        String apiKey = APIParam.API_KEY;
//
//        headerMap.put("OK-ACCESS-KEY", apiKey);
//        headerMap.put("OK-ACCESS-SIGN",sign);
//        headerMap.put("OK-ACCESS-TIMESTAMP", timestamp);
//        headerMap.put("OK-ACCESS-PASSPHRASE",APIParam.PASSWORD);
        headerMap.put("Content-Type", "application/json");


        return headerMap;
    }
    public static Map<String,String> build(String accessToken,boolean isSimluate){
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer "+accessToken);
        if(isSimluate){
            headerMap.put("x-simulated-trading","1");
        }
        return headerMap;
    }


    /**
     * OK-ACCESS-SIGN的请求头是对timestamp + method + requestPath + body字符串（+表示字符串连接），以及SecretKey，使用HMAC SHA256方法加密，通过Base-64编码输出而得到的。
     *
     * 如：sign=CryptoJS.enc.Base64.stringify(CryptoJS.HmacSHA256(timestamp + 'GET' + '/users/self/verify', SecretKey))
     *
     * 其中，timestamp的值与OK-ACCESS-TIMESTAMP请求头相同，为ISO格式，如2020-12-08T09:08:57.715Z。
     *
     * method是请求方法，字母全部大写：GET/POST。
     *
     * requestPath是请求接口路径。如：/api/v5/account/balance
     *
     * body是指请求主体的字符串，如果请求没有主体（通常为GET请求）则body可省略。如：{"instId":"BTC-USDT","lever":"5","mgnMode":"isolated"}
     *
     *
     *
     * **/
    public static String generate(String timeStamp,String requestMethod,String requestBody,String requestPath, boolean isSimulate){
        String signStr = timeStamp + requestMethod + requestPath + requestBody ;
        String result = null;
        String api_secret = APIParam.API_SECRET;
        try {
            Mac hmac_sha256 = Mac.getInstance("HmacSHA256");
            if (isSimulate) {
                api_secret = APIParam.SIMULATE_API_SECRET;
            }
            SecretKeySpec secret_key = new SecretKeySpec(api_secret.getBytes(), "HmacSHA256");
            hmac_sha256.init(secret_key);
            result = Base64.getEncoder().encodeToString(hmac_sha256.doFinal(signStr.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return  result;
    }

    public static void main(String args[]) {
        System.out.println(LocalDateTime.now().toString());
    }
}
