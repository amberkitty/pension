package com.amber.quant;

public interface APIParam {

    String API_KEY = "63c6dc1d-985d-4acc-bd3f-1c29e51c2508";

    String API_SECRET = "53B529381D1FD1756F2C2114555560DF";

    String PASSWORD = "Amber275&";

    String SIMULATE_API_KEY = "f47f736a-f0af-4600-84d0-952bd0d2dca4";

    String SIMULATE_API_SECRET = "42F41E76312F0380587FF31358B28AE9";

    //下单
    String TRADE_ORDER = "/api/v5/trade/order";

    String COPY_TRADE_CURRENT_LEADER = "/api/v5/copytrading/current-lead-traders";

    //获取订单信息
    String GET_TRADE_ORDERS = "/api/v5/trade/order";

    String GET_ACCOUNT_POSITIONS = "/api/v5/account/positions";
}
