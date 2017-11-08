package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import java.math.BigDecimal;

/**
 * IRR计算请求类，传给后端期货价格用于计算
 */
public class IRRMainRequestDto extends MainRequestDto {

    private BigDecimal futurePrice;

    public BigDecimal getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(BigDecimal futurePrice) {
        this.futurePrice = futurePrice;
    }
}
