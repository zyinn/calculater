package com.sumscope.optimus.calculator.planalysis.commons.util;

import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by simon.mao on 2016/5/11.
 * 计算两日期差距
 */
public class DateDifferentUtil {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat numericalDateFormat = new SimpleDateFormat("yyyyMMdd");

    public static String parseDateToString(Date date){
        return simpleDateFormat.format(date);
    }

    public static int daysBetween(String day1String, String day2String){
        try {
            Date date1 = simpleDateFormat2.parse(day1String);
            Date date2 = simpleDateFormat2.parse(day2String);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);
            int betweenYears = calendar2.get(Calendar.YEAR)-calendar1.get(Calendar.YEAR);
            int betweenDays = calendar2.get(Calendar.DAY_OF_YEAR)-calendar1.get(Calendar.DAY_OF_YEAR);
            for(int i=0;i<betweenYears;i++){
                calendar1.set(Calendar.YEAR,(calendar1.get(Calendar.YEAR)+1));
                betweenDays += calendar1.getMaximum(Calendar.DAY_OF_YEAR);
            }
            return betweenDays;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Date parseNumericalDateString(String dateString){
        try {
            return numericalDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER,"Invalid use of method, date string is not valid! "+ dateString+e);
        }
    }

    public static Date parseSimpleDateWithoutTimeString(String dateString){
        try {
            return simpleDateFormat2.parse(dateString);
        } catch (ParseException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER,"Invalid use of method, date string is not valid! "+ dateString+e);
        }
    }
    //工具类不应该有一个公共或者默认的构造函数
    private DateDifferentUtil(){}
}
