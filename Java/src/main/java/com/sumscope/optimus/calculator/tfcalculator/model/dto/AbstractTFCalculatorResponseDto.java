package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import java.math.BigDecimal;

/**
 * 抽象计算结果Dto。该类记录了所有计算结果共用的属性。例如利差，持有期损益等
 */
public abstract class AbstractTFCalculatorResponseDto {

    /**
     * 主要结果，根据计算目标决定对应实例。一些操作只能在特定场景下进行，因此对应的响应也可能只有唯一值。比如获取页面初始计算结果时一定是IRR计算，因此返回的主要结果一定对应IRRMainResultDto。
     */
    private CalculationMainResult calculationMainResult;

    /**
     * 利差
     */
    private BigDecimal interestsRateSpread;

    /**
     * 期现价差
     */
    private BigDecimal futureSpotSpread;

    /**
     * 持有期损益
     */
    private BigDecimal carry;

    /**
     * 发票价格
     */
    private BigDecimal receiptPrice;

    /**
     * 交易日应记利息
     */
    private BigDecimal interestOnSettlementDate;

    /**
     * 交割日应记利息
     */
    private BigDecimal interestOnTradingDate;

    /**
     * 期间付息
     */
    private BigDecimal interestDuringHolding;

    /**
     * 加权平均期间付息
     */
    private BigDecimal weightedAverageInterest;

    /**
     * 异常信息
     */
    private String exception;

    public CalculationMainResult getCalculationMainResult() {
        return calculationMainResult;
    }

    public void setCalculationMainResult(CalculationMainResult calculationMainResult) {
        this.calculationMainResult = calculationMainResult;
    }

    public BigDecimal getInterestsRateSpread() {
        return interestsRateSpread;
    }

    public void setInterestsRateSpread(BigDecimal interestsRateSpread) {
        this.interestsRateSpread = interestsRateSpread;
    }

    public BigDecimal getFutureSpotSpread() {
        return futureSpotSpread;
    }

    public void setFutureSpotSpread(BigDecimal futureSpotSpread) {
        this.futureSpotSpread = futureSpotSpread;
    }

    public BigDecimal getCarry() {
        return carry;
    }

    public void setCarry(BigDecimal carry) {
        this.carry = carry;
    }

    public BigDecimal getReceiptPrice() {
        return receiptPrice;
    }

    public void setReceiptPrice(BigDecimal receiptPrice) {
        this.receiptPrice = receiptPrice;
    }

    public BigDecimal getInterestOnSettlementDate() {
        return interestOnSettlementDate;
    }

    public void setInterestOnSettlementDate(BigDecimal interestOnSettlementDate) {
        this.interestOnSettlementDate = interestOnSettlementDate;
    }

    public BigDecimal getInterestOnTradingDate() {
        return interestOnTradingDate;
    }

    public void setInterestOnTradingDate(BigDecimal interestOnTradingDate) {
        this.interestOnTradingDate = interestOnTradingDate;
    }

    public BigDecimal getInterestDuringHolding() {
        return interestDuringHolding;
    }

    public void setInterestDuringHolding(BigDecimal interestDuringHolding) {
        this.interestDuringHolding = interestDuringHolding;
    }

    public BigDecimal getWeightedAverageInterest() {
        return weightedAverageInterest;
    }

    public void setWeightedAverageInterest(BigDecimal weightedAverageInterest) {
        this.weightedAverageInterest = weightedAverageInterest;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
