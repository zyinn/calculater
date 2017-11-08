package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.shared.model.dto.BondConvertionFactorDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用于转换QDP接口返回数据的中间层对象
 * Created by Administrator on 2016/9/1.
 */
public class Result {

    private BigDecimal irr; //irr值
    private BigDecimal basis; //基差
    private BigDecimal netBasis; //净基差
    private BigDecimal bondCleanPrice; //净价
    private BigDecimal bondDirtyPrice; //全价
    private List<BondConvertionFactorDto> conversionFactors; //转换因子
    private BigDecimal timeWeightedCoupon; //加权平均期间付息
    private BigDecimal bondYtm ;//债券收益率
    private BigDecimal invoicePrice; //发票价格
    private BigDecimal aiEnd; //交割日应计利息
    private BigDecimal aiStart;//交易日应计利息
    private BigDecimal spread; //利差
    private BigDecimal pnL; //持有期损益
    private BigDecimal coupon; //期间付息
    private BigDecimal margin; //期限价差
    private BigDecimal futurePrice;//期货价格
    private BigDecimal maturityMoveDayYtm;//收益率

    public BigDecimal getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(BigDecimal futurePrice) {
        this.futurePrice = futurePrice;
    }

    public BigDecimal getAiStart() {
        return aiStart;
    }

    public void setAiStart(BigDecimal aiStart) {
        this.aiStart = aiStart;
    }

    public BigDecimal getAiEnd() {
        return aiEnd;
    }

    public void setAiEnd(BigDecimal aiEnd) {
        this.aiEnd = aiEnd;
    }

    public BigDecimal getSpread() {
        return spread;
    }

    public void setSpread(BigDecimal spread) {
        this.spread = spread;
    }

    public BigDecimal getPnL() {
        return pnL;
    }

    public void setPnL(BigDecimal pnL) {
        this.pnL = pnL;
    }

    public BigDecimal getCoupon() {
        return coupon;
    }

    public void setCoupon(BigDecimal coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getIrr() {
        return irr;
    }

    public void setIrr(BigDecimal irr) {
        this.irr = irr;
    }

    public BigDecimal getBasis() {
        return basis;
    }

    public void setBasis(BigDecimal basis) {
        this.basis = basis;
    }

    public BigDecimal getNetBasis() {
        return netBasis;
    }

    public void setNetBasis(BigDecimal netBasis) {
        this.netBasis = netBasis;
    }

    public BigDecimal getBondCleanPrice() {
        return bondCleanPrice;
    }

    public void setBondCleanPrice(BigDecimal bondCleanPrice) {
        this.bondCleanPrice = bondCleanPrice;
    }

    public BigDecimal getBondDirtyPrice() {
        return bondDirtyPrice;
    }

    public void setBondDirtyPrice(BigDecimal bondDirtyPrice) {
        this.bondDirtyPrice = bondDirtyPrice;
    }

    public List<BondConvertionFactorDto> getConversionFactors() {
        return conversionFactors;
    }

    public void setConversionFactors(List<BondConvertionFactorDto> conversionFactors) {
        this.conversionFactors = conversionFactors;
    }

    public BigDecimal getTimeWeightedCoupon() {
        return timeWeightedCoupon;
    }

    public void setTimeWeightedCoupon(BigDecimal timeWeightedCoupon) {
        this.timeWeightedCoupon = timeWeightedCoupon;
    }

    public BigDecimal getBondYtm() {
        return bondYtm;
    }

    public void setBondYtm(BigDecimal bondYtm) {
        this.bondYtm = bondYtm;
    }

    public BigDecimal getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(BigDecimal invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public BigDecimal getMaturityMoveDayYtm() {
        return maturityMoveDayYtm;
    }

    public void setMaturityMoveDayYtm(BigDecimal maturityMoveDayYtm) {
        this.maturityMoveDayYtm = maturityMoveDayYtm;
    }
}
