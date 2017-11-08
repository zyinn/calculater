package com.sumscope.optimus.calculator.planalysis.gateway;

import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.log.LogManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * Created by simon.mao on 2016/5/10.
 */
@Service
public class RestServiceImpl implements RestService {
    @Value("${calculator.url}")
    public String url;

    @Override
    @CacheMe(timeout = 3600)
    public String sendData(String data) {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = null;
        String resData;
        try {
            StringEntity entity = new StringEntity(data);
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            Date calcStart = new Date(System.currentTimeMillis());
            response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200){
                resData = EntityUtils.toString(response.getEntity(), "UTF-8");
            }else{
                LogManager.error(response.toString());
                throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER, "计算失败！"+response);
            }
            Date calcEnd = new Date(System.currentTimeMillis());
            httpClient.close();
            long diff = calcEnd.getTime() - calcStart.getTime();
            System.out.println("Take: " + diff + "ms" );
        } catch (Exception e) {
            LogManager.error("sendData"+e);
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER, "计算失败！\t\n"+response+"\t\n url:"+url+"\t\n send:"+data);
        }
        return resData;
    }

}
