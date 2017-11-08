package com.sumscope.optimus.tfcalculator.planalysis.gateway;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumscope.optimus.calculator.planalysis.gateway.RepoRateGatewayService;
import com.sumscope.optimus.calculator.planalysis.model.dto.RepoRateDto;
import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import com.sumscope.optimus.tfcalculator.planalysis.gateway.model.HolidayInfo;
import com.sumscope.optimus.tfcalculator.planalysis.gateway.model.ResultTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fan.bai on 2016/5/5.
 * RepoRateGatewayService 实现
 */
@Service
public class RepoRateGatewayServiceImpl implements RepoRateGatewayService {
    private final static int TIMEOUT = 60*60;

    @Value("${external.username}")
    private String username;
    @Value("${external.password}")
    private String password;

    @Autowired
    private ShiborManagerInvoke shiborManagerInvoke;

    public List<ResultTable> sendCDHReceiveQbQmMapper() {
        List<ResultTable> qBQmList=new ArrayList<>();
        Boolean sign=true;
        int startPage=0;
        while (sign){
            startPage= startPage+1;
            List<ResultTable> shiborInvoke = shiborManagerInvoke.getShiborInvoke(ShiborJson(startPage,false,null));
            if(shiborInvoke!=null && shiborInvoke.size()>0){
                qBQmList.addAll(shiborInvoke);
            }
            sign= shiborInvoke==null ? false : shiborInvoke.size()<5000 ? false : true;
        }
        return qBQmList;
    }
    @Override
    @CacheMe(timeout = TIMEOUT)
    public List<RepoRateDto> retrieveRepoRatesWithPrice() {
        List<RepoRateDto> results = new ArrayList<>();
        List<ResultTable> resultTables = sendCDHReceiveQbQmMapper();
        for(ResultTable resultTable: resultTables){
            RepoRateDto repoRateDto=new RepoRateDto();
            if(resultTable.getIndexValue()!=null){
                repoRateDto.setPrice(resultTable.getIndexValue());
            }else{
                repoRateDto.setPrice(getCodeValue(resultTable.getSourcecode()));
            }
            repoRateDto.setCode(resultTable.getSourcecode());
            results.add(repoRateDto);
        }
        return results;
    }

    @Override
    public List<HolidayInfo> retrieveHolidayInfo() {
        List<HolidayInfo> qBQmList=new ArrayList<>();
        Boolean sign=true;
        int startPage=0;
        while (sign){
            startPage= startPage+1;
            List<HolidayInfo> shiborInvoke = shiborManagerInvoke.getHolidayInfo(holidayJson(startPage));
            if(shiborInvoke!=null && shiborInvoke.size()>0){
                qBQmList.addAll(shiborInvoke);
            }
            sign= shiborInvoke==null ? false : shiborInvoke.size()<5000 ? false : true;
        }
        return qBQmList;
    }


    public String holidayJson(int startPage){
        JSONObject obj = new JSONObject();
        obj.put("User",username);
        obj.put("Password",password);
        obj.put("ApiName","HOLIDAY_INFO");
        obj.put("ApiVersion","N");
        obj.put("DataSourceId",100);
        obj.put("StartDate",getStringDate());
        obj.put("EndDate",getStringDateShort(new Date()));
        obj.put("Columns",converterToInvokeColumns());
        obj.put("Codes",converterToInvokeCodes());
        obj.put("Conditions","ORDER BY HOLIDAY_DATE DESC");
        obj.put("PageSize",5000);
        obj.put("StartPage",startPage);
        StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonText = out.toString();
        return jsonText;
    }
    private JSONArray converterToInvokeCodes(){
        JSONArray array = new JSONArray();
        array.add("CIB");
        return array;
    }
    public JSONArray converterToInvokeColumns(){
        JSONArray array = new JSONArray();
        array.add("holiday_reason");
        array.add("holiday_date");
        array.add("market_type");
        array.add("country");
        return array;
    }

    public JSONArray converterToShiborInvokeCodes(){
        JSONArray array = new JSONArray();
        array.add("SHIBOR_O/N");
        array.add("SHIBOR_3M");
        array.add("FR001");
        array.add("FR007");
        array.add("FR014");
        array.add("GC001");
        array.add("GC002");
        array.add("GC003");
        array.add("GC004");
        array.add("GC007");
        array.add("GC014");
        return array;
    }
    public String ShiborJson(int startPage,boolean lastWork,String code){
        JSONObject obj = new JSONObject();
        obj.put("User",username);
        obj.put("Password",password);
        obj.put("ApiName","V_Interest_Rate");
        obj.put("ApiVersion","N");
        obj.put("DataSourceId",100);
        obj.put("Columns",converterToShiborInvokeColumns());
        if(lastWork){
            obj.put("Codes",converterInvokeCodes(code));
        }else{
            obj.put("Codes",converterToShiborInvokeCodes());
        }

        obj.put("Conditions","ORDER BY Source_code, Index_Date DESC");
        obj.put("PageSize",5000);
        obj.put("StartPage",startPage);
        StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonText = out.toString();
        return jsonText;
    }
    public JSONArray converterToShiborInvokeColumns(){
        JSONArray array = new JSONArray();
        array.add("Index_Date");
        array.add("Index_Value");
        array.add("unit_type_local");
        array.add("series_name_local");
        array.add("source_code");
        return array;
    }
    
    private BigDecimal getCodeValue(String code){
            for(int i=1;i<20;i++){
                List<ResultTable> qBQmList=new ArrayList<>();
                Boolean sign=true;
                int startPage=0;
                while (sign){
                    startPage= startPage+1;
                    String s = converterShiborJson(startPage, converterDate(i), code);
                    List<ResultTable> shiborInvoke = shiborManagerInvoke.getShiborInvoke(s);
                    if(shiborInvoke!=null && shiborInvoke.size()>0){
                        qBQmList.addAll(shiborInvoke);
                    }
                    sign= shiborInvoke==null ? false : shiborInvoke.size()<5000 ? false : true;
                }
                if(qBQmList!=null && qBQmList.size()>0){
                    if(qBQmList.get(0).getIndexValue()!=null){
                        return qBQmList.get(0).getIndexValue();
                    }
                }
            }
        return null;
    }

    private String converterDate(int i){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-i);
        return formatter.format(calendar.getTime());
    }

    private String converterShiborJson(int startPage,String date,String code){
        JSONObject obj = new JSONObject();
        obj.put("User",username);
        obj.put("Password",password);
        obj.put("ApiName","V_History_Interest_Rate");
        obj.put("ApiVersion","N");
        obj.put("DataSourceId",100);
        obj.put("StartDate",date);
        obj.put("EndDate",date);
        obj.put("Columns",converterToShiborInvokeColumns());
        obj.put("Codes",converterInvokeCodes(code));
        obj.put("Conditions","ORDER BY Source_code, Index_Date DESC");
        obj.put("PageSize",5000);
        obj.put("StartPage",startPage);
        StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonText = out.toString();
        return jsonText;
    }
    public JSONArray converterInvokeCodes(String code){
        JSONArray array = new JSONArray();
        array.add(code);
        return array;
    }

    public static String getStringDateShort(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(currentTime);
    }

    public static String getStringDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-30);
        return formatter.format(calendar.getTime());
    }

}
