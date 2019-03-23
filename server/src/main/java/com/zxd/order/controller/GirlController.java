package com.zxd.order.controller;

import com.zxd.order.config.GirlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GirlController {
    @Autowired
    private GirlConfig girlConfig;
    @RequestMapping("/girl/print")
    public String print(){
        return "name:"+girlConfig.getName()+"age:"+girlConfig.getAge();
    }
}
