package com.sumscope.optimus.calculator.planalysis.model.qdp;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by simon.mao on 2016/5/10.
 */
public class ValuationParamter {
    @JsonProperty(value = "DiscountCurveName")
    private String discountCurveName = "IrrCurve";

    @JsonProperty(value = "FixingCurveName")
    private String fixingCurveName = "";

    @JsonProperty(value = "RiskfreeCurveName")
    private String riskfreeCurveName = "FundingCurve";

    public String getDiscountCurveName() {
        return discountCurveName;
    }

    public void setDiscountCurveName(String discountCurveName) {
        this.discountCurveName = discountCurveName;
    }

    public String getFixingCurveName() {
        return fixingCurveName;
    }

    public void setFixingCurveName(String fixingCurveName) {
        this.fixingCurveName = fixingCurveName;
    }

    public String getRiskfreeCurveName() {
        return riskfreeCurveName;
    }

    public void setRiskfreeCurveName(String riskfreeCurveName) {
        this.riskfreeCurveName = riskfreeCurveName;
    }
}
