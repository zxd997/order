package com.zxd.order.converter;

import com.alibaba.fastjson.JSON;
import com.zxd.order.dto.OrderDto;
import com.zxd.order.enums.ResultEnum;
import com.zxd.order.exception.OrderException;
import com.zxd.order.form.OrderForm;
import com.zxd.order.model.OrderDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderForm2OrderDto {
    public static OrderDto toOrderDto(OrderForm orderForm){
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = JSON.parseArray(orderForm.getItems(), OrderDetail.class);
        }catch (Exception e){
            log.error("【json转换】错误,string={}",orderForm.getItems());
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }
        orderDto.setOrderDetailList(orderDetailList);
        return orderDto;
    }
}
