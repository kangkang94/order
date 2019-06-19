package com.alipay.order.repository;

import com.alipay.order.dataobject.OrderMaster;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    public void save() {

        OrderMaster orderMaster = new OrderMaster();
        orderMasterRepository.save(orderMaster);
    }
}