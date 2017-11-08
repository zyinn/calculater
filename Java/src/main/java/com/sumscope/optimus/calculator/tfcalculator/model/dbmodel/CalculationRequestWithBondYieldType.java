package com.sumscope.optimus.calculator.tfcalculator.model.dbmodel;

import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;
import com.sumscope.optimus.calculator.shared.model.dto.BondConvertionFactorDto;

/**
 * 在父类基差上包含了当前债卷价格的类型信息，即是哪种价格： ofr/bid/中债/成交
 */
public class CalculationRequestWithBondYieldType extends AbstractCalculationRequest {

    /**
     * 表明用于计算的债卷收益率是哪种收益率
     */
    private YieldTypeEnum bondYieldType;

    /**
     * 转换因子
     */
    private BondConvertionFactorDto convertionFactorDto;

    public BondConvertionFactorDto getConvertionFactorDto() {
        return convertionFactorDto;
    }

    public void setConvertionFactorDto(BondConvertionFactorDto convertionFactorDto) {
        this.convertionFactorDto = convertionFactorDto;
    }

    public YieldTypeEnum getBondYieldType() {
        return bondYieldType;
    }

    public void setBondYieldType(YieldTypeEnum bondYieldType) {
        this.bondYieldType = bondYieldType;
    }
}
