package com.alipay.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@DefaultProperties(defaultFallback = "defallback")
public class HystrixController {

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//设置熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求数达到后才计算
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//熔断时间
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//错误率
    })
    @RequestMapping(value = "/getProductInfoList", method = RequestMethod.GET)
    public String getProductInfoList(@RequestParam("number") Integer number) {

        if (number % 2 == 0) {
            return "success";
        }

        RestTemplate restTemplate = new RestTemplate();
        String productInfolist = restTemplate.postForObject("http://127.0.0.1:8082/product/listForOrder",
                Arrays.asList("157875196366160022", "157875227953464068"), String.class);

        return productInfolist;

    }

    private String defallback() {
        return "太拥挤了，请稍后访问~";
    }

}
