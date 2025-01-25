package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigInteger;

// Ovo je univerzalni DTO za prijatelje
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FriendDTO(
        Integer id,
        String username,
        String icon,
        Double hoursPlayed,
        String inGame
) {
}
