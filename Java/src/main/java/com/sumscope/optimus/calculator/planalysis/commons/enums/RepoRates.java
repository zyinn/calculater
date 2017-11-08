package com.sumscope.optimus.calculator.planalysis.commons.enums;

/**
 * Created by fan.bai on 2016/5/6.
 * 资金成本枚举
 */
public enum RepoRates {
    /**
     * 银行间回购定盘利率:1天
     */
    FR001("FR001"),
    /**
     * 银行间回购定盘利率:7天
     */
    FR007("FR007"),

    /**
     * 银行间回购定盘利率:14天
     */
    FR014("FR014"),
    /**
     * 质押式回购:1天
     */
    GC001("GC001"),
    /**
     * 质押式回购:2天
     */
    GC002("GC002"),
    /**
     * 质押式回购:3天
     */
    GC003("GC003"),
    /**
     * 质押式回购:4天
     */
    GC004("GC004"),
    /**
     * 质押式回购:7天
     */
    GC007("GC007"),
    /**
     * 质押式回购:14天
     */
    GC014("GC014"),
    /**
     * 同业拆放利率:隔夜
     */
    SHIBOR_ON("Shibor_O/N"),
    /**
     * 同业拆放利率:3个月
     */
    SHIBOR_3M("Shibor_3M");

    /**
     * 当通过URL进行当前价格读取的时候，url中该资金成本对应的字符串，例如：
     * SHIBOR_ON对应：
     * http://172.18.3.20:8006/info/interbank/Shibor_ON/rate
     */
    private final String url;

    RepoRates(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
