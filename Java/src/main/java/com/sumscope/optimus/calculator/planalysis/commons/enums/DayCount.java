package com.sumscope.optimus.calculator.planalysis.commons.enums;

/**
 * Created by fan.bai on 2016/5/4.
 * 计算日期枚举，使用者：bond.Interest_Basis
 */
public enum DayCount {
    BA0("360日","Act360"),
    BAA("交易所","InterBankBond"),
    BA5("当前年","ActAct"),
    BAF("365日","Act365"),
    B30("360日","B30360");

    private final String expalin;
    private final String calculatorEnumName;

    public String getExpalin() {
        return expalin;
    }

    public String getCalculatorEnumName() {
        return calculatorEnumName;
    }

    DayCount(String explain, String name){
        this.expalin = explain;
        this.calculatorEnumName = name;
    }
}
