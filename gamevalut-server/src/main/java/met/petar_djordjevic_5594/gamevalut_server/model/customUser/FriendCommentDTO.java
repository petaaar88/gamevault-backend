package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import java.time.LocalDate;

public record FriendCommentDTO(
        Integer id,
        FriendDTO user,
        String content,
        LocalDate addedAt
) {
}
