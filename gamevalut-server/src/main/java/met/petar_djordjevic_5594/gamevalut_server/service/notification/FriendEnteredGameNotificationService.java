package met.petar_djordjevic_5594.gamevalut_server.service.notification;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendDTO;
import met.petar_djordjevic_5594.gamevalut_server.model.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendEnteredGameNotificationService {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public FriendEnteredGameNotificationService() {
    }

    public void notifyOnlineFriends(CustomUser user, List<CustomUser> onlineFriends, Game game){
        onlineFriends.forEach(friend ->{
            messagingTemplate.convertAndSend("/friend-entered-game-notification/"+friend.getId().toString(),new FriendDTO(user.getId(), user.getUsername(), user.getImageUrl(), null,game.getTitle()));
        });
    }
}
