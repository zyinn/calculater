package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.TargetBonds;

import java.math.BigDecimal;

/**
 * 在计算目标为IRR或者债卷价格时，当用户企图获取期货最新价格进行计算时使用该请求Dto
 */
public class GetFuturePriceRequestDto extends AbstractTFCalculatorRequestDto {

    /**
     * 当前页面的债卷收益率
     */
    private BigDecimal bondYield;

    /**
     * 债券信息
     */
    private BondInfo bondInfo;

    /**
     *未发国债id
     */
    private String scheduleBondId;

    /**
     * 窗口标识
     */
    private TargetBonds targetBonds;

    /**
     *  票息率
     */
    private BigDecimal fixedCoupon;

    public TargetBonds getTargetBonds() {
        return targetBonds;
    }

    public BigDecimal getFixedCoupon() {
        return fixedCoupon;
    }

    public void setFixedCoupon(BigDecimal fixedCoupon) {
        this.fixedCoupon = fixedCoupon;
    }

    public void setTargetBonds(TargetBonds targetBonds) {
        this.targetBonds = targetBonds;
    }

    public String getScheduleBondId() {
        return scheduleBondId;
    }

    public void setScheduleBondId(String scheduleBondId) {
        this.scheduleBondId = scheduleBondId;
    }

    public BigDecimal getBondYield() {
        return bondYield;
    }

    public void setBondYield(BigDecimal bondYield) {
        this.bondYield = bondYield;
    }

    public BondInfo getBondInfo() {
        return bondInfo;
    }

    public void setBondInfo(BondInfo bondInfo) {
        this.bondInfo = bondInfo;
    }
}
