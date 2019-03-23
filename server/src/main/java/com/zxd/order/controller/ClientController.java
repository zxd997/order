package com.zxd.order.controller;

import com.zxd.product.client.ProductClient;
import com.zxd.product.dto.ProductStock;
import com.zxd.product.model.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 方便演示用的
 * 一般用到两种通信方式 RestTemplate和Feign
 */
@RestController
@Slf4j
public class ClientController {
//    @Autowired
//    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private ProductClient productClient;
    //@Autowired
    //private RestTemplate restTemplate;
    @GetMapping("/getProductMsg")
    public String msg(){
        //RestTemplate restTemplate = new RestTemplate();
        //第一种方式(直接使用RestTemplate，url写死)
        //String msg = restTemplate.getForObject("http://localhost:8080/msg", String.class);
        //第二种方式（LoadBalancerClient，通过应用名获取url
//        ServiceInstance serviceInstance = loadBalancerClient.choose("PRODUCT");
//        String url = String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort())+"msg";
//        String msg = restTemplate.getForObject(url, String.class);
       //第三种方式(利用@LoadBalanced,可在restTemplate中使用应用名字)
        //String msg = restTemplate.getForObject("http://PRODUCT/msg", String.class);
        //Feign方式
        String msg = productClient.getMsg();
        log.info("response={}",msg);
        return msg;
    }
    @GetMapping("/get1")
    public String get1(){
        List<ProductInfo> listforOrder = productClient.getListforOrder(Arrays.asList("001", "003"));
        log.info("response={}",listforOrder);
        return "ok";
    }
    @GetMapping("/get2")
    public String get31(){
        List<ProductStock> productStockList = new ArrayList<>();
        ProductStock productStock = new ProductStock("001",1);
        ProductStock productStock1 = new ProductStock("003", 2);
        productStockList.add(productStock);productStockList.add(productStock1);
        productClient.decreaseStock(productStockList);
        log.info("response={}","ok了");
        return "ok";
    }
}
