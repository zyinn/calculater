package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;

/**
 * 虚拟接口，描述当前的计算目标请求，根据不同的目标有不同的请求类。
 * 各Facade请求类都携带一个该接口实例用于后端计算，携带的具体实例则由计算目标决定。
 * 在转换器中必须手动转换。
 */
public interface CalculationMainRequest {

    /**
     * 获取计算目标
     */
    CalculationTarget getCalculationType();

}
