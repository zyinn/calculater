package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import java.math.BigDecimal;

/**
 * 计算债卷价格请求，传给后端期货价格及IRR等参数
 */
public class BPMainRequestDto extends MainRequestDto {

    private BigDecimal futurePrice;

    private BigDecimal irr;

    private BigDecimal basis;

    private BigDecimal netBasis;

    public BigDecimal getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(BigDecimal futurePrice) {
        this.futurePrice = futurePrice;
    }

    public BigDecimal getIrr() {
        return irr;
    }

    public void setIrr(BigDecimal irr) {
        this.irr = irr;
    }

    public BigDecimal getBasis() {
        return basis;
    }

    public void setBasis(BigDecimal basis) {
        this.basis = basis;
    }

    public BigDecimal getNetBasis() {
        return netBasis;
    }

    public void setNetBasis(BigDecimal netBasis) {
        this.netBasis = netBasis;
    }
}
