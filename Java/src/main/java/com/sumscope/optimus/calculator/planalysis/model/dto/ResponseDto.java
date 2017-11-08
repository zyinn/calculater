package com.sumscope.optimus.calculator.planalysis.model.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by simon.mao on 2016/4/26.
 * 盈亏分析计算结果Dto
 */
public class ResponseDto extends CommonDto {
    /**
     * 手数
     */
    private int futureNumber;
    /**
     * 现卷期货久期
     */
    private BigDecimal cdcDuration;
    /**
     * CTD 期货久期
     */
    private BigDecimal ctdDuration;
    /**
     * IRR计算结果
     */
    private BigDecimal irr;
    /**
     * 基差计算结果
     */
    private BigDecimal basis;

    /**
     * 净基差计算结果
     */
    private BigDecimal netBasis;

    /**
     * 现卷转换因子
     */
    private BigDecimal conversionFactor;

    /**
     * 持有期
     */
    private int holdingPeriod;

    /**
     * CTD ctdBondCode 前台需要显示，每次的计算都需要传回
     */
    private String ctdBondCode;

    /**
     * 现券
     */
    private String bondCode;

    /**
     * 期货价格最后更新日期
     */
    private String futureLastUpdateTime;
    /**
     * 现卷收益率最后更新时间
     */
    private String yieldLastUpdateTime;
    /**
     * 用以显示盈亏曲线的数值列表
     */
    private Map<String, ClosePositionDto> profitLossByYield;

    /**
     * 冲击成本额
     */
    private BigDecimal scenarioImpactAmount;

    /**
     * yieldLastUpdateTime 是中债估值的最新收益率,如果Ofr,bid,deal收益率为空时，取CDC收益率
     */
    private Boolean isCdcYieldLastUpdateTime = false;

    /**
     * 持有期损益 ，由QDP系统计算得出
     */
    private BigDecimal holdPnl;

    public Boolean getIsCdcYieldLastUpdateTime() {
        return isCdcYieldLastUpdateTime;
    }

    public void setIsCdcYieldLastUpdateTime(Boolean isCdcYieldLastUpdateTime) {
        this.isCdcYieldLastUpdateTime = isCdcYieldLastUpdateTime;
    }

    public BigDecimal getScenarioImpactAmount() {
        return scenarioImpactAmount;
    }

    public void setScenarioImpactAmount(BigDecimal scenarioImpactAmount) {
        this.scenarioImpactAmount = scenarioImpactAmount;
    }

    public String getBondCode() {
        return bondCode;
    }

    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }

    public int getFutureNumber() {
        return futureNumber;
    }

    public void setFutureNumber(int futureNumber) {
        this.futureNumber = futureNumber;
    }

    public BigDecimal getCdcDuration() {
        return cdcDuration;
    }

    public void setCdcDuration(BigDecimal cdcDuration) {
        this.cdcDuration = cdcDuration;
    }

    public BigDecimal getCtdDuration() {
        return ctdDuration;
    }

    public void setCtdDuration(BigDecimal ctdDuration) {
        this.ctdDuration = ctdDuration;
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

    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public int getHoldingPeriod() {
        return holdingPeriod;
    }

    public void setHoldingPeriod(int holdingPeriod) {
        this.holdingPeriod = holdingPeriod;
    }

    public String getFutureLastUpdateTime() {
        return futureLastUpdateTime;
    }

    public void setFutureLastUpdateTime(String futureLastUpdateTime) {
        this.futureLastUpdateTime = futureLastUpdateTime;
    }

    public String getYieldLastUpdateTime() {
        return yieldLastUpdateTime;
    }

    public void setYieldLastUpdateTime(String yieldLastUpdateTime) {
        this.yieldLastUpdateTime = yieldLastUpdateTime;
    }

    public Map<String, ClosePositionDto> getProfitLossByYield() {
        return profitLossByYield;
    }

    public void setProfitLossByYield(Map<String, ClosePositionDto> profitLossByYield) {
        this.profitLossByYield = profitLossByYield;
    }

    public String getCtdBondCode() {
        return ctdBondCode;
    }

    public void setCtdBondCode(String ctdBondCode) {
        this.ctdBondCode = ctdBondCode;
    }

    public BigDecimal getHoldPnl() {
        return holdPnl;
    }

    public void setHoldPnl(BigDecimal holdPnl) {
        this.holdPnl = holdPnl;
    }
}
