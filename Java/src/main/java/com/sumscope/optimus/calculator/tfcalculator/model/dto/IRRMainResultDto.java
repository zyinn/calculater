package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import java.math.BigDecimal;

/**
 * IRR计算结果实例
 */
public class IRRMainResultDto extends AbstractMainResultDto {

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
