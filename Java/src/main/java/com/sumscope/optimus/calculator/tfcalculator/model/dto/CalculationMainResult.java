package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;

/**
 * 主要计算结果的虚拟接口。根据计算目标不同需要展示不同的主要结果，则每个计算目标都有对应的实例类用于主要结果的携带。
 */
public interface CalculationMainResult {

    CalculationTarget getCalculationType();

}
