package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import java.math.BigDecimal;

/**
 * 期货价格主要计算结果实例
 */
public class BPMainResultDto extends AbstractMainResultDto {

    private BigDecimal yield;

    private BigDecimal bondNetPrice;

    private BigDecimal bondFullPrice;
    /**
     * T+1收益率
     */
    private BigDecimal yieldByDay;

    /**
     * 在债劵价格目标下 供web端展示使用
     */
    private BigDecimal irr;

    private BigDecimal netBasis;

    private BigDecimal basis;

    public BigDecimal getYield() {
        return yield;
    }

    public void setYield(BigDecimal yield) {
        this.yield = yield;
    }

    public BigDecimal getBondNetPrice() {
        return bondNetPrice;
    }

    public void setBondNetPrice(BigDecimal bondNetPrice) {
        this.bondNetPrice = bondNetPrice;
    }

    public BigDecimal getBondFullPrice() {
        return bondFullPrice;
    }

    public void setBondFullPrice(BigDecimal bondFullPrice) {
        this.bondFullPrice = bondFullPrice;
    }

    public BigDecimal getYieldByDay() {
        return yieldByDay;
    }

    public void setYieldByDay(BigDecimal yieldByDay) {
        this.yieldByDay = yieldByDay;
    }

    public BigDecimal getIrr() {
        return irr;
    }

    public void setIrr(BigDecimal irr) {
        this.irr = irr;
    }

    public BigDecimal getNetBasis() {
        return netBasis;
    }

    public void setNetBasis(BigDecimal netBasis) {
        this.netBasis = netBasis;
    }

    public BigDecimal getBasis() {
        return basis;
    }

    public void setBasis(BigDecimal basis) {
        this.basis = basis;
    }
}
