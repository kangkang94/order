package com.alipay.order.utils;

import java.util.Random;

/**
 * 生成唯一主键
 * 时间+随机数
 */
public class KeyUtils {

    public static synchronized String getUniqueKey() {
        Random random = new Random();
        Integer i = random.nextInt(90000) + 100000;

        return System.currentTimeMillis() + String.valueOf(i);
    }
}
