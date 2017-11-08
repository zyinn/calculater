package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.tfcalculator.commons.enums.TargetBonds;

/**
 * 当用户改变了当前债卷时使用该请求Dto
 */
public class BondChangedRequestDto extends AbstractTFCalculatorRequestDto {

    /**
     * 债卷key，与listedMarket一起作为债卷的业务主键
     */
    private String bondKey;

    /**
     * 债卷发行市场，与bondKey一起作为债卷的业务主键
     */
    private String listedMarket;

    /**
     * 债劵Id
     */
    private String bondId;

    private TargetBonds targetBonds;

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

    public String getBondId() {
        return bondId;
    }

    public void setBondId(String bondId) {
        this.bondId = bondId;
    }

    public TargetBonds getTargetBonds() {
        return targetBonds;
    }

    public void setTargetBonds(TargetBonds targetBonds) {
        this.targetBonds = targetBonds;
    }
}
