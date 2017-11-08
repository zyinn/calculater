package com.sumscope.optimus.calculator.planalysis.model.qdp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 */
public class QdpAnalysis {
    @JsonProperty(value = "TradeInfo")
    public TradeInfoAnalysis tradeInfo;

    @JsonProperty(value = "DeliverableBondYtm")
    public List<KeyValuePair> deliverableBondYtm;

    @JsonProperty(value = "Direction")
    public String direction;

    @JsonProperty(value = "ImpactRate")
    public BigDecimal impactRate;

    @JsonProperty(value = "Irr")
    public String pricingRequest = "Irr";

    @JsonProperty(value = "FuturesPrice")
    public BigDecimal futuresPrice;

    @JsonProperty(value = "FundingRate")
    public BigDecimal fundingRate;

    @JsonProperty(value = "CtdBondInfo")
    public FixedRateBondInfo ctdBondInfo;

    @JsonProperty(value = "CtdBondFullPrice")
    public BigDecimal ctdBondFullPrice;

    @JsonProperty(value = "TargetBondInfo")
    public FixedRateBondInfo targetBondInfo;

    @JsonProperty(value = "TargetBondYtm")
    public BigDecimal targetBondYtm;

    @JsonProperty(value = "TargetBondDirtyPrice")
    public BigDecimal targetBondDirtyPrice;

    @JsonProperty(value = "TargetBondCleanPrice")
    public BigDecimal targetBondCleanPrice;

    @JsonProperty(value = "CurveYtm")
    public BigDecimal curveYtm;

    @JsonProperty(value = "CurveFuturesPrice")
    public BigDecimal curveFuturesPrice;

    @JsonProperty(value = "OpenPositionDate")
    public String openPositionDate;

    @JsonProperty(value = "ClosePositionDate")
    public String closePositionDate;

    @JsonProperty(value = "Guid")
    public String guid = "ca2f7ba4-8503-450c-a46a-6ef29744e789";

    @JsonProperty(value = "TimeOutInSeconds")
    public int timeOutInSeconds = 500;

    public TradeInfoAnalysis getTradeInfo() {
        return tradeInfo;
    }

    public void setTradeInfo(TradeInfoAnalysis tradeInfo) {
        this.tradeInfo = tradeInfo;
    }

    public List<KeyValuePair> getDeliverableBondYtm() {
        return deliverableBondYtm;
    }

    public void setDeliverableBondYtm(List<KeyValuePair> deliverableBondYtm) {
        this.deliverableBondYtm = deliverableBondYtm;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BigDecimal getImpactRate() {
        return impactRate;
    }

    public void setImpactRate(BigDecimal impactRate) {
        this.impactRate = impactRate;
    }

    public String getPricingRequest() {
        return pricingRequest;
    }

    public void setPricingRequest(String pricingRequest) {
        this.pricingRequest = pricingRequest;
    }

    public BigDecimal getFuturesPrice() {
        return futuresPrice;
    }

    public void setFuturesPrice(BigDecimal futuresPrice) {
        this.futuresPrice = futuresPrice;
    }

    public BigDecimal getFundingRate() {
        return fundingRate;
    }

    public void setFundingRate(BigDecimal fundingRate) {
        this.fundingRate = fundingRate;
    }

    public FixedRateBondInfo getCtdBondInfo() {
        return ctdBondInfo;
    }

    public void setCtdBondInfo(FixedRateBondInfo ctdBondInfo) {
        this.ctdBondInfo = ctdBondInfo;
    }

    public BigDecimal getCtdBondFullPrice() {
        return ctdBondFullPrice;
    }

    public void setCtdBondFullPrice(BigDecimal ctdBondFullPrice) {
        this.ctdBondFullPrice = ctdBondFullPrice;
    }

    public FixedRateBondInfo getTargetBondInfo() {
        return targetBondInfo;
    }

    public void setTargetBondInfo(FixedRateBondInfo targetBondInfo) {
        this.targetBondInfo = targetBondInfo;
    }

    public BigDecimal getTargetBondYtm() {
        return targetBondYtm;
    }

    public void setTargetBondYtm(BigDecimal targetBondYtm) {
        this.targetBondYtm = targetBondYtm;
    }

    public BigDecimal getTargetBondDirtyPrice() {
        return targetBondDirtyPrice;
    }

    public void setTargetBondDirtyPrice(BigDecimal targetBondDirtyPrice) {
        this.targetBondDirtyPrice = targetBondDirtyPrice;
    }

    public BigDecimal getTargetBondCleanPrice() {
        return targetBondCleanPrice;
    }

    public void setTargetBondCleanPrice(BigDecimal targetBondCleanPrice) {
        this.targetBondCleanPrice = targetBondCleanPrice;
    }

    public BigDecimal getCurveYtm() {
        return curveYtm;
    }

    public void setCurveYtm(BigDecimal curveYtm) {
        this.curveYtm = curveYtm;
    }

    public BigDecimal getCurveFuturesPrice() {
        return curveFuturesPrice;
    }

    public void setCurveFuturesPrice(BigDecimal curveFuturesPrice) {
        this.curveFuturesPrice = curveFuturesPrice;
    }

    public String getOpenPositionDate() {
        return openPositionDate;
    }

    public void setOpenPositionDate(String openPositionDate) {
        this.openPositionDate = openPositionDate;
    }

    public String getClosePositionDate() {
        return closePositionDate;
    }

    public void setClosePositionDate(String closePositionDate) {
        this.closePositionDate = closePositionDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    public void setTimeOutInSeconds(int timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }
}
