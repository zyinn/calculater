package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.tfcalculator.model.dto.CalculationMainRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dto.FutureInitRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 期货修改验证器。父类基础上再根据用户所处债卷窗口进行验证：
 * 若用户在自选卷或者虚拟卷窗口下并且没有传入完整的债卷信息和债卷价格信息则验证失败
 */
@Component
public class FutureInitResquestValidator extends AbstractParameterValidatorImpl {

    @Autowired
    private RequestDtoConverter requestDtoConverter;

    public void validateFutureInitRequest(CalculationMainRequest mainRequest, String jsonString) {
        FutureInitRequestDto futureInitRequestDto = requestDtoConverter.convertToFutureInitRequestDto(jsonString);
        validateParameter(futureInitRequestDto);
        validateRequest(mainRequest,jsonString);
    }
}
