package met.petar_djordjevic_5594.gamevalut_server.service.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveToRedis(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object getFromRedis(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public boolean checkIfHashExist(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void deleteFromRedis(String key){
        redisTemplate.delete(key);
    }

    public void deleteHashFromRedis(String key, String hash){
        redisTemplate.opsForHash().delete(key,hash);
    }

    public boolean checkIfHashExist(String key, String hash){
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, hash));
    }


}