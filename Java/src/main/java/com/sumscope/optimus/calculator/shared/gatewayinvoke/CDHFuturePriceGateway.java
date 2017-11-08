package com.sumscope.optimus.calculator.shared.gatewayinvoke;

import java.math.BigDecimal;

public interface CDHFuturePriceGateway {

    BigDecimal getLatestFuturePrice(String tfid);

}
