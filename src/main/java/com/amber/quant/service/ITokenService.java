package com.amber.quant.service;

import com.amber.quant.model.Token;

import java.util.List;

public interface ITokenService {

    List<Token> getTokenList();

    String getTotalBalance() throws Exception;
}
