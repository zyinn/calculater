package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;
import com.sumscope.optimus.calculator.tfcalculator.model.dto.CalculationMainRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dto.GetFuturePriceRequestDto;
import com.sumscope.optimus.calculator.tfcalculator.utils.ValidateUtils;
import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 获取期货价格验证器。在父类基础上验证债卷收益率不得为0。且计算目标不得为期货理论价格及期货情景分析
 */
@Component
public class GetFuturePriceRequestValidator extends AbstractParameterValidatorImpl {

    @Autowired
    private RequestDtoConverter requestDtoConverter;

   void validateGetFuturePriceRequest(CalculationMainRequest mainRequest,String jsonString) {
        GetFuturePriceRequestDto getFuturePriceRequestDtos = requestDtoConverter.convertToGetFuturePriceRequestDto(jsonString);
        if(getFuturePriceRequestDtos==null){
            ValidateUtils.validatParams(GeneralValidationErrorType.DATA_MISSING,"GetFuturePriceRequestDto","期货价格刷新GetFuturePriceRequestDto不能为空");
            return;
        }
       validateRequest(getFuturePriceRequestDtos.getCalculationMainRequest(),jsonString);

       validateParameter(getFuturePriceRequestDtos);
        if(mainRequest.getCalculationType()== CalculationTarget.FUTURE_THEORETICAL_PRICE||mainRequest.getCalculationType()== CalculationTarget.FUTURE_ANALYSIS){
            ValidateUtils.validatParams(GeneralValidationErrorType.DATA_MISSING,"GetFuturePriceRequestDto","期货价格刷新计算目标不能为期货场景分析或期货理论价格");
            return;
        }
        if(mainRequest.getCalculationType()== CalculationTarget.IRR_BASE){
            if (validateBondInfoAndFutureContract(getFuturePriceRequestDtos)) return;
        }
        if(mainRequest.getCalculationType()== CalculationTarget.BOND_PRICE){
            if (validateBondInfoAndFutureContract(getFuturePriceRequestDtos)) return;
        }
    }

    private boolean validateBondInfoAndFutureContract(GetFuturePriceRequestDto getFuturePriceRequestDtos) {
        if(getFuturePriceRequestDtos.getBondInfo()==null){
            ValidateUtils.validatParams(GeneralValidationErrorType.DATA_MISSING,"GetFuturePriceRequestDto","期货价格刷新债劵不能为空");
            return true;
        }
        if(getFuturePriceRequestDtos.getFutureContract()==null){
            ValidateUtils.validatParams(GeneralValidationErrorType.DATA_MISSING,"GetFuturePriceRequestDto","期货价格刷新期货刷新不能为空");
            return true;
        }
        return false;
    }
}
