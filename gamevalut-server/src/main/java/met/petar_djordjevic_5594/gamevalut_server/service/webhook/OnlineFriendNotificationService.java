package met.petar_djordjevic_5594.gamevalut_server.service.webhook;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.webhook.OnlineFriendWebhookDTO;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import met.petar_djordjevic_5594.gamevalut_server.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnlineFriendNotificationService {

    @Autowired
    CustomUserService userService;
    @Autowired
    RedisService redisService;

    public OnlineFriendNotificationService() {
    }

    public void subscribe(OnlineFriendWebhookDTO subscriber){
        CustomUser user = userService.getUserById(subscriber.id());

        redisService.saveToRedis(user.getId().toString(), "nesto");

    }
}
