package com.sumscope.optimus.calculator.planalysis.model.dto;

import java.math.BigDecimal;

/**
 * Created by simon.mao on 2016/4/28.
 * 请求与响应DTO共享一部分数据，该类记录这些数据
 */
public abstract class CommonDto {
    /**
     * 期货基本信息
     */
    private FutureContractDto futureContract;
    /**
     * 期货价格
     */
    private BigDecimal futurePrice;
    /**
     * CTD全价
     */
    private BigDecimal ctdFullPrice;

    /**
     * CTD bond_key，前台记录单不显示，传回用以计算
     */
    private String ctdBondKey;
    /**
     * CTD listed_market， 前台记录不显示，传回用以计算
     */
    private String ctdListedMarket;

    /**
     * 现卷 bond_key， 前台记录不显示，传回用以计算
     */
    private String bondKey;
    /**
     * 现卷 listed_market， 前台记录不显示，传回用以计算
     */
    private String bondListedMarket;

    /**
     * 现卷收益率，作为request时只能存在一个，与bondFullPrice和bondNetPrice互斥，
     */
    private BigDecimal yield;
    /**
     * 现卷净价，作为request时只能存在一个，与yield和bondFullPrice互斥
     */
    private BigDecimal bondNetPrice;
    /**
     * 现卷全价，作为request时只能存在一个，与yield和bondNetPrice互斥
     */
    private BigDecimal bondFullPrice;

    public BigDecimal getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(BigDecimal futurePrice) {
        this.futurePrice = futurePrice;
    }

    public BigDecimal getCtdFullPrice() {
        return ctdFullPrice;
    }

    public void setCtdFullPrice(BigDecimal ctdFullPrice) {
        this.ctdFullPrice = ctdFullPrice;
    }

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

    public String getCtdBondKey() {
        return ctdBondKey;
    }

    public void setCtdBondKey(String ctdBondKey) {
        this.ctdBondKey = ctdBondKey;
    }

    public String getCtdListedMarket() {
        return ctdListedMarket;
    }

    public void setCtdListedMarket(String ctdListedMarket) {
        this.ctdListedMarket = ctdListedMarket;
    }

    public String getBondKey() {
        return bondKey;
    }

    public void setBondKey(String bondKey) {
        this.bondKey = bondKey;
    }

    public String getBondListedMarket() {
        return bondListedMarket;
    }

    public void setBondListedMarket(String bondListedMarket) {
        this.bondListedMarket = bondListedMarket;
    }

    public FutureContractDto getFutureContract() {
        return futureContract;
    }

    public void setFutureContract(FutureContractDto futureContract) {
        this.futureContract = futureContract;
    }
}
