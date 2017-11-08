package com.sumscope.optimus.calculator.shared.model.dbmodel;

import java.math.BigDecimal;

/**
 * Created by fan.bai on 2016/7/11.
 * 债卷的CDC信息，包含中债价格和久期
 */
public class BondCDCValuationInfo {

    /**
     * 中债收益率
     */
    private BigDecimal yield;

    /**
     * 中债久期
     */
    private BigDecimal modifiedDuration;

    public BigDecimal getYield() {
        return yield;
    }

    public void setYield(BigDecimal yield) {
        this.yield = yield;
    }

    public BigDecimal getModifiedDuration() {
        return modifiedDuration;
    }

    public void setModifiedDuration(BigDecimal modifiedDuration) {
        this.modifiedDuration = modifiedDuration;
    }
}
