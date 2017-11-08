package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dto.BondConvertionFactorDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPriceDto;
import com.sumscope.optimus.calculator.shared.model.dto.FuturePriceDto;

import java.util.List;

/**
 * 用于页面初始化时携带计算结果的Dto
 */
public class IniResponseDto extends AbstractTFCalculatorResponseDto {

    private FuturePriceDto defaultFuturePrice;

    private List<BondInfo> deliverableBonds;

    private List<BondConvertionFactorDto> bondConvertionFactor;

    private BondPriceDto defaultBondPrice;

    public FuturePriceDto getDefaultFuturePrice() {
        return defaultFuturePrice;
    }

    public void setDefaultFuturePrice(FuturePriceDto defaultFuturePrice) {
        this.defaultFuturePrice = defaultFuturePrice;
    }

    public List getDeliverableBonds() {
        return deliverableBonds;
    }

    public void setDeliverableBonds(List deliverableBonds) {
        this.deliverableBonds = deliverableBonds;
    }

    public List getBondConvertionFactor() {
        return bondConvertionFactor;
    }

    public void setBondConvertionFactor(List bondConvertionFactor) {
        this.bondConvertionFactor = bondConvertionFactor;
    }

    public BondPriceDto getDefaultBondPrice() {
        return defaultBondPrice;
    }

    public void setDefaultBondPrice(BondPriceDto defaultBondPrice) {
        this.defaultBondPrice = defaultBondPrice;
    }
}
