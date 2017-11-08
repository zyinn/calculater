package com.sumscope.optimus.calculator.planalysis.gateway;

import com.sumscope.optimus.commons.log.LogManager;
import com.sumscope.optimus.tfcalculator.planalysis.commons.util.JosnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_FLOOR;

/**
 * Created by simon.mao on 2016/4/29.
 */
@Service
public class RedisGatewayServiceImpl implements RedisGatewayService {

    @Autowired
    private RedisUtil redisUtil;

    @Value("${redis.spring_redis_host}")
    private String host;
    @Value("${redis.spring_redis_port}")
    private int port;

    public static final String BASIS_REPO_RATE_LAST_PRICE_PREFIX = "CFFEX:LastPrice:";

    @Override
    public BigDecimal getFutureLastPrice(String tfId) {
        long l = System.currentTimeMillis();
        String key = BASIS_REPO_RATE_LAST_PRICE_PREFIX + tfId;
        String redis = redisUtil.redis(key, host, port);
        LogManager.info("redis response is :"+redis+",host:"+host+",port:"+port);
        System.out.println("redis time is----------"+(System.currentTimeMillis()-l) +"ms");
        String lastPrice = redis!=null ? JosnNode.getJsonNode(redis).path("LastPrice").asText() : null;
        return lastPrice!=null ? new BigDecimal(lastPrice).setScale(3,BigDecimal.ROUND_HALF_UP) : null;

    }

    //mock futureLastPrice value of redis back
    private String mockFutureLastPrice(){
        double x=(double)(Math.random()*10+99);
        String s = String.valueOf(x);
        String value=s.substring(0,s.indexOf(".")+4);
        return value;
    }

}
