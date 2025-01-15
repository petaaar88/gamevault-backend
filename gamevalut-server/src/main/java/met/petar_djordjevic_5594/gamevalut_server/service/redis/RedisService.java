package met.petar_djordjevic_5594.gamevalut_server.service.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveToRedis(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}