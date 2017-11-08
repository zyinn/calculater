package com.sumscope.optimus.calculator.tfcalculator.model.dbmodel;

import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;

/**
 * 用于一般计算的请求，没有附加信息
 */
public class CalculationRequest extends AbstractCalculationRequest {

    private YieldTypeEnum yieldTypeEnum;

    public YieldTypeEnum getYieldTypeEnum() {
        return yieldTypeEnum;
    }

    public void setYieldTypeEnum(YieldTypeEnum yieldTypeEnum) {
        this.yieldTypeEnum = yieldTypeEnum;
    }
}
