package com.sumscope.optimus.calculator.planalysis.model.dto;

import java.math.BigDecimal;

/**
 * Created by simon.mao on 2016/4/29.
 * 资金成本Dto
 */
public class RepoRateDto {
    /**
     * 资金成本code
     */
    private String code;
    /**
     * 当前资金成本price
     */
    private BigDecimal price;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
