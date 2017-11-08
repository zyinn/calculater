package com.sumscope.optimus.calculator.planalysis.gateway;

import java.math.BigDecimal;

/**
 * Created by simon.mao on 2016/5/6.
 */
public interface RedisGatewayService {
    BigDecimal getFutureLastPrice(String tfId);
}
