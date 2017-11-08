package com.sumscope.optimus.calculator.tfcalculator.utils;

import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import com.sumscope.optimus.commons.exceptions.ValidationException;
import com.sumscope.optimus.commons.exceptions.ValidationExceptionDetails;
import com.sumscope.optimus.commons.log.LogManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


/**
 * Created by xuejian.sun on 2016/7/27.
 * 用于参数校验异常的工具类
 */
public final class ValidateUtils {

    private  static final String[] SPECIAL_CHARACTERS_SIGN = new String[]{"'","‘","and","=","ltrim","(",")","**","\"","%","select","+","--",",","/or/","/","\"","%","|"};

    public static void validatParams(GeneralValidationErrorType type,Object object,String message){
        ValidationExceptionDetails details = new ValidationExceptionDetails(type,object.toString(),message);
        ValidationException validationException = new ValidationException();
        validationException.addValidationExceptionDetails(details);
        throw validationException;
    }

    public static void setResponseHeader(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
    }
    //跨域OPTIONS请求
    public static void getMethodOptions(HttpServletRequest request, HttpServletResponse response){
        try {
            response.addHeader("Access-Control-Allow-Methods", "OPTIONS");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.flush();
            return;
        } catch (IOException e) {
            LogManager.error("写入response出现错误。"+e.toString());
        }
    }

    private ValidateUtils() {
    }

    public static String validate(String params){
        if(params!=null){
            for(int i=0;i<SPECIAL_CHARACTERS_SIGN.length;i++){
                if(params.contains(SPECIAL_CHARACTERS_SIGN[i])){
                    params= params.replace(SPECIAL_CHARACTERS_SIGN[i],"");
                }
            }
        }
        return params;
    }

}
