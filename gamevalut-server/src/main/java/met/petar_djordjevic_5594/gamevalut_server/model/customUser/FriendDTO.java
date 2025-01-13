package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import java.math.BigInteger;

// Ovo je univerzalni DTO za prijatelje
public record FriendDTO(
        Integer id,
        String username,
        String icon,
        BigInteger hoursPlayed,
        Boolean inGame
) {
}
