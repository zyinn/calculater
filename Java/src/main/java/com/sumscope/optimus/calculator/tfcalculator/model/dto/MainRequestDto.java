package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;

/**
 * 实例类的抽象父类
 */

public class MainRequestDto implements CalculationMainRequest {
    /*
     * 当前请求的计算目标
     */
    private CalculationTarget calculationType;

    @Override
    public CalculationTarget getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationTarget calculationType) {
        this.calculationType = calculationType;
    }
}
