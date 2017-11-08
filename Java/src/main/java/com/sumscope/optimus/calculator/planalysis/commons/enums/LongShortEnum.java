package com.sumscope.optimus.calculator.planalysis.commons.enums;

/**
 * Created by simon.mao on 2016/4/28.
 * 方向枚举
 */
public enum LongShortEnum {
    LONG_BASIS("LONG_BASIS", "做多基差","bull"),
    SHORT_BASIS("SHORT_BASIS", "做空基差","bear");

    private final String key;
    private final String name;
    private final String calculatorEnumName;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getCalculatorEnumName() {
        return calculatorEnumName;
    }

    LongShortEnum(String key, String name, String enumName) {
        this.key = key;
        this.name = name;
        this.calculatorEnumName = enumName;
    }
}
