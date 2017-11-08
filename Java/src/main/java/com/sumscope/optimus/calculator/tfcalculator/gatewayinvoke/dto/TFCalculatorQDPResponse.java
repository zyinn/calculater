package com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sumscope.optimus.calculator.planalysis.model.qdp.KeyValuePair;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;

public class TFCalculatorQDPResponse implements java.io.Serializable{

    @JsonProperty(value = "AiEnd")
    private KeyValuePair[] aiEnd;//交割日应计利息

    @JsonProperty(value = "AiStart")
    private KeyValuePair[] aiStart;//交易日=开仓日应计利息

    @JsonProperty(value = "Basis")
    private KeyValuePair[] basis; //基差

    @JsonProperty(value = "BondCleanPrice")
    private KeyValuePair[] bondCleanPrice;//净价

    @JsonProperty(value = "BondDirtyPrice")
    private KeyValuePair[] bondDirtyPrice;//全价

    @JsonProperty(value = "BondYtm")
    private KeyValuePair[] bondYtm;//债券收益率

    @JsonProperty(value = "ConversionFactors")
    private KeyValuePair[] conversionFactors;//转换因子

    @JsonProperty(value = "Coupon")
    private KeyValuePair[] coupon;//期间付息

    @JsonProperty(value = "FuturesPrice")
    private KeyValuePair[] futuresPrice;//期货价格

    @JsonProperty(value = "InterestCost")
    private KeyValuePair[] interestCost;//利息成本

    @JsonProperty(value = "InterestIncome")
    private KeyValuePair[] interestIncome;//利息回报

    @JsonProperty(value = "InvoicePrice")
    private KeyValuePair[] invoicePrice;//发票价格

    @JsonProperty(value = "Irr")
    private KeyValuePair[] irr;//irr值

    @JsonProperty(value = "Margin")
    private KeyValuePair[] margin;//期现价差

    @JsonProperty(value = "MaturityMoveDayYtm")
    private KeyValuePair[] maturityMoveDayYtm;//T+1收益率

    @JsonProperty(value = "ModifiedDuration")
    private KeyValuePair[] modifiedDuration;//修正久期

    @JsonProperty(value = "NetBasis")
    private KeyValuePair[] netBasis;//净基差

    @JsonProperty(value = "PnL")
    private KeyValuePair[] pnL;//损益

    @JsonProperty(value = "Spread")
    private KeyValuePair[] spread;//利差

    @JsonProperty(value = "TimeWeightedCoupon")
    private KeyValuePair[] timeWeightedCoupon;//加权平均期间付息

    @JsonProperty(value = "Succeeded")
    private boolean succeeded;//是否计算成功字段

    private CalculationTarget calculationTarget;

    public TFCalculatorQDPResponse() {
    }

