package com.alipay.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/env")
public class EnvController {

    @Value("${env}")
    private String env;

    @RequestMapping(value = "/env", method = RequestMethod.GET)
    public String print() {
        return env;
    }
}
