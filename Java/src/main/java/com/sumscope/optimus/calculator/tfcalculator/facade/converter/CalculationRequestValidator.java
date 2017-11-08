package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.tfcalculator.model.dto.BPMainRequestDto;
import com.sumscope.optimus.calculator.tfcalculator.model.dto.FSAMainRequestDto;
import com.sumscope.optimus.calculator.tfcalculator.model.dto.IRRMainRequestDto;
import org.springframework.stereotype.Component;
/**
 * 普通计算请求验证器。在父类基础再验证输入的债卷价格不得为0
 */
@Component
public class CalculationRequestValidator extends AbstractParameterValidatorImpl {

    public void validateIRRMainRequestDto(IRRMainRequestDto irrMainRequestDtos) {
        if(irrMainRequestDtos != null && irrMainRequestDtos.getFuturePrice() != null){
            validateRequestDto(irrMainRequestDtos.getFuturePrice(), "期货价格不合法，请输入正确的期货价格! 注：期货价格格式范围(0.000, 1000.000) ");
        }
    }

    public void validateBPMainRequestDto(BPMainRequestDto bpMainRequestDtos) {
        if(bpMainRequestDtos != null && bpMainRequestDtos.getFuturePrice() != null){
            validateRequestDto(bpMainRequestDtos.getFuturePrice(), "期货价格不合法，请输入正确的期货价格! 注：期货价格格式范围(0.000, 1000.000)");
        }
        if(bpMainRequestDtos.getIrr() != null){
            validate("(-?([1-9][0-9]{0,1}|0)(\\.)[0-9]{1,4})|-?([1-9][0-9]{0,1}|0)|-?100", bpMainRequestDtos.getIrr(), "IRR不合法，请输入正确的IRR! 注：IRR格式范围(-100.0000%, 100.0000%)");
        }
        if(bpMainRequestDtos.getBasis()!=null){
            validate("(-?([1-9][0-9]{0,1}|0)(\\.)[0-9]{1,4})|-?([1-9][0-9]{0,1}|0)|-?100", bpMainRequestDtos.getBasis(), "基差不合法，请输入正确的基差! 注：基差格式范围(-100.0000, 100.0000) ");
        }
        if(bpMainRequestDtos.getNetBasis()!=null){
            validate("(-?([1-9][0-9]{0,1}|0)(\\.)[0-9]{1,4})|-?([1-9][0-9]{0,1}|0)|-?100",bpMainRequestDtos.getNetBasis(), "净基差不合法，请输入正确的净基差! 注：净基差格式范围(-100.0000, 100.0000) ");
        }
    }
    public void validateFSAMainRequestDto(FSAMainRequestDto fsaResult) {
        if(fsaResult.getIrr()!=null){
            validate("(-?([1-9][0-9]{0,1}|0)(\\.)[0-9]{1,4})|-?([1-9][0-9]{0,1}|0)|-?100", fsaResult.getIrr(), "IRR不合法，请输入正确的IRR! 注：IRR格式范围(-100.0000%, 100.0000%)");
        }else{
            if(fsaResult.getBasis()!=null){
                validate("(-?([1-9][0-9]{0,1}|0)(\\.)[0-9]{1,4})|-?([1-9][0-9]{0,1}|0)|-?100", fsaResult.getBasis(), "基差不合法，请输入正确的基差! 注：基差格式范围(-100.0000, 100.0000) ");
            }
            if(fsaResult.getNetBasis()!=null){
                validate("(-?([1-9][0-9]{0,1}|0)(\\.)[0-9]{1,4})|-?([1-9][0-9]{0,1}|0)|-?100",fsaResult.getNetBasis(), "净基差不合法，请输入正确的净基差! 注：净基差格式范围(-100.0000, 100.0000) ");
            }
        }
    }

}
