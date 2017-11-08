package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.shared.model.dbmodel.FutureContract;

import java.math.BigDecimal;

/**
 * 抽象TFCalculator请求Dto，该抽象请求携带了所有请求都必要的数据
 */
public class AbstractTFCalculatorRequestDto {

    /**
     * 根据计算目标决定携带的主要请求参数实例
     */

    private MainRequestDto calculationMainRequest;

    /**
     * 当前期货信息，必须传递
     */
    private FutureContract futureContract;

    /**
     * 资金成本，必须传递，可为0
     */
    private BigDecimal capitalCost;


    public MainRequestDto getCalculationMainRequest() {
        return calculationMainRequest;
    }

    public void setCalculationMainRequest(MainRequestDto calculationMainRequest) {
        this.calculationMainRequest = calculationMainRequest;
    }

    public FutureContract getFutureContract() {
        return futureContract;
    }

    public void setFutureContract(FutureContract futureContract) {
        this.futureContract = futureContract;
    }

    public BigDecimal getCapitalCost() {
        return capitalCost;
    }

    public void setCapitalCost(BigDecimal capitalCost) {
        this.capitalCost = capitalCost;
    }
}
