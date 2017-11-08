import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by simon.mao on 2016/4/29.
 */
public class TestRedis {
    @Bean
    public CacheManager cacheManager(@SuppressWarnings("rawtypes")RedisTemplate redisTemplate){
        return new RedisCacheManager(redisTemplate);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
        final StringRedisTemplate template = new StringRedisTemplate(factory);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        return template;
    }

    /*public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date str1 = simpleDateFormat2.parse("2016-02-02");
            Date str2 = simpleDateFormat2.parse("2016-03-02");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(str1);
            int day1 = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(str2);
            int day2 = calendar.get(Calendar.DAY_OF_YEAR);

            System.out.println(day2-day1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

}
