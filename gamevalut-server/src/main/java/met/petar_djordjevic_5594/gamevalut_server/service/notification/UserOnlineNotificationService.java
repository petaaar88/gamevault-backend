package met.petar_djordjevic_5594.gamevalut_server.service.notification;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendDTO;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOnlineNotificationService {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public UserOnlineNotificationService() {
    }

    public void notifyOnlineFriends(CustomUser user, List<CustomUser> onlineFriends){
        onlineFriends.forEach(friend ->{
            messagingTemplate.convertAndSend("/user-online-notification/"+friend.getId().toString(),new FriendDTO(user.getId(), user.getUsername(), user.getImageUrl(), null,null));
        });
    }

}
