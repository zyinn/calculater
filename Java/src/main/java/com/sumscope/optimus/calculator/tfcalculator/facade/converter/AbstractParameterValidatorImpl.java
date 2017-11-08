package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.shared.enums.BondPriceType;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestWithBondYieldType;
import com.sumscope.optimus.calculator.tfcalculator.model.dto.*;
import com.sumscope.optimus.calculator.tfcalculator.utils.JsonNodeUtils;
import com.sumscope.optimus.calculator.tfcalculator.utils.ValidateUtils;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import com.sumscope.optimus.commons.util.JsonUtil;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * 所有验证器的抽象父类。该类对AbstractTFCalculatorRequestDto对象进行参数验证。主要验证如下信息：
 * 必须有完整期货信息
 * 必须有完整债卷信息（期货改变场景除外）
 * 根据计算目标分别验证MainRequest信息是否完整
 */
public abstract class AbstractParameterValidatorImpl implements ParameterValidator {

    protected void validateMainRequest(CalculationMainRequest mainRequest) {
        if(mainRequest.getCalculationType()==null){
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, " 当前请求的计算目标不能为空");
        }
    }

    protected void validateRequest(CalculationMainRequest mainRequest, String jsonString) {
        CalculationTarget calculationType = mainRequest.getCalculationType();
        //计算目标 -- 债卷价格 验证期货价格，Irr/基差/净基差是否为空
        if (calculationType.BOND_PRICE.equals(calculationType)){
            String bpMainRequestDtos = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonString).get("BPMainRequestDto"));
            BPMainRequestDto bpMainRequestDto = JsonUtil.readValue(bpMainRequestDtos, BPMainRequestDto.class);
            if(bpMainRequestDto==null){
                throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "债券类型的BPMainRequestDto不能为空");
//                ValidateUtils.validatParams(GeneralValidationErrorType.DATA_MISSING,"BPMainRequestDto","债券类型的BPMainRequestDto不能为空");
//                return;
            }else{
                if(("".equals(bpMainRequestDto.getIrr())||bpMainRequestDto.getIrr()==null)&&("".equals(bpMainRequestDto.getBasis())||bpMainRequestDto.getBasis()==null)
                        &&("".equals(bpMainRequestDto.getNetBasis())||bpMainRequestDto.getNetBasis()==null)){
                    throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "债卷价格Irr/基差/净基差不能同时为空");
//                    ValidateUtils.validatParams(GeneralValidationErrorType.DATA_MISSING,bpMainRequestDto,"债卷价格Irr/基差/净基差不能同时为空" );
//                    return;
                }
            }
        }
        //计算目标 -- 期货场景分析 验证Irr/基差/净基差是否为空
        if(calculationType.FUTURE_ANALYSIS.equals(calculationType)){
            String fsaMainRequestDtos = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonString).get("FSAMainRequestDto"));
            FSAMainRequestDto fsaMainRequestDto = JsonUtil.readValue(fsaMainRequestDtos, FSAMainRequestDto.class);
            if(fsaMainRequestDto==null){
                throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "期货场景分析FSAMainRequestDto为空");
//                ValidateUtils.validatParams(GeneralValidationErrorType.DATA_MISSING,"FSAMainRequestDto","期货场景分析FSAMainRequestDto为空" );
//                return;
            }else{
                if(("".equals(fsaMainRequestDto.getIrr())||fsaMainRequestDto.getIrr()==null)&&("".equals(fsaMainRequestDto.getBasis())||fsaMainRequestDto.getBasis()==null)
                        &&("".equals(fsaMainRequestDto.getNetBasis())||fsaMainRequestDto.getNetBasis()==null)){
                    throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, " 期货场景分析Irr/基差/净基差不能同时为空");
//                    ValidateUtils.validatParams(GeneralValidationErrorType.DATA_MISSING,"FSAMainRequestDto","期货场景分析Irr/基差/净基差不能同时为空" );
//                    return;
                }
            }
        }
    }

    public void validateRequestDto(BigDecimal param, String message) {
        validate("(([1-9][0-9]{0,2}|0)(\\.)[0-9]{1,4})|([1-9][0-9]{0,2}|0)", param, message);
    }
    public void validateCapitalCostDto(BigDecimal param, String message) {
        validate("(([1-9][0-9]{0,1}|0)(\\.)[0-9]{1,4})|([1-9][0-9]{0,1}|0)", param, message);
    }

    public void validate(String validate, BigDecimal param, String message) {
        boolean validateParam=param!=null?(param.toString()).matches(validate):false;
        if (!validateParam) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, message);
        }
    }
    public void validateBondPriceDto(CalculationRequestDto calculationRequestDto) {
        if (calculationRequestDto.getBondPriceType() == BondPriceType.YIELD) {
            validate("-?(([1-9][0-9]{0,1}|0)(\\.)[0-9]{1,4})|-?([1-9][0-9]{0,1}|0)",calculationRequestDto.getBondPrice().getYield(), "收益率不合法，请输入正确的收益率! 注：收益率格式范围(-100.0000%, 100.0000%) ");
        }
        if (calculationRequestDto.getBondPriceType() == BondPriceType.NET_PRICE) {
            validate("(([1-9][0-9]{0,2}|0)(\\.)[0-9]{1,4})|([1-9][0-9]{0,2}|0)",calculationRequestDto.getBondPrice().getNetPrice(), "债劵净价不合法，请输入正确的债劵净价! 注：债劵净价格式范围(0.0000, 1000.0000) ");
        }
        if (calculationRequestDto.getBondPriceType() == BondPriceType.FULL_PRICE) {
            validate("(([1-9][0-9]{0,2}|0)(\\.)[0-9]{1,4})|([1-9][0-9]{0,2}|0)",calculationRequestDto.getBondPrice().getFullPrice(), "债劵全价不合法，请输入正确的债劵全价! 注：债劵全价格式范围(0.0000, 1000.0000) ");
        }
    }
    public void validateCapitalCostDto(BigDecimal capitalCost) {
        if(capitalCost!=null){
            validateCapitalCostDto(capitalCost, "资金成本不合法，请输入正确的资金成本! 注：资金成本格式范围(0.00%, 100.00%) ");
        }
    }

    @Override
    public void validateParameter(AbstractTFCalculatorRequestDto parameterDto) {
        validateMainRequest(parameterDto.getCalculationMainRequest());
    }

    public void validateMaturityDate(CalculationRequestWithBondYieldType calculationRequest) {
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        if(calculationRequest.getBond().getMaturityDate()!=null){
            try {
                long maturityDate = sf.parse(calculationRequest.getBond().getMaturityDate()).getTime();
                long deliveryDate=calculationRequest.getFutureContract().getDeliveryDate().getTime();
                long interestStartDate=sf.parse(calculationRequest.getBond().getInterestStartDate()).getTime();
                if(interestStartDate>maturityDate){
                    throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"债劵的到期日需大于起息日！");
                }
                if(deliveryDate>maturityDate){
                    throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"债劵的到期日需大于期货的交割日！");
                }
            } catch (ParseException e) {
                throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"期日格式不正确，请修改后再次计算！");
            }
        }
    }
}
