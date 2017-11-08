package com.sumscope.optimus.tfcalculator.planalysis.gateway;

import com.sumscope.optimus.calculator.planalysis.gateway.RestInvocationUtil;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.exceptions.ExceptionType;
import com.sumscope.optimus.commons.log.LogManager;
import com.sumscope.optimus.commons.util.JsonUtil;
import com.sumscope.optimus.tfcalculator.planalysis.gateway.model.HolidayInfo;
import com.sumscope.optimus.tfcalculator.planalysis.gateway.model.ResultTable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据runapi接口 用Http 获取资金成本
 *
 */
@Component
public class ShiborManagerInvoke {

    @Autowired
    private RestInvocationUtil service;

    @Value("${external.shiborUrl}")
    private String urlTemplate;

    public List<ResultTable> getShiborInvoke(String param) {
        List<ResultTable> resultMap = new ArrayList<>();
        String url = urlTemplate;
        String response=null;
        try {
            response = service.post(url, param);
            if (response != null) {
                JSONObject dataJson = new JSONObject(response);
                JSONArray array=dataJson.getJSONArray("resultTable");
                if(array!=null && array.length()>0){
                    for(int i=0; i<array.length();i++){
                        String resultTables =array.get(i)!=null? array.get(i).toString() : null;
                        resultMap.add(JsonUtil.readValue(resultTables, ResultTable.class));
                    }
                }
            }
        } catch (Exception e) {
            ExceptionType et = BusinessRuntimeExceptionType.OTHER;
            LogManager.error(new BusinessRuntimeException(et, e)+",response:"+response);
        }
        return resultMap;
    }

    public List<HolidayInfo> getHolidayInfo(String param){
        List<HolidayInfo> resultMap = new ArrayList<>();
        String url = urlTemplate;
        String response=null;
        try {
            response = service.post(url, param);
            if (response != null) {
                JSONObject dataJson = new JSONObject(response);
                JSONArray array=dataJson.getJSONArray("resultTable");
                if(array!=null && array.length()>0){
                    for(int i=0; i<array.length();i++){
                        String holidayInfo =array.get(i)!=null? array.get(i).toString() : null;
                        resultMap.add(JsonUtil.readValue(holidayInfo, HolidayInfo.class));
                    }
                }
            }
        } catch (Exception e) {
            ExceptionType et = BusinessRuntimeExceptionType.OTHER;
            LogManager.error(new BusinessRuntimeException(et, e)+",response:"+response);
        }
        return resultMap;
    }

}