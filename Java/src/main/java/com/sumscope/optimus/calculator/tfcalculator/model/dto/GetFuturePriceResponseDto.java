package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 在计算目标为IRR或者债卷价格时，当用户企图获取期货最新价格进行计算时使用该响应Dto
 */
public class GetFuturePriceResponseDto extends AbstractTFCalculatorResponseDto {

    private BigDecimal futurePrice;

    private Date lastFutureUpdateDate;

    public BigDecimal getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(BigDecimal futurePrice) {
        this.futurePrice = futurePrice;
    }

    public Date getLastFutureUpdateDate() {
        return lastFutureUpdateDate;
    }

    public void setLastFutureUpdateDate(Date lastFutureUpdateDate) {
        this.lastFutureUpdateDate = lastFutureUpdateDate;
    }
}
