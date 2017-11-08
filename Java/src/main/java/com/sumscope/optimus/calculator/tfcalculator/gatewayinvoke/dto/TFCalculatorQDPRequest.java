package com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sumscope.optimus.calculator.planalysis.model.qdp.KeyValuePair;
import com.sumscope.optimus.calculator.planalysis.model.qdp.TradeInfo;
import java.math.BigDecimal;
import java.util.List;

public class TFCalculatorQDPRequest {
    //每次请求生成一个id
    @JsonProperty("Guid")
    private String guid="d5bdc249-3377-4176-abe9-33c984bb27dc";
    //默认500
    @JsonProperty("TimeOutInSeconds")
    private Integer timeOutInSeconds=500;
    //国债期货信息
    @JsonProperty("TradeInfo")
    private TradeInfo tradeInfo;
    //Irr计算Irr/FairQuote计算期货价格 /UnderlyingFairQuote计算可交割券价格/MktQuote计算期货理论价格
    @JsonProperty("PricingRequest")
    private String pricingRequest;
    //期货价格
    @JsonProperty("FuturesPrice")
    private BigDecimal futuresPrice;
    //irr
    @JsonProperty("IrrRate")
    private BigDecimal irrRate;
    //资金成本
    @JsonProperty("FundingRate")
    private BigDecimal fundingRate;
    //交割债券到期收益率
    @JsonProperty("DeliverableBondYtm")
    private List<KeyValuePair> deliverableBondYtm;
    //交割债券全价
    @JsonProperty("DeliverableBondDirtyPrices")
    private  List<KeyValuePair> deliverableBondDirtyPrices;
    //交割债券净价
    @JsonProperty("DeliverableBondCleanPrices")
    private  List<KeyValuePair> deliverableBondCleanPrices;
    //交割债券基差
    @JsonProperty("DeliverableBondBasis")
    private List<KeyValuePair> deliverableBondBasis;
    //交割债券净基差
    @JsonProperty("DeliverableBondNetBasis")
    private List<KeyValuePair> deliverableBondNetBasis;
    //到期日
    @JsonProperty("MaturityMoveDay")
    private Integer maturityMoveDay;
    //试算时间
    @JsonProperty("CountPerUpdate")
    private String countPerUpdate=null;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Integer getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    public void setTimeOutInSeconds(Integer timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }

    public TradeInfo getTradeInfo() {
        return tradeInfo;
    }

