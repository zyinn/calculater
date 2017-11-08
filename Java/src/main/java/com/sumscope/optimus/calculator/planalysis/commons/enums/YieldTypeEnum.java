package com.sumscope.optimus.calculator.planalysis.commons.enums;

/**
 * Created by simon.mao on 2016/4/27.
 */
public enum YieldTypeEnum {
    ofr("ofr", "Ofr"),
    bid("bid", "Bid"),
    deal("deal", "成交"),
    cdc("cdc", "中债");

    private final String key;
    private final String name;

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    YieldTypeEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
