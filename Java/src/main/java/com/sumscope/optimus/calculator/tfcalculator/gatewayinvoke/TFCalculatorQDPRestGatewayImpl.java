package com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke;

import com.sumscope.optimus.calculator.planalysis.commons.CalculatorExceptionType;
import com.sumscope.optimus.calculator.planalysis.gateway.RestInvocationUtil;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.FutureContract;
import com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.dto.TFCalculatorQDPRequest;
import com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.dto.TFCalculatorQDPResponse;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.AbstractCalculationRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestForInit;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestWithBondYieldType;
import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/8/1.
 */
@Component
public class TFCalculatorQDPRestGatewayImpl implements  TFCalculatorQDPRestGateway{
    @Autowired
    private TFCalculationQDPRequestConverter tFCalculationQDPRequestConverter;
    @Value("${TfCalculator.tfCalculator_url}")
    public String tfCalculator_url;

    @Override
//    @CacheMe(timeout = 3600)
    public TFCalculatorQDPResponse doCalculation(AbstractCalculationRequest calculationRequest) {
        TFCalculatorQDPRequest tFCalculatorQDPRequest = AbstractCalculationRequest(calculationRequest);
        String response = RestInvocationUtil.post(tfCalculator_url, JsonUtil.writeValueAsString(tFCalculatorQDPRequest));
        if (response == null) {
            throw new BusinessRuntimeException(CalculatorExceptionType.INPUT_PARAM_INVALID, "输入的计算参数不合法，造成计算结果异常，请重新输入。");
        } else {
            TFCalculatorQDPResponse tFCalculatorQDPResponse = JsonUtil.readValue(response, TFCalculatorQDPResponse.class);
            if (tFCalculatorQDPResponse==null || tFCalculatorQDPResponse.isSucceeded() == false) {
                throw new BusinessRuntimeException(CalculatorExceptionType.INPUT_PARAM_INVALID, "QDP计算失败,如有疑问,请联系管理员!");
            } else {
                tFCalculatorQDPResponse.setCalculationTarget(calculationRequest.getCalculationTarget());
                return tFCalculatorQDPResponse;
            }
        }
    }

    /**
     * calculationRequest 转 tFCalculatorQDPRequest
     * @param calculationRequest
     * @return
     */
    private TFCalculatorQDPRequest AbstractCalculationRequest(AbstractCalculationRequest calculationRequest){
        FutureContract futureContract = calculationRequest.getFutureContract();
        BondInfo bond = calculationRequest.getBond();
        TFCalculatorQDPRequest tFCalculatorQDPRequest=new TFCalculatorQDPRequest();
        if("CalculationRequestForInit".equals(calculationRequest.getDistinguish())){
            CalculationRequestForInit calculationRequestForInit=new CalculationRequestForInit();
            BeanUtils.copyProperties(calculationRequest,calculationRequestForInit);
            tFCalculatorQDPRequest=tFCalculationQDPRequestConverter.convertCalculationRequest(futureContract,bond,calculationRequestForInit);
        }else if("CalculationRequestWithBondYieldType".equals(calculationRequest.getDistinguish())){
            CalculationRequestWithBondYieldType calculationRequestWithBondYieldType=new CalculationRequestWithBondYieldType();
            BeanUtils.copyProperties(calculationRequest,calculationRequestWithBondYieldType);
            tFCalculatorQDPRequest=tFCalculationQDPRequestConverter.convertCalculationRequest(futureContract,bond,calculationRequestWithBondYieldType);
        }else{
            CalculationRequest CalculationRequest=new CalculationRequest();
            BeanUtils.copyProperties(calculationRequest,CalculationRequest);
            tFCalculatorQDPRequest=tFCalculationQDPRequestConverter.convertCalculationRequest(futureContract,bond,CalculationRequest);
        }
        return tFCalculatorQDPRequest;
    }

}
