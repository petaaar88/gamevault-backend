package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewFriendCommentDTO(
        @NotNull(message = "Comment not provided")
        @NotBlank(message = "Comment not provided")
        String content
) {
}
