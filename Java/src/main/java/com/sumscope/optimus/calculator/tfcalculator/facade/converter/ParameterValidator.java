package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.tfcalculator.model.dto.AbstractTFCalculatorRequestDto;

/**
 * 参数验证器接口。该接口实例为Facade的各方法对应的验证实例。各方法调用对应的验证实例进行参数验证。参数验证根据需求文档中对输入的上下借等说明进行。
 */
public interface ParameterValidator {

    /**
     * 验证参数是否合法。当参数不合法时抛出验证错误。该验证错误可能包含多个参数的数据异常信息。前端收到该验证错误后将验证异常的数据高亮显示，同时将所有结果显示为“--”
     */
    void validateParameter(AbstractTFCalculatorRequestDto parameterDto);

}
