package com.sumscope.optimus.calculator.planalysis.model.dbmodel;

import com.sumscope.optimus.calculator.planalysis.commons.enums.DayCount;
import com.sumscope.optimus.calculator.planalysis.commons.enums.PaymenyFrequency;
import com.sumscope.optimus.calculator.planalysis.model.dto.BondPrimaryKeyDto;

import java.math.BigDecimal;

/**
 * Created by fan.bai on 2016/5/4.
 * 用于记录债卷的信息，数据从bond表得到。
 */
public class BondInfo extends BondPrimaryKeyDto {

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 到期日期
     */
    private String maturityDate;

    /**
     * 固息利率
     * 数据库字段 Issue_Rate
     */
    private BigDecimal fixedCoupon;

    /**
     * 付息频率
     * 数据库字段 Coupon_Frequency
     */
    private PaymenyFrequency paymentFrequency;

    /**
     * 日期， 例如"Act365"
     * 数据库字段 Interest_Basis
     */
    private DayCount dayCount;

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

    public BigDecimal getFixedCoupon() {
        return fixedCoupon;
    }

    public void setFixedCoupon(BigDecimal fixedCoupon) {
        this.fixedCoupon = fixedCoupon;
    }

    public PaymenyFrequency getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(PaymenyFrequency paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public DayCount getDayCount() {
        return dayCount;
    }

    public void setDayCount(DayCount dayCount) {
        this.dayCount = dayCount;
    }
}
