package com.alipay.order.controller;

import com.alipay.order.DTO.OrderDTO;
import com.alipay.order.VO.ResultVO;
import com.alipay.order.converter.OrderForm2OrderDTOConverter;
import com.alipay.order.enums.ResultEnum;
import com.alipay.order.exception.OrderException;
import com.alipay.order.form.OrderForm;
import com.alipay.order.service.OrderService;
import com.alipay.order.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 1.参数校验
     * 2.查询商品信息（调用商品服务）
     * 3.计算总价
     * 4.扣除库存（调用商品服务）
     * 5.订单入库
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    //post orderform表单
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new OrderException(bindingResult.getFieldError().getDefaultMessage(), ResultEnum.PARAM_ERROR.getCode());
        }

        //orderForm -->orderDTO
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            throw new OrderException(ResultEnum.CART_EMPTY.getMsg(), ResultEnum.CART_EMPTY.getCode());
        }

        //订单入库；orderMaster存入数据库
        OrderDTO orderDTO1 = orderService.create(orderDTO);

        //封装返回数据
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderDTO1.getOrderId());

        return ResultVOUtil.sucess(map);


    }
}
