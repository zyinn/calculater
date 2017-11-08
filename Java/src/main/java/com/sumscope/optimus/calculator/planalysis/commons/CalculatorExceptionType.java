package com.sumscope.optimus.calculator.planalysis.commons;

import com.sumscope.optimus.commons.exceptions.ExceptionType;

/**
 * Created by fan.bai on 2016/6/17.
 */
public enum CalculatorExceptionType implements ExceptionType {
    INPUT_PARAM_INVALID("E4101","QDP计算失败！");

    CalculatorExceptionType(String code,String info) {
        this.code = code;
        this.errorInfoCN = info;
    }

    private String code;
    private String errorInfoCN;

    @Override
    public String getExceptionCode() {
        return code;
    }

    @Override
    public String getExceptionInfoCN() {
        return errorInfoCN;
    }
}
