package com.zxd.order.service.impl;

import com.zxd.order.dto.OrderDto;
import com.zxd.order.enums.OrderStatusEnum;
import com.zxd.order.enums.PayStatusEnum;
import com.zxd.order.enums.ResultEnum;
import com.zxd.order.exception.OrderException;
import com.zxd.order.model.OrderDetail;
import com.zxd.order.model.OrderMaster;
import com.zxd.order.repository.OrderDetailRepository;
import com.zxd.order.repository.OrderMasterRepository;
import com.zxd.order.service.OrderService;
import com.zxd.order.utils.KeyUtil;
import com.zxd.product.client.ProductClient;
import com.zxd.product.dto.ProductStock;
import com.zxd.product.model.ProductInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private ProductClient productClient;
    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        String orderId=KeyUtil.getUniqueKey();
        orderDto.setOrderId(orderId);
        //查询商品信息
        List<String> productIdList = orderDto.getOrderDetailList().stream().map(OrderDetail::getProductId).collect(Collectors.toList());
        List<ProductInfo> productInfoList = productClient.getListforOrder(productIdList);
        //List<com.zxd.product.model.ProductInfo> productInfoList = productClient.getListforOrder(productIdList);
        //如果有需要在此创建一个异步扣库存1. 判断redis中 库存充足2.redis减少 3.写入订单，并发送消息
        //计算总价
        BigDecimal orderAmout = new BigDecimal(BigInteger.ZERO);
        for (ProductInfo productInfo: productInfoList){
            for (OrderDetail orderDetail: orderDto.getOrderDetailList()){
                if (productInfo.getProductId().equals(orderDetail.getProductId())){
                    orderAmout= productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmout);
                    BeanUtils.copyProperties(productInfo,orderDetail);
                    orderDetail.setDetailId(KeyUtil.getUniqueKey()+10);
                    orderDetail.setOrderId(orderId);
                    //订单详情入库
                    orderDetailRepository.save(orderDetail);
                }
            }
        }
        //扣库存
        List<ProductStock> productStockList = orderDto.getOrderDetailList().stream().map(e -> new ProductStock(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());

        productClient.decreaseStock(productStockList);
        //订单入库
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDto,orderMaster);
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderAmount(orderAmout);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMasterRepository.save(orderMaster);
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto finish(String orderId) {
        //1.查询订单
        Optional<OrderMaster> orderMasterOpt = orderMasterRepository.findById(orderId);
        if (!orderMasterOpt.isPresent()){
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }
        //2.判断订单
        OrderMaster orderMaster = orderMasterOpt.get();
        if (OrderStatusEnum.NEW.getCode() != orderMaster.getOrderStatus()){
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //3.修改订单状态为完结
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMasterRepository.save(orderMaster);
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            new OrderException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster,orderDto);
        orderDto.setOrderDetailList(orderDetailList);
        return orderDto;
    }
}
