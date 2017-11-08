package com.sumscope.optimus.calculator.planalysis.gateway;

import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.log.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fan.bai on 2016/5/6.
 */
@Component
public class RestInvocationUtil {

    @Value("${external.httpReadTimeout}")
    private static  int readTimeOut;

    public static String post(String uri, String request) {
        try {
            HttpURLConnection connection = getConnection(uri);
            connection.setReadTimeout(readTimeOut);
            connection.connect();
            BufferedOutputStream outputStream = new BufferedOutputStream(new DataOutputStream(connection.getOutputStream()));
            outputStream.write(request.getBytes());
            outputStream.flush();
            outputStream.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            connection.disconnect();
            return sb.toString();
        } catch (Exception e) {
            LogManager.error(uri+","+request+";"+e);
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,"接口失败,请稍后再试或联系管理员进行修改。异常信息："+e.getMessage()+"send:"+request);
        }
    }

    protected static HttpURLConnection getConnection(String uri) throws IOException {
        HttpURLConnection connection;
        URL url = new URL(uri);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }

    private RestInvocationUtil() {
    }
}


