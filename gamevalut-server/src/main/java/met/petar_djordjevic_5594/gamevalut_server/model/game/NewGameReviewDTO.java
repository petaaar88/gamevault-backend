package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewGameReviewDTO(
        @NotNull(message = "Rating not provided!")
        @NotBlank(message = "Rating not provided!")
        String rating,
        @NotBlank(message = "Content not provided!")
        String content
) {
}
