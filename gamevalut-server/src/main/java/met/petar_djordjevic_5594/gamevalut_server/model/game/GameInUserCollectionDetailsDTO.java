package met.petar_djordjevic_5594.gamevalut_server.model.game;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendDTO;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record GameInUserCollectionDetailsDTO(
        Integer id,
        BigInteger playTime,
        LocalDateTime lastPlayed,
        String title,
        String description,
        String image,
        List<FriendDTO> friends
) {
}
