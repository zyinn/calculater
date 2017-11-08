package com.sumscope.optimus.calculator.planalysis.commons.enums;

/**
 * Created by fan.bai on 2016/5/4.
 * 付息频率枚举，bond.Coupon_Frequency
 */
public enum PaymenyFrequency {
    N("",""),
    S("半年","SemiAnnual"),
    A("一年","Annual"),
    Q("一季度","Quarterly"),
    M("一个月","Monthly");

    private final String explain;
    private final String calculatorEnumName;

    public String getExplain() {
        return explain;
    }

    public String getCalculatorEnumName() {
        return calculatorEnumName;
    }

    PaymenyFrequency(String explain, String enumName){
        this.explain = explain;
        this.calculatorEnumName = enumName;
    }

}
