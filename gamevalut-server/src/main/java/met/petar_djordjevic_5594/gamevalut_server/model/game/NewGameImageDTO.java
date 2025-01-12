package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewGameImageDTO(
        @NotNull(message = "Image type not provided!")
        @NotBlank(message = "Image type not provided!")
        String type,
        @NotBlank(message = "Image url not provided")
        String url
) {
}
