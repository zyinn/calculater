package com.sumscope.optimus.calculator.shared.gatewayinvoke;

import com.sumscope.optimus.calculator.planalysis.gateway.RedisGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/8/24.
 */
@Service
public class CDHFuturePriceGatewayImpl implements  CDHFuturePriceGateway{

    @Autowired
    private RedisGatewayService redisGatewayService;

    @Override
    public BigDecimal getLatestFuturePrice(String tfid) {
        return redisGatewayService.getFutureLastPrice(tfid);
    }

}
