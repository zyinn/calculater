package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import java.math.BigDecimal;

/**
 * 期货场景分析请求，传给后端irr等参数用于计算
 */
public class FSAMainRequestDto extends MainRequestDto {

    private BigDecimal irr;

    private BigDecimal basis;

    private BigDecimal netBasis;

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
