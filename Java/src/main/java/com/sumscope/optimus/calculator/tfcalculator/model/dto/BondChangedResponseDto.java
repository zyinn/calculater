package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;
import com.sumscope.optimus.calculator.shared.model.dto.BondPriceDto;

import java.math.BigDecimal;

/**
 * 当用户改变了当前债卷时使用该响应Dto
 */
public class BondChangedResponseDto extends AbstractTFCalculatorResponseDto {

    private BondPriceDto bondPrice;

    private BigDecimal bondConvertionFactor;

    public BondPriceDto getBondPrice() {
        return bondPrice;
    }

    public void setBondPrice(BondPriceDto bondPrice) {
        this.bondPrice = bondPrice;
    }

    public BigDecimal getBondConvertionFactor() {
        return bondConvertionFactor;
    }

    public void setBondConvertionFactor(BigDecimal bondConvertionFactor) {
        this.bondConvertionFactor = bondConvertionFactor;
    }
}
