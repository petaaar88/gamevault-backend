package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import java.util.List;
import java.util.Map;

public record AllFriendsDTO(
        Map<String, List<FriendDTO>> friends
) {
}
