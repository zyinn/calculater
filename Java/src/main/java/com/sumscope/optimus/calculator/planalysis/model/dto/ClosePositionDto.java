package com.sumscope.optimus.calculator.planalysis.model.dto;

import java.math.BigDecimal;

/**
 * Created by simon.mao on 2016/4/26.
 * 平仓日特定收益率条件下的盈亏分析结果
 */
public class ClosePositionDto {
    /**
     * 每手损益
     */
    private BigDecimal eachProfitLoss;
    /**
     * 总损益，决定盈亏曲线位置
     */
    private BigDecimal totalProfitLoss;
    /**
     * 年化收益率
     */
    private BigDecimal annualizedYield;

    /**
     * 收益率
     */
    private BigDecimal yield;
    /**
     * IRR
     */
    private BigDecimal irr;
    /**
     * 基差
     */
    private BigDecimal basis;
    /**
     * 净基差
     */
    private BigDecimal netBasis;
    /**
     * 现卷价格，决定现卷曲线位置
     */
    private BigDecimal bondPrice;
    /**
     * 期货价格，决定期货曲线位置
     */
    private BigDecimal futurePrice;

    /**
     * 冲击成本额
     */
    private BigDecimal impactAmount;

    public BigDecimal getImpactAmount() {
        return impactAmount;
    }

    public void setImpactAmount(BigDecimal impactAmount) {
        this.impactAmount = impactAmount;
    }

    public BigDecimal getEachProfitLoss() {
        return eachProfitLoss;
    }

    public void setEachProfitLoss(BigDecimal eachProfitLoss) {
        this.eachProfitLoss = eachProfitLoss;
    }

    public BigDecimal getTotalProfitLoss() {
        return totalProfitLoss;
    }

    public void setTotalProfitLoss(BigDecimal totalProfitLoss) {
        this.totalProfitLoss = totalProfitLoss;
    }

    public BigDecimal getAnnualizedYield() {
        return annualizedYield;
    }

    public void setAnnualizedYield(BigDecimal annualizedYield) {
        this.annualizedYield = annualizedYield;
    }

    public BigDecimal getYield() {
        return yield;
    }

    public void setYield(BigDecimal yield) {
        this.yield = yield;
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

    public BigDecimal getBondPrice() {
        return bondPrice;
    }

    public void setBondPrice(BigDecimal bondPrice) {
        this.bondPrice = bondPrice;
    }

    public BigDecimal getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(BigDecimal futurePrice) {
        this.futurePrice = futurePrice;
    }
}
