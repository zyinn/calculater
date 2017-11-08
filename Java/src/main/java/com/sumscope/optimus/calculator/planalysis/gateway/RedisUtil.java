package com.sumscope.optimus.calculator.planalysis.gateway;

import com.sumscope.optimus.commons.log.LogManager;
import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by simon.mao on 2016/4/29.
 */
@Service
public class RedisUtil {

    private JedisCluster jc;

    public String redis(String key,String host,int port){
        String value=null;
        try {
            Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
            jedisClusterNodes.add(new HostAndPort(host, port));
            this.jc = new JedisCluster(jedisClusterNodes);
            value= this.jc.get(key);
        } catch (Exception e) {
            LogManager.error("redis is error :"+e);
        } finally {
            if(this.jc!=null){
                this.jc.close();
            }
        }
        return value;
    }
}
