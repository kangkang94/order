package com.alipay.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ClientController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate1;

    @RequestMapping(value = "getProductMsg", method = RequestMethod.GET)
    public String get() {
        //1.第一种方式（直接使用restTemplate，url写死）
        RestTemplate restTemplate = new RestTemplate();
        String message1 = restTemplate.getForObject("http://127.0.0.1:8080/msg", String.class);

        //2.第二种方式（通过loadBalancerClient与注册中心通信，通过服务应用名负载均衡获取ip，port）
        ServiceInstance serviceInstance = loadBalancerClient.choose("PRODUCT");
        String url = String.format("http://%s:%s/msg", serviceInstance.getHost(), serviceInstance.getPort());
        String message2 = restTemplate.getForObject(url, String.class);

        //第三种（通过@loadbalanced 将RestTemplate配置进IOC容器）
        String message3 = restTemplate1.getForObject(String.format("http://PRODUCT/msg"), String.class);

        return message3;
    }


}
