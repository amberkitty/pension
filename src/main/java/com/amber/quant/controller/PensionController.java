package com.amber.quant.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amber.quant.model.Token;
import com.amber.quant.service.ITokenService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.parser.TokenList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/pension")
public class PensionController {

    @Autowired
    ITokenService tokenService;

    @GetMapping("/getTotalBalance")
    public String getTotalBalance() throws Exception{
        return tokenService.getTotalBalance();
    }

    @GetMapping("/getTotalPeople")
    public String getTOtalPeople() {
        return tokenService.getTotalPeople();
        //return "2";
    }

    @GetMapping("/tokenList")
    public ResponseEntity<List<Token>> queryTokenList() {
        return ResponseEntity.ok(tokenService.getTokenList());
    }

    public static void main(String[] args) {
        String address = "9qMknujRc8eqBZ6gSrypjYyzNNpwiwASxocKdAfg563C";
        Set<String> uniqueAddresses = new HashSet<>();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // 获取签名
            String jsonSignatures = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"getSignaturesForAddress\",\"params\":[\"" + address + "\"]}";
            String signaturesResponse = sendRequest(client, jsonSignatures);
            // 解析签名并获取交易
            Set<String> parsedSignatures = parsedTransferAddresses(signaturesResponse);

            // 假设你解析了签名，循环获取每个交易
            for (String signature : parsedSignatures) {
                String jsonTransaction = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"getConfirmedTransaction\",\"params\":[\"" + signature + "\"]}";
                String transactionResponse = sendRequest(client, jsonTransaction);
                // 解析交易中的转账地址并添加到集合
                uniqueAddresses.addAll(parsedTransferAddresses(transactionResponse));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Unique transfer addresses: " + uniqueAddresses.size());
    }

    private static String sendRequest(CloseableHttpClient client, String jsonInputString) throws Exception {
        HttpPost post = new HttpPost("https://api.mainnet-beta.solana.com");
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(jsonInputString));

        try (CloseableHttpResponse response = client.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    // 解析方法需要根据实际响应格式实现
    private static Set<String> parsedTransferAddresses(String transactionResponse) {
        Set<String> addresses = new HashSet<>();
        // 解析逻辑
        return addresses;
    }
}
