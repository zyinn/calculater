package com.sumscope.optimus.calculator.planalysis.model.qdp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by simon.mao on 2016/5/10.
 */
public class KeyValuePair {
    @JsonProperty(value = "Key")
    public String key;

    @JsonProperty(value = "Value")
    public BigDecimal value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
