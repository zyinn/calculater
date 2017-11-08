package com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke;

import com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.dto.TFCalculatorQDPResponse;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.AbstractCalculationRequest;

/**
 * 调用外部QDP计算的Gateway接口。
 * 该接口内对请求进行转换并调用对应的外部计算接口（Rest）
 */

public interface TFCalculatorQDPRestGateway {

    /**
     * 接受本地请求，转换为QDP计算要求的请求类型，调用QDP计算获取计算结果
     */
    public abstract TFCalculatorQDPResponse doCalculation(AbstractCalculationRequest calculationRequest);

}
