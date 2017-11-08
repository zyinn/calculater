/**
 * QbTfScenarioResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sumscope.optimus.calculator.planalysis.model.qdp.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sumscope.optimus.calculator.planalysis.model.qdp.KeyValuePair;

import java.math.BigDecimal;

public class QbTfScenarioResult implements java.io.Serializable {
    @JsonProperty(value = "CtdCleanPrice")
    private BigDecimal ctdCleanPrice;

    @JsonProperty(value = "CtdDirtyPrice")
    private BigDecimal ctdDirtyPrice;

    @JsonProperty(value = "CtdId")
    private String ctdId;

    @JsonProperty(value = "CtdModifiedDuration")
    private BigDecimal ctdModifiedDuration;

    @JsonProperty(value = "CtdYtm")
    private BigDecimal ctdYtm;

    @JsonProperty(value = "FuturesBasis")
    private BigDecimal futuresBasis;

    @JsonProperty(value = "FuturesId")
    private String futuresId;

    @JsonProperty(value = "FuturesIrr")
    private BigDecimal futuresIrr;

    @JsonProperty(value = "FuturesLot")
    private int futuresLot;

    @JsonProperty(value = "FuturesNetBasis")
    private BigDecimal futuresNetBasis;

    @JsonProperty(value = "ScenarioImpactAmount")
    private KeyValuePair[] scenarioImpactAmount;

    @JsonProperty(value = "ScenarioBasis")
    private KeyValuePair[] scenarioBasis;

    @JsonProperty(value = "ScenarioBondCleanPrice")
    private KeyValuePair[] scenarioBondCleanPrice;

    @JsonProperty(value = "ScenarioEachPnL")
    private KeyValuePair[] scenarioEachPnL;

    @JsonProperty(value = "ScenarioFuturesCleanPrice")
    private KeyValuePair[] scenarioFuturesCleanPrice;

    @JsonProperty(value = "ScenarioIrr")
    private KeyValuePair[] scenarioIrr;

    @JsonProperty(value = "ScenarioNetBasis")
    private KeyValuePair[] scenarioNetBasis;

    @JsonProperty(value = "ScenarioPnL")
    private KeyValuePair[] scenarioPnL;

    @JsonProperty(value = "ScenarioYearYtm")
    private KeyValuePair[] scenarioYearYtm;

    @JsonProperty(value = "TargetBondCleanPrice")
    private BigDecimal targetBondCleanPrice;

    @JsonProperty(value = "TargetBondConversionFactor")
    private BigDecimal targetBondConversionFactor;

    @JsonProperty(value = "TargetBondDirtyPrice")
    private BigDecimal targetBondDirtyPrice;

    @JsonProperty(value = "TargetBondId")
    private String targetBondId;

    @JsonProperty(value = "TargetBondModifiedDuration")
    private BigDecimal targetBondModifiedDuration;

    @JsonProperty(value = "TargetBondYtm")
    private BigDecimal targetBondYtm;

    @JsonProperty(value = "HoldPnL")
    private BigDecimal holdPnl;

    public QbTfScenarioResult() {
    }

    public QbTfScenarioResult(BigDecimal ctdCleanPrice, BigDecimal ctdDirtyPrice, String ctdId, BigDecimal ctdModifiedDuration, BigDecimal ctdYtm, BigDecimal futuresBasis, String futuresId, BigDecimal futuresIrr, int futuresLot, BigDecimal futuresNetBasis, KeyValuePair[] scenarioImpactAmount, KeyValuePair[] scenarioBasis, KeyValuePair[] scenarioBondCleanPrice, KeyValuePair[] scenarioEachPnL, KeyValuePair[] scenarioFuturesCleanPrice, KeyValuePair[] scenarioIrr, KeyValuePair[] scenarioNetBasis, KeyValuePair[] scenarioPnL, KeyValuePair[] scenarioYearYtm, BigDecimal targetBondCleanPrice, BigDecimal targetBondConversionFactor, BigDecimal targetBondDirtyPrice, String targetBondId, BigDecimal targetBondModifiedDuration, BigDecimal targetBondYtm) {
        this.ctdCleanPrice = ctdCleanPrice;
        this.ctdDirtyPrice = ctdDirtyPrice;
        this.ctdId = ctdId;
        this.ctdModifiedDuration = ctdModifiedDuration;
        this.ctdYtm = ctdYtm;
        this.futuresBasis = futuresBasis;
        this.futuresId = futuresId;
        this.futuresIrr = futuresIrr;
        this.futuresLot = futuresLot;
        this.futuresNetBasis = futuresNetBasis;
        this.scenarioImpactAmount = scenarioImpactAmount;
        this.scenarioBasis = scenarioBasis;
        this.scenarioBondCleanPrice = scenarioBondCleanPrice;
        this.scenarioEachPnL = scenarioEachPnL;
        this.scenarioFuturesCleanPrice = scenarioFuturesCleanPrice;
        this.scenarioIrr = scenarioIrr;
        this.scenarioNetBasis = scenarioNetBasis;
        this.scenarioPnL = scenarioPnL;
        this.scenarioYearYtm = scenarioYearYtm;
        this.targetBondCleanPrice = targetBondCleanPrice;
        this.targetBondConversionFactor = targetBondConversionFactor;
        this.targetBondDirtyPrice = targetBondDirtyPrice;
        this.targetBondId = targetBondId;
        this.targetBondModifiedDuration = targetBondModifiedDuration;
        this.targetBondYtm = targetBondYtm;
    }

    public BigDecimal getCtdCleanPrice() {
        return ctdCleanPrice;
    }

    public void setCtdCleanPrice(BigDecimal ctdCleanPrice) {
        this.ctdCleanPrice = ctdCleanPrice;
    }

    public BigDecimal getCtdDirtyPrice() {
        return ctdDirtyPrice;
    }

    public void setCtdDirtyPrice(BigDecimal ctdDirtyPrice) {
        this.ctdDirtyPrice = ctdDirtyPrice;
    }

    public String getCtdId() {
        return ctdId;
    }

    public void setCtdId(String ctdId) {
        this.ctdId = ctdId;
    }

    public BigDecimal getCtdModifiedDuration() {
        return ctdModifiedDuration;
    }

    public void setCtdModifiedDuration(BigDecimal ctdModifiedDuration) {
        this.ctdModifiedDuration = ctdModifiedDuration;
    }

    public BigDecimal getCtdYtm() {
        return ctdYtm;
    }

    public void setCtdYtm(BigDecimal ctdYtm) {
        this.ctdYtm = ctdYtm;
    }

    public BigDecimal getFuturesBasis() {
        return futuresBasis;
    }

    public void setFuturesBasis(BigDecimal futuresBasis) {
        this.futuresBasis = futuresBasis;
    }

    public String getFuturesId() {
        return futuresId;
    }

    public void setFuturesId(String futuresId) {
        this.futuresId = futuresId;
    }

    public BigDecimal getFuturesIrr() {
        return futuresIrr;
    }

    public void setFuturesIrr(BigDecimal futuresIrr) {
        this.futuresIrr = futuresIrr;
    }

    public int getFuturesLot() {
        return futuresLot;
    }

    public void setFuturesLot(int futuresLot) {
        this.futuresLot = futuresLot;
    }

    public BigDecimal getFuturesNetBasis() {
        return futuresNetBasis;
    }

    public void setFuturesNetBasis(BigDecimal futuresNetBasis) {
        this.futuresNetBasis = futuresNetBasis;
    }

    public KeyValuePair[] getScenarioBasis() {
        return scenarioBasis;
    }

    public void setScenarioBasis(KeyValuePair[] scenarioBasis) {
        this.scenarioBasis = scenarioBasis;
    }

    public KeyValuePair[] getScenarioBondCleanPrice() {
        return scenarioBondCleanPrice;
    }

    public void setScenarioBondCleanPrice(KeyValuePair[] scenarioBondCleanPrice) {
        this.scenarioBondCleanPrice = scenarioBondCleanPrice;
    }

    public KeyValuePair[] getScenarioEachPnL() {
        return scenarioEachPnL;
    }

    public void setScenarioEachPnL(KeyValuePair[] scenarioEachPnL) {
        this.scenarioEachPnL = scenarioEachPnL;
    }

    public KeyValuePair[] getScenarioFuturesCleanPrice() {
        return scenarioFuturesCleanPrice;
    }

    public void setScenarioFuturesCleanPrice(KeyValuePair[] scenarioFuturesCleanPrice) {
        this.scenarioFuturesCleanPrice = scenarioFuturesCleanPrice;
    }

    public KeyValuePair[] getScenarioIrr() {
        return scenarioIrr;
    }

    public void setScenarioIrr(KeyValuePair[] scenarioIrr) {
        this.scenarioIrr = scenarioIrr;
    }

    public KeyValuePair[] getScenarioNetBasis() {
        return scenarioNetBasis;
    }

    public void setScenarioNetBasis(KeyValuePair[] scenarioNetBasis) {
        this.scenarioNetBasis = scenarioNetBasis;
    }

    public KeyValuePair[] getScenarioPnL() {
        return scenarioPnL;
    }

    public void setScenarioPnL(KeyValuePair[] scenarioPnL) {
        this.scenarioPnL = scenarioPnL;
    }

    public KeyValuePair[] getScenarioYearYtm() {
        return scenarioYearYtm;
    }

    public void setScenarioYearYtm(KeyValuePair[] scenarioYearYtm) {
        this.scenarioYearYtm = scenarioYearYtm;
    }

    public BigDecimal getTargetBondCleanPrice() {
        return targetBondCleanPrice;
    }

    public void setTargetBondCleanPrice(BigDecimal targetBondCleanPrice) {
        this.targetBondCleanPrice = targetBondCleanPrice;
    }

    public BigDecimal getTargetBondConversionFactor() {
        return targetBondConversionFactor;
    }

    public void setTargetBondConversionFactor(BigDecimal targetBondConversionFactor) {
        this.targetBondConversionFactor = targetBondConversionFactor;
    }

    public BigDecimal getTargetBondDirtyPrice() {
        return targetBondDirtyPrice;
    }

    public void setTargetBondDirtyPrice(BigDecimal targetBondDirtyPrice) {
        this.targetBondDirtyPrice = targetBondDirtyPrice;
    }

    public String getTargetBondId() {
        return targetBondId;
    }

    public void setTargetBondId(String targetBondId) {
        this.targetBondId = targetBondId;
    }

    public BigDecimal getTargetBondModifiedDuration() {
        return targetBondModifiedDuration;
    }

    public void setTargetBondModifiedDuration(BigDecimal targetBondModifiedDuration) {
        this.targetBondModifiedDuration = targetBondModifiedDuration;
    }

    public BigDecimal getTargetBondYtm() {
        return targetBondYtm;
    }

    public void setTargetBondYtm(BigDecimal targetBondYtm) {
        this.targetBondYtm = targetBondYtm;
    }

    public KeyValuePair[] getScenarioImpactAmount() {
        return scenarioImpactAmount;
    }

    public void setScenarioImpactAmount(KeyValuePair[] scenarioImpactAmount) {
        this.scenarioImpactAmount = scenarioImpactAmount;
    }

    public BigDecimal getHoldPnl() {
        return holdPnl;
    }

    public void setHoldPnl(BigDecimal holdPnl) {
        this.holdPnl = holdPnl;
    }
}
