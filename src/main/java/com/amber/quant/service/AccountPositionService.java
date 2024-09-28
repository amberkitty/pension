package com.amber.quant.service;

import org.springframework.stereotype.Service;

@Service
public class AccountPositionService implements IAccountPositionService{
    @Override
    public void test() {
        System.out.println("定时任务执行:"+System.currentTimeMillis());
    }

    @Override
    public void tradeOrder() {

    }


}
