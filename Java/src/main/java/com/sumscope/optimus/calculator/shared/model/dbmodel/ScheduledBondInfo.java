package com.sumscope.optimus.calculator.shared.model.dbmodel;

import com.sumscope.optimus.calculator.shared.enums.PaymenyFrequency;
import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  未发国债表
 * Created by xuejian.sun on 2016/8/29.
 */
public class ScheduledBondInfo{

    private String id;
    /**
     * 发行日
     */
    private Date issueStartDate;

    /**
     * 起息日
     */
    private String interestStartDate;

    /**
     * 到期日
     */
    private String maturityDate;

    /**
     * 付息频率
     */
    private PaymenyFrequency couponType;

    /**
     * 债券名字
     */
    private String shortName;

    /**
     * 票息率
     */
    private BigDecimal  couponRate;

    /**
     * 收益率
     */
    private BigDecimal yield;

    /**
     * 未发国债期限, x年期
     */
    private Integer deadLine;

    /**
     * 未发国债所选的哪种收益率
     */
    private YieldTypeEnum yieldType;

    public YieldTypeEnum getYieldType() {
        return yieldType;
    }

    public void setYieldType(YieldTypeEnum yieldType) {
        this.yieldType = yieldType;
    }

    public Integer getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Integer deadLine) {
        this.deadLine = deadLine;
    }


    public Date getIssueStartDate() {
        return issueStartDate;
    }

    public void setIssueStartDate(Date issueStartDate) {
        this.issueStartDate = issueStartDate;
    }

    public String getInterestStartDate() {
        return interestStartDate;
    }

    public void setInterestStartDate(String interestStartDate) {
        this.interestStartDate = interestStartDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public PaymenyFrequency getCouponType() {
        return couponType;
    }

    public void setCouponType(PaymenyFrequency couponType) {
        this.couponType = couponType;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public BigDecimal getCouponRate() {
        return couponRate;
    }

    public void setCouponRate(BigDecimal couponRate) {
        this.couponRate = couponRate;
    }

    public BigDecimal getYield() {
        return yield;
    }

    public void setYield(BigDecimal yield) {
        this.yield = yield;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
