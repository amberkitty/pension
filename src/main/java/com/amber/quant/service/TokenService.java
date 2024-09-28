package com.amber.quant.service;

import com.amber.quant.HttpClientUtils;
import com.amber.quant.mapper.TokenMapper;
import com.amber.quant.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService implements ITokenService{
    @Autowired
    TokenMapper tokenMapper;

    @Override
    public List<Token> getTokenList() {
        return tokenMapper.getTokenList();
    }

    @Override
    public String getTotalBalance() throws Exception {
        double balance = Double.valueOf(new HttpClientUtils().getBalance().substring(0,3)) / 100;
        return String.valueOf(balance);
    }
}
