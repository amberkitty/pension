package com.amber.quant.model;

import lombok.Data;

@Data
public class SolanaParams {

    private String jsonrpc;

    private int id;

    private String method;

    private String[] params;
}
