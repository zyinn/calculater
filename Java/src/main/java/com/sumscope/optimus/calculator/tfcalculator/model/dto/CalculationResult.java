package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import java.math.BigDecimal;

/**
 * 用于excel导出的javabean
 * Created by xuejian.sun on 2016/9/6.
 */
public class CalculationResult {
    /**
     * 期货的id
     */
    private String tfID;

    /**
     * 债券代码  12121.IB
     */
    private String bondCode;

    /**
     * 期货价格
     */
    private double futurePrice;

    /**
     * 债券收益率
     */
    private double bondRate;

    /**
     * 债券净价
     */
    private double bondNetPrice;

    /**
     * 转换因子
     */
    private double convertionFactor;

    /**
     *  IRR
     */
    private double irr;

    /**
     * 基差
     */
    private  double basis;

    /**
     * 净基差
     */
    private double netBasis;

    /**
     * 试算时间
     */
    private String trialTime;

    /**
     * T+1收益率
     */
    private Double bondRateByAddDay;


    /**
     * 交易日
     */
    private String tradingDate;
    /**
     * 交割日
     */
    private String deliveryDate;
    /**
     * 资金成本
     */
    private double capitalCost;
    /**
     * 债券全价
     */
    private double bondFullPrice;
    /**
     * 持有期损益
     */
    private double carry;
    /**
     * 发票价格
     */
    private double receiptPrice;

    /**
     * 期现价差
     */
    private double futureSpotSpread;

    /**
     * 利差
     */
    private double interestsRateSpread;
    /**
     * 交易日应记利息
     */
    private double interestOnSettlementDate;

    /**
     * 交割日应记利息
     */
    private double interestOnTradingDate;

    /**
     * 期间付息
     */
    private double interestDuringHolding;

    /**
     * 加权平均期间付息
     */
    private double weightedAverageInterest;


    public String getTfID() {
        return tfID;
    }

    public void setTfID(String tfID) {
        this.tfID = tfID;
    }

    public String getBondCode() {
        return bondCode;
    }

    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }

    public double getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(double futurePrice) {
        this.futurePrice = futurePrice;
    }

    public double getBondRate() {
        return bondRate;
    }

    public void setBondRate(double bondRate) {
        this.bondRate = bondRate;
    }

    public double getBondNetPrice() {
        return bondNetPrice;
    }

    public void setBondNetPrice(double bondNetPrice) {
        this.bondNetPrice = bondNetPrice;
    }

    public double getConvertionFactor() {
        return convertionFactor;
    }

    public void setConvertionFactor(double convertionFactor) {
        this.convertionFactor = convertionFactor;
    }

    public double getIrr() {
        return irr;
    }

    public void setIrr(double irr) {
        this.irr = irr;
    }

    public double getBasis() {
        return basis;
    }

    public void setBasis(double basis) {
        this.basis = basis;
    }

    public double getNetBasis() {
        return netBasis;
    }

    public void setNetBasis(double netBasis) {
        this.netBasis = netBasis;
    }

    public String getTrialTime() {
        return trialTime;
    }

    public void setTrialTime(String trialTime) {
        this.trialTime = trialTime;
    }

    public Double getBondRateByAddDay() {
        return bondRateByAddDay;
    }

    public void setBondRateByAddDay(Double bondRateByAddDay) {
        this.bondRateByAddDay = bondRateByAddDay;
    }


    public String getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(String tradingDate) {
        this.tradingDate = tradingDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getCapitalCost() {
        return capitalCost;
    }

    public void setCapitalCost(double capitalCost) {
        this.capitalCost = capitalCost;
    }

    public double getBondFullPrice() {
        return bondFullPrice;
    }

    public void setBondFullPrice(double bondFullPrice) {
        this.bondFullPrice = bondFullPrice;
    }

    public double getCarry() {
        return carry;
    }

    public void setCarry(double carry) {
        this.carry = carry;
    }

    public double getReceiptPrice() {
        return receiptPrice;
    }

    public void setReceiptPrice(double receiptPrice) {
        this.receiptPrice = receiptPrice;
    }

    public double getFutureSpotSpread() {
        return futureSpotSpread;
    }

    public void setFutureSpotSpread(double futureSpotSpread) {
        this.futureSpotSpread = futureSpotSpread;
    }

    public double getInterestsRateSpread() {
        return interestsRateSpread;
    }

    public void setInterestsRateSpread(double interestsRateSpread) {
        this.interestsRateSpread = interestsRateSpread;
    }

    public double getInterestOnSettlementDate() {
        return interestOnSettlementDate;
    }

    public void setInterestOnSettlementDate(double interestOnSettlementDate) {
        this.interestOnSettlementDate = interestOnSettlementDate;
    }

    public double getInterestOnTradingDate() {
        return interestOnTradingDate;
    }

    public void setInterestOnTradingDate(double interestOnTradingDate) {
        this.interestOnTradingDate = interestOnTradingDate;
    }

    public double getInterestDuringHolding() {
        return interestDuringHolding;
    }

    public void setInterestDuringHolding(double interestDuringHolding) {
        this.interestDuringHolding = interestDuringHolding;
    }

    public double getWeightedAverageInterest() {
        return weightedAverageInterest;
    }

    public void setWeightedAverageInterest(double weightedAverageInterest) {
        this.weightedAverageInterest = weightedAverageInterest;
    }
}