    public void setTradeInfo(TradeInfo tradeInfo) {
        this.tradeInfo = tradeInfo;
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

    public BigDecimal getIrrRate() {
        return irrRate;
    }

    public void setIrrRate(BigDecimal irrRate) {
        this.irrRate = irrRate;
    }

    public BigDecimal getFundingRate() {
        return fundingRate;
    }

    public void setFundingRate(BigDecimal fundingRate) {
        this.fundingRate = fundingRate;
    }

    public List<KeyValuePair> getDeliverableBondYtm() {
        return deliverableBondYtm;
    }

    public void setDeliverableBondYtm(List<KeyValuePair> deliverableBondYtm) {
        this.deliverableBondYtm = deliverableBondYtm;
    }

    public List<KeyValuePair> getDeliverableBondDirtyPrices() {
        return deliverableBondDirtyPrices;
    }

    public void setDeliverableBondDirtyPrices(List<KeyValuePair> deliverableBondDirtyPrices) {
        this.deliverableBondDirtyPrices = deliverableBondDirtyPrices;
    }

    public List<KeyValuePair> getDeliverableBondCleanPrices() {
        return deliverableBondCleanPrices;
    }

    public void setDeliverableBondCleanPrices(List<KeyValuePair> deliverableBondCleanPrices) {
        this.deliverableBondCleanPrices = deliverableBondCleanPrices;
    }

    public List<KeyValuePair> getDeliverableBondBasis() {
        return deliverableBondBasis;
    }

    public void setDeliverableBondBasis(List<KeyValuePair> deliverableBondBasis) {
        this.deliverableBondBasis = deliverableBondBasis;
    }

    public List<KeyValuePair> getDeliverableBondNetBasis() {
        return deliverableBondNetBasis;
    }

    public void setDeliverableBondNetBasis(List<KeyValuePair> deliverableBondNetBasis) {
        this.deliverableBondNetBasis = deliverableBondNetBasis;
    }

    public Integer getMaturityMoveDay() {
        return maturityMoveDay;
    }

    public void setMaturityMoveDay(Integer maturityMoveDay) {
        this.maturityMoveDay = maturityMoveDay;
    }

    public String getCountPerUpdate() {
        return countPerUpdate;
    }

    public void setCountPerUpdate(String countPerUpdate) {
        this.countPerUpdate = countPerUpdate;
    }


//    public String getGuid() {
//        return Guid;
//    }
//
//    public void setGuid(String guid) {
//        Guid = guid;
//    }
//
//    public Integer getTimeOutInSeconds() {
//        return TimeOutInSeconds;
//    }
//
//    public void setTimeOutInSeconds(Integer timeOutInSeconds) {
//        TimeOutInSeconds = timeOutInSeconds;
//    }
//
//    public com.sumscope.optimus.calculator.planalysis.model.qdp.TradeInfo getTradeInfo() {
//        return TradeInfo;
//    }
//
//    public void setTradeInfo(com.sumscope.optimus.calculator.planalysis.model.qdp.TradeInfo tradeInfo) {
//        TradeInfo = tradeInfo;
//    }
//
//    public String getPricingRequest() {
//        return PricingRequest;
//    }
//
//    public void setPricingRequest(String pricingRequest) {
//        PricingRequest = pricingRequest;
//    }
//
//    public BigDecimal getFuturesPrice() {
//        return FuturesPrice;
//    }
//
//    public void setFuturesPrice(BigDecimal futuresPrice) {
//        FuturesPrice = futuresPrice;
//    }
//
//    public BigDecimal getIrrRate() {
//        return IrrRate;
//    }
//
//    public void setIrrRate(BigDecimal irrRate) {
//        IrrRate = irrRate;
//    }
//
//    public BigDecimal getFundingRate() {
//        return FundingRate;
//    }
//
//    public void setFundingRate(BigDecimal fundingRate) {
//        FundingRate = fundingRate;
//    }
//
//    public List<KeyValuePair> getDeliverableBondYtm() {
//        return DeliverableBondYtm;
//    }
//
//    public void setDeliverableBondYtm(List<KeyValuePair> deliverableBondYtm) {
//        DeliverableBondYtm = deliverableBondYtm;
//    }
//
//    public BigDecimal getDeliverableBondDirtyPrices() {
//        return DeliverableBondDirtyPrices;
//    }
//
//    public void setDeliverableBondDirtyPrices(BigDecimal deliverableBondDirtyPrices) {
//        DeliverableBondDirtyPrices = deliverableBondDirtyPrices;
//    }
//
//    public BigDecimal getDeliverableBondCleanPrices() {
//        return DeliverableBondCleanPrices;
//    }
//
//    public void setDeliverableBondCleanPrices(BigDecimal deliverableBondCleanPrices) {
//        DeliverableBondCleanPrices = deliverableBondCleanPrices;
//    }
//
//    public BigDecimal getDeliverableBondBasis() {
//        return DeliverableBondBasis;
//    }
//
//    public void setDeliverableBondBasis(BigDecimal deliverableBondBasis) {
//        DeliverableBondBasis = deliverableBondBasis;
//    }
//
//    public BigDecimal getDeliverableBondNetBasis() {
//        return DeliverableBondNetBasis;
//    }
//
//    public void setDeliverableBondNetBasis(BigDecimal deliverableBondNetBasis) {
//        DeliverableBondNetBasis = deliverableBondNetBasis;
//    }
//
//    public Integer getMaturityMoveDay() {
//        return MaturityMoveDay;
//    }
//
//    public void setMaturityMoveDay(Integer maturityMoveDay) {
//        MaturityMoveDay = maturityMoveDay;
//    }
//
//    public String getCountPerUpdate() {
//        return CountPerUpdate;
//    }
//
//    public void setCountPerUpdate(String countPerUpdate) {
//        CountPerUpdate = countPerUpdate;
//    }
}
