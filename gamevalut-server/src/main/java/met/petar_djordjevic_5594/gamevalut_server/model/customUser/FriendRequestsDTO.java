package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import java.util.List;

public record FriendRequestsDTO(
        List<SingleFriendRequestDTO> received,
        List<SingleFriendRequestDTO> sent
) {
}
