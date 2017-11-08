package com.sumscope.optimus.calculator.planalysis.model.dto;

/**
 * Created by fan.bai on 2016/5/3.
 * 用于记录债卷业务主键信息，bondKey与listedMarket是一个债卷的业务主键，bondCode则是一个债卷的显示信息
 * 该结构用于提供根据用户输入显示可选固息债的功能， 可从mv_bond_enhanced表可读取
 */
public class BondPrimaryKeyDto {

    /**
     * 债卷code，例如: 150016.IB
     */
    private String bondCode;

    /**
     * 债卷key，与listedMarket共同构成业务主键.例如 Z0003002015GOVBGB02
     */
    private String bondKey;
    /**
     * 债卷交易所，与bondKey共同构成业务主键，例如：CIB
     */
    private String listedMarket;

    private String shortName;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getBondCode() {
        return bondCode;
    }

    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }

    public String getBondKey() {
        return bondKey;
    }

    public void setBondKey(String bondKey) {
        this.bondKey = bondKey;
    }

    public String getListedMarket() {
        return listedMarket;
    }

    public void setListedMarket(String listedMarket) {
        this.listedMarket = listedMarket;
    }
}
