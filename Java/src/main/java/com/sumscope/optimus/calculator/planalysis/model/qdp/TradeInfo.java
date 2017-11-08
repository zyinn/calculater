package com.sumscope.optimus.calculator.planalysis.model.qdp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sumscope.optimus.calculator.planalysis.commons.enums.DayCount;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by simon.mao on 2016/5/10.
 */
public class TradeInfo {
    @JsonProperty(value = "StartDate")
    public String startDate;

    @JsonProperty(value = "MaturityDate")
    public String maturityDate;

    @JsonProperty(value = "Notional")
    public long notional = 100;

    @JsonProperty(value = "Calendar")
    public String calendar = "chn";

    @JsonProperty(value = "Currency")
    public String currency = "CNY";

    @JsonProperty(value = "DayCount")
    public String dayCount = DayCount.BAF.getCalculatorEnumName();

    @JsonProperty(value = "NonimalCoupon")
    public BigDecimal nonimalCoupon = new BigDecimal(0.03).setScale(2,BigDecimal.ROUND_HALF_UP);

    @JsonProperty(value = "DeliverableBondInfos")
    public List<FixedRateBondInfo> deliverableBondInfos;

    @JsonProperty(value = "ValuationParamters")
    public ValuationParamter valuationParamters;

    @JsonProperty(value = "TradeId")
    public String tradeId;

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

    public long getNotional() {
        return notional;
    }

    public void setNotional(long notional) {
        this.notional = notional;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public List<FixedRateBondInfo> getDeliverableBondInfos() {
        return deliverableBondInfos;
    }

    public void setDeliverableBondInfos(List<FixedRateBondInfo> deliverableBondInfos) {
        this.deliverableBondInfos = deliverableBondInfos;
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

    public BigDecimal getNonimalCoupon() {
        return nonimalCoupon;
    }

    public void setNonimalCoupon(BigDecimal nonimalCoupon) {
        this.nonimalCoupon = nonimalCoupon;
    }
}
