package com.sumscope.optimus.calculator.planalysis.model.qdp;

import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;

/**
 * Created by Administrator on 2017/3/9.
 */
public class BondYieldWithKeyValuePair extends KeyValuePair{

    private YieldTypeEnum yieldTypeEnum;

    public YieldTypeEnum getYieldTypeEnum() {
        return yieldTypeEnum;
    }

    public void setYieldTypeEnum(YieldTypeEnum yieldTypeEnum) {
        this.yieldTypeEnum = yieldTypeEnum;
    }
}
