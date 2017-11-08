package com.sumscope.optimus.calculator.planalysis.model.dto;

import com.sumscope.optimus.calculator.planalysis.commons.enums.LongShortEnum;
import com.sumscope.optimus.calculator.planalysis.commons.enums.YieldTypeEnum;

import java.math.BigDecimal;

/**
 * Created by simon.mao on 2016/4/28.
 * 盈亏分析计算参数Dto
 */
public class RequestDto extends CommonDto {
    private String openPositionDate;

    private String closePositionDate;
    /**
     * 资金成本，根据列表及BP从前端计算好传入
     */
    private BigDecimal capitalCost;
    /**
     * 现卷名义本金
     */
    private BigDecimal bondNominalPrincipal = BigDecimal.valueOf(10000000);
    /**
     * 冲击成本
     */
    private BigDecimal impactCost = BigDecimal.valueOf(0);
    /**
     * 获取何种最新价格：ofr?bid?成交？中债？
     */
    private YieldTypeEnum yieldType = YieldTypeEnum.ofr;
    /**
     * 做多 or 做空
     */
    private LongShortEnum longShortSymbol = LongShortEnum.SHORT_BASIS;

    private BigDecimal curveYtm = null;

    private BigDecimal curveFuturesPrice = null;

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

    public BigDecimal getCapitalCost() {
        return capitalCost;
    }

    public void setCapitalCost(BigDecimal capitalCost) {
        this.capitalCost = capitalCost;
    }

    public BigDecimal getBondNominalPrincipal() {
        return bondNominalPrincipal;
    }

    public void setBondNominalPrincipal(BigDecimal bondNominalPrincipal) {
        this.bondNominalPrincipal = bondNominalPrincipal;
    }

    public BigDecimal getImpactCost() {
        return impactCost;
    }

    public void setImpactCost(BigDecimal impactCost) {
        this.impactCost = impactCost;
    }

    public YieldTypeEnum getYieldType() {
        return yieldType;
    }

    public void setYieldType(YieldTypeEnum yieldType) {
        this.yieldType = yieldType;
    }

    public LongShortEnum getLongShortSymbol() {
        return longShortSymbol;
    }

    public void setLongShortSymbol(LongShortEnum longShortSymbol) {
        this.longShortSymbol = longShortSymbol;
    }
}
