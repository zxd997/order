package com.zxd.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {
    //服务的降级 commandKey默认为方法名
    @HystrixCommand(fallbackMethod = "fallback",commandKey = "getProductInfoList",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1000"),//设置超时时间 3s 默认是1s
            //断路器
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//是否启用熔断器，默认是TURE
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),//当在配置时间窗口内达到此数量的失败后，进行短路。默认20个
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//短路多久以后开始尝试是否恢复，默认5s
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),//出错百分比阈值，当达到此阈值后，开始短路。默认50%
    })
    @GetMapping("/getProductInfoList")
    public String getProductInfoList(Integer number){
        if (number%2==0){
            return "好了";
        }
        RestTemplate restTemplate = new RestTemplate();
        String s = restTemplate.postForObject("http://localhost:8082/product/listForOrder", Arrays.asList("002"), String.class);
        return s;
    }
    //自己内部的降级
    @HystrixCommand
    @GetMapping("/getProductInfoList1")
    public String getProductInfoList1(){
        throw new RuntimeException("发送异常了");
    }
    //可以调用的
    @HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/sss")
    public String getProductInfoList12(){
        return "sss";
    }
    private String fallback(Integer number){
        return "太拥挤了，请稍后再试~~";
    }
    private String defaultFallback(Integer number){
        return "异常啦~ 请等待";
    }
}
