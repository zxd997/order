package com.zxd.order.message;

import com.alibaba.fastjson.JSON;
import com.zxd.product.model.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductInfoReceiver {
    private static final String PRODUCT_STOCK_TEMPLATE = "product_stock_%s";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void process(String msg){
        List<ProductInfo> productInfoList = JSON.parseArray(msg, ProductInfo.class);
        log.info("从队列【{}】接受到消息：{}","productInfo",productInfoList);
        //存储到redis中
        for (ProductInfo productInfo : productInfoList) {
            String key = String.format(PRODUCT_STOCK_TEMPLATE, productInfo.getProductId());
            stringRedisTemplate.opsForValue().set(key,String.valueOf(productInfo.getProductStock()));

            String v = stringRedisTemplate.opsForValue().get(key);
            log.info("key为【{}】的值为：{}", key, v);
        }
    }
}
