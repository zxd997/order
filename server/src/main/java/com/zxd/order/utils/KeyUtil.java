package com.zxd.order.utils;

import java.util.Random;

public class KeyUtil {
    /**
     * 随机生成订单id
     * 注意：此处是一个方法 ，但真的的开发中，不会这样。这样还是会有重复的
     */
    public static synchronized String getUniqueKey(){
        int i = new Random().nextInt(900000) + 100000;
        return System.currentTimeMillis()+String.valueOf(i);
    }
}