    public TFCalculatorQDPResponse(KeyValuePair[] aiEnd, KeyValuePair[] aiStart, KeyValuePair[] basis, KeyValuePair[] bondCleanPrice, KeyValuePair[] bondDirtyPrice, KeyValuePair[] bondYtm, KeyValuePair[] conversionFactors, KeyValuePair[] coupon, KeyValuePair[] futuresPrice, KeyValuePair[] interestCost, KeyValuePair[] interestIncome, KeyValuePair[] invoicePrice, KeyValuePair[] irr, KeyValuePair[] margin, KeyValuePair[] maturityMoveDayYtm, KeyValuePair[] modifiedDuration, KeyValuePair[] netBasis, KeyValuePair[] pnL, KeyValuePair[] spread, KeyValuePair[] timeWeightedCoupon,boolean succeeded,CalculationTarget calculationTarget) {
        this.aiEnd = aiEnd;
        this.aiStart = aiStart;
        this.basis = basis;
        this.bondCleanPrice = bondCleanPrice;
        this.bondDirtyPrice = bondDirtyPrice;
        this.bondYtm = bondYtm;
        this.conversionFactors = conversionFactors;
        this.coupon = coupon;
        this.futuresPrice = futuresPrice;
        this.interestCost = interestCost;
        this.interestIncome = interestIncome;
        this.invoicePrice = invoicePrice;
        this.irr = irr;
        this.margin = margin;
        this.maturityMoveDayYtm = maturityMoveDayYtm;
        this.modifiedDuration = modifiedDuration;
        this.netBasis = netBasis;
        this.pnL = pnL;
        this.spread = spread;
        this.timeWeightedCoupon = timeWeightedCoupon;
        this.succeeded = succeeded;
        this.calculationTarget = calculationTarget;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public CalculationTarget getCalculationTarget() {
        return calculationTarget;
    }

    public void setCalculationTarget(CalculationTarget calculationTarget) {
        this.calculationTarget = calculationTarget;
    }

    public KeyValuePair[] getAiEnd() {
        return aiEnd;
    }

    public void setAiEnd(KeyValuePair[] aiEnd) {
        this.aiEnd = aiEnd;
    }

    public KeyValuePair[] getAiStart() {
        return aiStart;
    }

    public void setAiStart(KeyValuePair[] aiStart) {
        this.aiStart = aiStart;
    }

    public KeyValuePair[] getBasis() {
        return basis;
    }

    public void setBasis(KeyValuePair[] basis) {
        this.basis = basis;
    }

    public KeyValuePair[] getBondCleanPrice() {
        return bondCleanPrice;
    }

    public void setBondCleanPrice(KeyValuePair[] bondCleanPrice) {
        this.bondCleanPrice = bondCleanPrice;
    }

    public KeyValuePair[] getBondDirtyPrice() {
        return bondDirtyPrice;
    }

    public void setBondDirtyPrice(KeyValuePair[] bondDirtyPrice) {
        this.bondDirtyPrice = bondDirtyPrice;
    }

    public KeyValuePair[] getBondYtm() {
        return bondYtm;
    }

    public void setBondYtm(KeyValuePair[] bondYtm) {
        this.bondYtm = bondYtm;
    }

    public KeyValuePair[] getConversionFactors() {
        return conversionFactors;
    }

    public void setConversionFactors(KeyValuePair[] conversionFactors) {
        this.conversionFactors = conversionFactors;
    }

    public KeyValuePair[] getCoupon() {
        return coupon;
    }

    public void setCoupon(KeyValuePair[] coupon) {
        this.coupon = coupon;
    }

    public KeyValuePair[] getFuturesPrice() {
        return futuresPrice;
    }

    public void setFuturesPrice(KeyValuePair[] futuresPrice) {
        this.futuresPrice = futuresPrice;
    }

    public KeyValuePair[] getInterestCost() {
        return interestCost;
    }

    public void setInterestCost(KeyValuePair[] interestCost) {
        this.interestCost = interestCost;
    }

    public KeyValuePair[] getInterestIncome() {
        return interestIncome;
    }

    public void setInterestIncome(KeyValuePair[] interestIncome) {
        this.interestIncome = interestIncome;
    }

    public KeyValuePair[] getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(KeyValuePair[] invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public KeyValuePair[] getIrr() {
        return irr;
    }

    public void setIrr(KeyValuePair[] irr) {
        this.irr = irr;
    }

    public KeyValuePair[] getMargin() {
        return margin;
    }

    public void setMargin(KeyValuePair[] margin) {
        this.margin = margin;
    }

    public KeyValuePair[] getMaturityMoveDayYtm() {
        return maturityMoveDayYtm;
    }

    public void setMaturityMoveDayYtm(KeyValuePair[] maturityMoveDayYtm) {
        this.maturityMoveDayYtm = maturityMoveDayYtm;
    }

    public KeyValuePair[] getModifiedDuration() {
        return modifiedDuration;
    }

    public void setModifiedDuration(KeyValuePair[] modifiedDuration) {
        this.modifiedDuration = modifiedDuration;
    }

    public KeyValuePair[] getNetBasis() {
        return netBasis;
    }

    public void setNetBasis(KeyValuePair[] netBasis) {
        this.netBasis = netBasis;
    }

    public KeyValuePair[] getPnL() {
        return pnL;
    }

    public void setPnL(KeyValuePair[] pnL) {
        this.pnL = pnL;
    }

    public KeyValuePair[] getSpread() {
        return spread;
    }

    public void setSpread(KeyValuePair[] spread) {
        this.spread = spread;
    }

    public KeyValuePair[] getTimeWeightedCoupon() {
        return timeWeightedCoupon;
    }

    public void setTimeWeightedCoupon(KeyValuePair[] timeWeightedCoupon) {
        this.timeWeightedCoupon = timeWeightedCoupon;
    }
}
