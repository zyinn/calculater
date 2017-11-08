package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.shared.enums.BondPriceType;
import com.sumscope.optimus.calculator.shared.model.dto.BondInfoDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPriceDto;

import java.math.BigDecimal;

/**
 * 除特定操作之外引起的其他所有计算都使用该请求Dto
 */
public class CalculationRequestDto extends AbstractTFCalculatorRequestDto {

    /**
     * 债卷价格,若用户修改净价或全价这设置对应的数值，取消其他两个数值的数据。其他情况仅取当前页面的债卷收益率
     */
    private BondPriceDto bondPrice;

    private BondPriceType bondPriceType;

    /**
     * 债卷信息Dto
     * 当前债卷信息仅在虚拟卷时设置全量，其他窗口仅设置bondKey和listedMarket
     */
    private BondInfoDto bondInfo;

    private BigDecimal futurePrice;//期货价格

    public BondPriceDto getBondPrice() {
        return bondPrice;
    }

    public void setBondPrice(BondPriceDto bondPrice) {
        this.bondPrice = bondPrice;
    }

    public BondInfoDto getBondInfo() {
        return bondInfo;
    }

    public void setBondInfo(BondInfoDto bondInfo) {
        this.bondInfo = bondInfo;
    }

    public BigDecimal getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(BigDecimal futurePrice) {
        this.futurePrice = futurePrice;
    }

    public BondPriceType getBondPriceType() {
        return bondPriceType;
    }

    public void setBondPriceType(BondPriceType bondPriceType) {
        this.bondPriceType = bondPriceType;
    }
}
