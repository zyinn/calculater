package com.sumscope.optimus.calculator.planalysis.commons.util;

import java.math.BigDecimal;

/**
 * Created by simon.mao on 2016/5/19.
 */
public class NumberUtil {
    public static BigDecimal divide(BigDecimal bigDecimal, int num) {
        return bigDecimal == null ? null : bigDecimal.divide(new BigDecimal(num));
    }

    public static BigDecimal multiply(BigDecimal bigDecimal, int num){
        return bigDecimal == null ? null : bigDecimal.multiply(new BigDecimal(num));
    }

    public static BigDecimal roundHalfUp(BigDecimal bigDecimal, int num){
        return bigDecimal == null ? null : bigDecimal.setScale(num, BigDecimal.ROUND_HALF_UP);
    }

    private NumberUtil() {
    }
}
