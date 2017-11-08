package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.planalysis.commons.util.NumberUtil;
import com.sumscope.optimus.calculator.planalysis.model.qdp.KeyValuePair;
import com.sumscope.optimus.calculator.shared.model.dto.BondConvertionFactorDto;
import com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.dto.TFCalculatorQDPResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */
public final class QdpConverterToResult {

    private QdpConverterToResult() {
    }

    public static Result converterQdpToResult(TFCalculatorQDPResponse tfCalculatorQDPResponse, String bondCode){
        Result result = new Result();
        if(tfCalculatorQDPResponse!=null){
        for(KeyValuePair irr:tfCalculatorQDPResponse.getIrr()){
            if(irr.getKey().equals(bondCode)){
                result.setIrr(NumberUtil.roundHalfUp(irr.getValue(), 8));
                break;
            }
        }
        for (KeyValuePair basis : tfCalculatorQDPResponse.getBasis()){
            if(basis.getKey().equals(bondCode)){
                result.setBasis(NumberUtil.roundHalfUp(basis.getValue(),4));
                break;
            }
        }
        for (KeyValuePair netBasis : tfCalculatorQDPResponse.getNetBasis()){
            if(netBasis.getKey().equals(bondCode)){
                result.setNetBasis(NumberUtil.roundHalfUp(netBasis.getValue(),4));
                break;
            }
        }
        for (KeyValuePair bondCleanPrice : tfCalculatorQDPResponse.getBondCleanPrice()){
            if(bondCleanPrice.getKey().equals(bondCode)){
                result.setBondCleanPrice(NumberUtil.roundHalfUp(bondCleanPrice.getValue(),8));
                break;
            }
        }
        for (KeyValuePair bondDirtyPrice : tfCalculatorQDPResponse.getBondDirtyPrice()){
            if(bondDirtyPrice.getKey().equals(bondCode)){
                result.setBondDirtyPrice(NumberUtil.roundHalfUp(bondDirtyPrice.getValue(),8));
                break;
            }
        }
        if(tfCalculatorQDPResponse.getConversionFactors()!=null){
            List<BondConvertionFactorDto> list=new ArrayList<>();
            for (KeyValuePair conversionFactors : tfCalculatorQDPResponse.getConversionFactors()){
                    BondConvertionFactorDto bondConvertionFactorDto=new BondConvertionFactorDto();
                    bondConvertionFactorDto.setBondKey(conversionFactors.getKey());
                    bondConvertionFactorDto.setConvertionFactor(NumberUtil.roundHalfUp(conversionFactors.getValue(),4));
                    list.add(bondConvertionFactorDto);
            }
            result.setConversionFactors(list);
        }
        for (KeyValuePair timeWeightedCoupon : tfCalculatorQDPResponse.getTimeWeightedCoupon()){
            if(timeWeightedCoupon.getKey().equals(bondCode)){
                result.setTimeWeightedCoupon(NumberUtil.roundHalfUp(timeWeightedCoupon.getValue(),4));
                break;
            }
        }

        for (KeyValuePair invoicePrice : tfCalculatorQDPResponse.getInvoicePrice()){
            if(invoicePrice.getKey().equals(bondCode)){
                result.setInvoicePrice(NumberUtil.roundHalfUp(invoicePrice.getValue(),4));
                break;
            }
        }
        for (KeyValuePair aiEnd : tfCalculatorQDPResponse.getAiEnd()){
            if(aiEnd.getKey().equals(bondCode)){
                result.setAiEnd(NumberUtil.roundHalfUp(aiEnd.getValue(),4));
                break;
            }
        }
        for (KeyValuePair spread : tfCalculatorQDPResponse.getSpread()){
            if(spread.getKey().equals(bondCode)){
                result.setSpread(NumberUtil.roundHalfUp(spread.getValue(),4)!=null ? NumberUtil.multiply(NumberUtil.roundHalfUp(spread.getValue(),4),10000).setScale(2,BigDecimal.ROUND_HALF_UP) : null);
                break;
            }
        }
        for (KeyValuePair pnL : tfCalculatorQDPResponse.getPnL()){
            if(pnL.getKey().equals(bondCode)){
                result.setPnL(NumberUtil.roundHalfUp(pnL.getValue(),4));
                break;
            }
        }
        for (KeyValuePair coupon : tfCalculatorQDPResponse.getCoupon()){
            if(coupon.getKey().equals(bondCode)){
                result.setCoupon(NumberUtil.roundHalfUp(coupon.getValue(),4));
                break;
            }
        }
        for (KeyValuePair margin : tfCalculatorQDPResponse.getMargin()){
            if(margin.getKey().equals(bondCode)){
                result.setMargin(NumberUtil.roundHalfUp(margin.getValue(),4));
                break;
            }
        }
        for (KeyValuePair aiStart : tfCalculatorQDPResponse.getAiStart()){
            if(aiStart.getKey().equals(bondCode)){
                result.setAiStart(NumberUtil.roundHalfUp(aiStart.getValue(),4));
                break;
            }
        }
        for (KeyValuePair bondYtm : tfCalculatorQDPResponse.getBondYtm()){
            if(bondYtm.getKey().equals(bondCode)){
                result.setBondYtm(bondYtm.getValue().setScale(8,BigDecimal.ROUND_HALF_UP));
                break;
            }
        }
        for (KeyValuePair futurePrice : tfCalculatorQDPResponse.getFuturesPrice()){
            if(futurePrice.getKey().equals(bondCode)){
                result.setFuturePrice(NumberUtil.roundHalfUp(futurePrice.getValue(),4));
                break;
            }
        }
        if(tfCalculatorQDPResponse.getMaturityMoveDayYtm()!=null){
            for (KeyValuePair maturityMoveDayYtm : tfCalculatorQDPResponse.getMaturityMoveDayYtm()){
                if(maturityMoveDayYtm.getKey().equals(bondCode)){
                    result.setMaturityMoveDayYtm(NumberUtil.roundHalfUp(maturityMoveDayYtm.getValue(),4));
                    break;
                }
            }
        }
        }
        return result;
    }


    /**
     * 将 BigDecimal 保留四位
     * @param bdi
     * @return
     */
    public static BigDecimal getRoundHalfUp(BigDecimal bdi){
        return NumberUtil.roundHalfUp(bdi,4);
    }

}
