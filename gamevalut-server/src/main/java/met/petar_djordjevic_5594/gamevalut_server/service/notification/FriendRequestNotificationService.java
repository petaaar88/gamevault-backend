package met.petar_djordjevic_5594.gamevalut_server.service.notification;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class FriendRequestNotificationService {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public FriendRequestNotificationService() {
    }

    public void notifyOnlineFriends(CustomUser user, CustomUser friend){
        messagingTemplate.convertAndSend("/friend-request-notification/"+user.getId().toString(),new FriendDTO(friend.getId(), friend.getUsername(), friend.getImageUrl(), null,null));

    }
}
