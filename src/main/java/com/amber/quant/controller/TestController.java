package com.amber.quant.controller;


import org.apache.catalina.connector.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class TestController {

    @GetMapping("/start")
    public String queryCategoryOverview() {
        return "hi";
    }
}
