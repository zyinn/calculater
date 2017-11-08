package com.sumscope.optimus.calculator.planalysis.commons.util;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by simon.mao on 2016/5/20.
 */
public class SortUtil {
    public static Map<String, ?> sortByNumberKey(Map<String, ?> oriMap){
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Object> sortedMap = new TreeMap<String, Object>(new Comparator<String>() {
            public int compare(String key1, String key2) {
                BigDecimal intKey1 = getBigDecimal(key1);
                BigDecimal intKey2 = getBigDecimal(key2);

                BigDecimal result = intKey1.subtract(intKey2).setScale(0, BigDecimal.ROUND_CEILING);

                return result.intValue();
            }
        });
        sortedMap.putAll(oriMap);
        return sortedMap;
    }

    private static BigDecimal getBigDecimal(String str) {
        try {
            Pattern p = Pattern.compile("^[0-9]+(.[0-9]*)?$");
            Matcher m = p.matcher(str);
            if (m.find()) {
                return new BigDecimal(m.group());
            }else {
                return new BigDecimal(0);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new BigDecimal(0);
        }
    }

    private SortUtil() {
    }
}
