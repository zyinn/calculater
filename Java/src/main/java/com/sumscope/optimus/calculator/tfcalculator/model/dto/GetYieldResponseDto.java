package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.shared.model.dto.BondPriceDto;

/**
 * 当用户企图获取当前债卷的最新收益率用于计算时使用该响应Dto
 */
public class GetYieldResponseDto extends AbstractTFCalculatorResponseDto {

    private BondPriceDto bondPrice;

    public BondPriceDto getBondPrice() {
        return bondPrice;
    }

    public void setBondPrice(BondPriceDto bondPrice) {
        this.bondPrice = bondPrice;
    }
}
