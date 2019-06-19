package com.alipay.order.service.iml;

import com.alipay.order.DTO.OrderDTO;
import com.alipay.order.dataobject.OrderDetail;
import com.alipay.order.dataobject.OrderMaster;
import com.alipay.order.enums.OrderStatusEnum;
import com.alipay.order.enums.PayStatusEnum;
import com.alipay.order.repository.OrderDetailRepository;
import com.alipay.order.repository.OrderMasterRepository;
import com.alipay.order.service.OrderService;
import com.alipay.order.utils.KeyUtils;
import com.alipay.product.client.ProductClient;
import com.alipay.product.common.DescreaseStockInput;
import com.alipay.product.common.ProductInfoOutPut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceiml implements OrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtils.getUniqueKey();

        //2.查询商品信息（调用商品服务）
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();
        List<String> productIdList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {

            productIdList.add(orderDetail.getProductId());
        }
        List<ProductInfoOutPut> productInfoList = productClient.listForOrder(productIdList);

        //TODO 3.计算总价
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        for (OrderDetail orderDetail : orderDetailList) {
            for (ProductInfoOutPut productInfoOutPut : productInfoList) {
                if (orderDetail.getProductId().equals(productInfoOutPut.getProductId())) {
                    orderAmount = productInfoOutPut.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);

                    //orderDetail 订单详情入库
                    BeanUtils.copyProperties(productInfoOutPut, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtils.getUniqueKey());
                    orderDetailRepository.save(orderDetail);
                }
            }
        }


        // 4.扣除库存（调用商品服务）
        List<DescreaseStockInput> descreaseStockInputList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            descreaseStockInputList.add(new DescreaseStockInput(orderDetail.getProductId(), orderDetail.getProductQuantity()));
        }

        productClient.descreaseStock(descreaseStockInputList);

        //5.订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(KeyUtils.getUniqueKey());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderId(KeyUtils.getUniqueKey());
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.SUCCESS.getCode());

        orderMasterRepository.save(orderMaster);


        return orderDTO;
    }
}
