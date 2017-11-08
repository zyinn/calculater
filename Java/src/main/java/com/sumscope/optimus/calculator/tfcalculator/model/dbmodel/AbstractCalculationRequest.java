package com.sumscope.optimus.calculator.tfcalculator.model.dbmodel;

import com.sumscope.optimus.calculator.shared.enums.BondPriceType;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.FutureContract;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.TargetBonds;

import java.math.BigDecimal;

/**
 * 本地系统用于QDP计算的请求数据结构。该数据类型与QDP计算要求的请求类型稍有不同，主要区别在于本数据包含了期货和债卷的详细信息。需要通过QDP接口层的转换器转换为用于QDP计算的请求数据。
 * 该类型应包括了所有用于计算的参数。其子类的数据仅用于响应Dto的组装，而不用于计算。
 */
public abstract class AbstractCalculationRequest {

    /**
     * 期货信息
     */
    private FutureContract futureContract;

    /**
     * 债卷信息
     */
    private BondInfo bond;

    /**
     * 债卷价格信息，可能为收益率或者全价或者净价，有bondPriceType表明。可能由Web端dto给出，也可能从数据库取出
     */
    private BigDecimal bondPrice;

    /**
     * 债卷收益率?全价？净价？
     */
    private BondPriceType bondPriceType;

    /**
     * irr
     */
    private BigDecimal irr;

    /**
     * 基差,由Web端dto给出
     */
    private BigDecimal basis;

    /**
     * 净基差,由Web端dto给出
     */
    private BigDecimal netBasis;

    /**
     * 期货价格，可能由Web端dto给出，也可能由数据库取出
     */
    private BigDecimal futurePrice;

    /**
     * 资金成本，由Web端dto给出
     */
    private BigDecimal capitalCost;

    /**
     * 计算目标
     */
    private CalculationTarget calculationTarget;

    /**
     * 标记是 calculationRequestForInit CalculationRequest CalculationRequestWithBondYieldType对象
     */
    private String distinguish;

    /**
     * 在哪个tab下
     */
    private TargetBonds targetBonds;

    public FutureContract getFutureContract() {
        return futureContract;
    }

    public void setFutureContract(FutureContract futureContract) {
        this.futureContract = futureContract;
    }

    public BondInfo getBond() {
        return bond;
    }

    public void setBond(BondInfo bond) {
        this.bond = bond;
    }

    public BigDecimal getBondPrice() {
        return bondPrice;
    }

    public void setBondPrice(BigDecimal bondPrice) {
        this.bondPrice = bondPrice;
    }

    public BondPriceType getBondPriceType() {
        return bondPriceType;
    }

    public void setBondPriceType(BondPriceType bondPriceType) {
        this.bondPriceType = bondPriceType;
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

    public BigDecimal getFuturePrice() {
        return futurePrice;
    }

    public void setFuturePrice(BigDecimal futurePrice) {
        this.futurePrice = futurePrice;
    }

    public BigDecimal getCapitalCost() {
        return capitalCost;
    }

    public void setCapitalCost(BigDecimal capitalCost) {
        this.capitalCost = capitalCost;
    }

    public CalculationTarget getCalculationTarget() {
        return calculationTarget;
    }

    public void setCalculationTarget(CalculationTarget calculationTarget) {
        this.calculationTarget = calculationTarget;
    }

    public String getDistinguish() {
        return distinguish;
    }

    public void setDistinguish(String distinguish) {
        this.distinguish = distinguish;
    }

    public TargetBonds getTargetBonds() {
        return targetBonds;
    }

    public void setTargetBonds(TargetBonds targetBonds) {
        this.targetBonds = targetBonds;
    }
}
