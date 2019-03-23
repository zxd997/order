package com.zxd.order.exception;

import com.zxd.order.enums.ResultEnum;

public class OrderException extends RuntimeException {
    private int code;
    private String msg;

    public OrderException(int code,String msg) {
        super(msg);
        this.code = code;
    }
    public OrderException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code=resultEnum.getCode();
    }
}
