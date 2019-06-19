package com.alipay.order.message;

import com.alipay.order.utils.JsonUtil;
import com.alipay.product.common.ProductInfoOutPut;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductInfoReceiver {

    public static final String PRODUCT_STOCK_TEMPLATE = "product_stock_%s";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void getProductInfo(String message) {

        ProductInfoOutPut productInfoOutPut = (ProductInfoOutPut) JsonUtil.fromJson(message, ProductInfoOutPut.class);

        System.out.println(JsonUtil.toJson(productInfoOutPut));

        stringRedisTemplate.opsForValue().set(String.format(PRODUCT_STOCK_TEMPLATE, productInfoOutPut.getProductId()), String.valueOf(productInfoOutPut.getProductStock()));

    }

}
