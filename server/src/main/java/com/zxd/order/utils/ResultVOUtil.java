package com.zxd.order.utils;

import com.zxd.order.vo.ResultVO;

public class ResultVOUtil {
    public static ResultVO success(Object o){
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        resultVO.setData(o);
        return resultVO;
    }
}
