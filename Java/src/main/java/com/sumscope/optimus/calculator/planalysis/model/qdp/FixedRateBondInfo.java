package com.sumscope.optimus.calculator.planalysis.model.qdp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sumscope.optimus.calculator.planalysis.commons.enums.DayCount;

import java.math.BigDecimal;

/**
 * Created by simon.mao on 2016/5/10.
 */
public class FixedRateBondInfo {
    @JsonProperty(value = "FixedCoupon")
    public BigDecimal fixedCoupon;

    @JsonProperty(value = "StartDate")
    public String startDate;

    @JsonProperty(value = "MaturityDate")
    public String maturityDate;

    @JsonProperty(value = "Calendar")
    public String calendar = "chn_ib";

    @JsonProperty(value = "PaymentFrequency")
    public String paymentFrequency;

    @JsonProperty(value = "PaymentStub")
    public String paymentStub = "LongEnd";

    @JsonProperty(value = "Notional")
    public int notional = 100;

    @JsonProperty(value = "Currency")
    public String currency = "CNY";

    @JsonProperty(value = "DayCount")
    public String dayCount = DayCount.BAF.getCalculatorEnumName();

    @JsonProperty(value = "AccrualBusinessDayConvention")
    public String accrualBusinessDayConvention = "None";

    @JsonProperty(value = "PaymentBusinessDayConvention")
    public String paymentBusinessDayConvention = "ModifiedFollowing";

    @JsonProperty(value = "Settlment")
    public String settlment = "+0BD";

    @JsonProperty(value = "TradingMarket")
    public String tradingMarket = "ChinaInterBank";

    @JsonProperty(value = "IsZeroCouponBond")
    public boolean isZeroCouponBond = false;

    @JsonProperty(value = "IssuePrice")
    public BigDecimal issuePrice = new BigDecimal(0);

    @JsonProperty(value = "FirstPaymentDate")
    public String firstPaymentDate = null;

    @JsonProperty(value = "Amoritzation")
    public String amoritzation = null;

    @JsonProperty(value = "ValuationParamters")
    public ValuationParamter valuationParamters=null;// new ValuationParamter()

    @JsonProperty(value = "TradeId")
    public String tradeId;

    @JsonProperty(value = "AmoritzationInDate")
    public String AmoritzationInDate = null;

    @JsonProperty(value = "AmoritzationInIndex")
    public String AmoritzationInIndex = null;

    @JsonProperty(value = "StickToEom")
    public boolean StickToEom = false;

    @JsonProperty(value = "AccrualDayCount")
    public String AccrualDayCount = DayCount.BAF.getCalculatorEnumName();

    public BigDecimal getFixedCoupon() {
        return fixedCoupon;
    }

    public void setFixedCoupon(BigDecimal fixedCoupon) {
        this.fixedCoupon = fixedCoupon;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public String getPaymentStub() {
        return paymentStub;
    }

    public void setPaymentStub(String paymentStub) {
        this.paymentStub = paymentStub;
    }

    public int getNotional() {
        return notional;
    }

    public void setNotional(int notional) {
        this.notional = notional;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccrualBusinessDayConvention() {
        return accrualBusinessDayConvention;
    }

    public void setAccrualBusinessDayConvention(String accrualBusinessDayConvention) {
        this.accrualBusinessDayConvention = accrualBusinessDayConvention;
    }

    public String getPaymentBusinessDayConvention() {
        return paymentBusinessDayConvention;
    }

    public void setPaymentBusinessDayConvention(String paymentBusinessDayConvention) {
        this.paymentBusinessDayConvention = paymentBusinessDayConvention;
    }

    public String getSettlment() {
        return settlment;
    }

    public void setSettlment(String settlment) {
        this.settlment = settlment;
    }

    public String getTradingMarket() {
        return tradingMarket;
    }

    public void setTradingMarket(String tradingMarket) {
        this.tradingMarket = tradingMarket;
    }

    public BigDecimal getIssuePrice() {
        return issuePrice;
    }

    public void setIssuePrice(BigDecimal issuePrice) {
        this.issuePrice = issuePrice;
    }

    public String getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(String firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public String getAmoritzation() {
        return amoritzation;
    }

    public void setAmoritzation(String amoritzation) {
        this.amoritzation = amoritzation;
    }

    public ValuationParamter getValuationParamters() {
        return valuationParamters;
    }

    public void setValuationParamters(ValuationParamter valuationParamters) {
        this.valuationParamters = valuationParamters;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

}
