package met.petar_djordjevic_5594.gamevalut_server.service.webhook;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendDTO;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.LoginUserDTO;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import met.petar_djordjevic_5594.gamevalut_server.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class OnlineFriendNotificationService {

    @Autowired
    CustomUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;

    public OnlineFriendNotificationService() {
    }

    public void subscribe(Integer userId, String url){
        CustomUser user = userService.getUserById(userId);

        if(redisService.checkIfHashExist(userId.toString())){
            throw new DataIntegrityViolationException("User is already online!");
        }

        redisService.saveToRedis(user.getId().toString(), "username",user.getUsername());
        redisService.saveToRedis(user.getId().toString(), "icon", "nesto random");
        redisService.saveToRedis(user.getId().toString(), "url", url);

        this.notifyAllFriends(userId, url);

    }

    public void unsubscribe(Integer userId){
        CustomUser user = userService.getUserById(userId);

        redisService.deleteFromRedis(userId.toString());
    }

    public void notifyAllFriends(Integer userId, String url){
        CustomUser user = userService.getUserById(userId);

        List<CustomUser> friends =  userService.getAllFriends(userId);

        if(!friends.isEmpty()){
            friends.forEach(friend->{
                if(redisService.checkIfHashExist(friend.getId().toString())){
                    this.sendRequest(user, redisService.getFromRedis(friend.getId().toString(),"url").toString());
                }
            });
        }

    }

    private void sendRequest(CustomUser user, String url){
        FriendDTO friendDTO = new FriendDTO(user.getId(), user.getUsername(), user.getImageUrl(), null,null);

        // Postavljanje zaglavlja i telo zahteva
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<FriendDTO> entity = new HttpEntity<>(friendDTO, headers);

        // Slanje POST zahteva
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

    }
}
