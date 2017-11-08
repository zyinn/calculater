package com.sumscope.optimus.tfcalculator.planalysis.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/11/18.
 */
public class ResultTable {
    @JsonProperty("Index_Date")
    private String indexDate;
    @JsonProperty("Index_Value")
    private BigDecimal indexValue;
    @JsonProperty("unit_type_local")
    private String unittypelocal;
    @JsonProperty("series_name_local")
    private String seriesnamelocal;
    @JsonProperty("source_code")
    private String sourcecode;

    public void setIndexDate(String indexDate) {
        this.indexDate = indexDate;
    }

    public String getIndexDate() {
        return indexDate;
    }

    public void setIndexValue(BigDecimal indexValue) {
        this.indexValue = indexValue;
    }

    public BigDecimal getIndexValue() {
        return indexValue;
    }

    public String getUnittypelocal() {
        return unittypelocal;
    }

    public void setUnittypelocal(String unittypelocal) {
        this.unittypelocal = unittypelocal;
    }

    public String getSeriesnamelocal() {
        return seriesnamelocal;
    }

    public void setSeriesnamelocal(String seriesnamelocal) {
        this.seriesnamelocal = seriesnamelocal;
    }

    public String getSourcecode() {
        return sourcecode;
    }

    public void setSourcecode(String sourcecode) {
        this.sourcecode = sourcecode;
    }
}
