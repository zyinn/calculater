package com.sumscope.optimus.calculator.tfcalculator.model.dto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPriceDto;
import com.sumscope.optimus.calculator.shared.model.dto.FuturePriceDto;

import java.math.BigDecimal;

/**
 * 除特定操作之外引起的其他所有计算都使用该响应Dto
 */
public class CalculationResponseDto extends AbstractTFCalculatorResponseDto {

    private BondPriceDto bondPrice;

    private BigDecimal bondConvertionFactor;

    private FuturePriceDto futruePrice;

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

    public FuturePriceDto getFutruePrice() {
        return futruePrice;
    }

    public void setFutruePrice(FuturePriceDto futruePrice) {
        this.futruePrice = futruePrice;
    }
}
