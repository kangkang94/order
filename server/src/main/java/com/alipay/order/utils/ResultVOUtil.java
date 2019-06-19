package com.alipay.order.utils;

import com.alipay.order.VO.ResultVO;

public class ResultVOUtil {

    public static ResultVO sucess(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(object);

        return resultVO;

    }
}
