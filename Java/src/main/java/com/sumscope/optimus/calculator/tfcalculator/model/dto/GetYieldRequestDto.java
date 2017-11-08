package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;

import java.math.BigDecimal;

/**
 * 当用户企图获取当前债卷的最新收益率用于计算时使用该请求Dto
 */
public class GetYieldRequestDto extends AbstractTFCalculatorRequestDto {

    /**
     * 债卷收益率类型
     */
    private YieldTypeEnum yieldType;

    /**
     * 债卷key，与listedMarket一起作为债卷的业务主键
     */
    private String bondKey;

    /**
     * 债卷发行市场，与bondKey一起作为债卷的业务主键
     */
    private String bondListedMarket;

    private BigDecimal futurePrice;

    public BigDecimal getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(BigDecimal futurePrice) {
        this.futurePrice = futurePrice;
    }

    public YieldTypeEnum getYieldType() {
        return yieldType;
    }

    public void setYieldType(YieldTypeEnum yieldType) {
        this.yieldType = yieldType;
    }

    public String getBondKey() {
        return bondKey;
    }

    public void setBondKey(String bondKey) {
        this.bondKey = bondKey;
    }

    public String getBondListedMarket() {
        return bondListedMarket;
    }

    public void setBondListedMarket(String bondListedMarket) {
        this.bondListedMarket = bondListedMarket;
    }
}
