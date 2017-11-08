package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;

/**
 * 抽象主要结果Dto，该类仅记录计算目标
 */
public class AbstractMainResultDto implements CalculationMainResult {

    private CalculationTarget calculationType;


    /**
     * @see com.sumscope.optimus.calculator.tfcalculator.model.dto.CalculationMainResult#getCalculationType()
     */
    public CalculationTarget getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationTarget calculationType) {
        this.calculationType = calculationType;
    }
}
