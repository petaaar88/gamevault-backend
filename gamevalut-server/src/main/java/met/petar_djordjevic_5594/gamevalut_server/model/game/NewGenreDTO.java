package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewGenreDTO(
        @NotNull(message = "Genre not provided!")
        @NotBlank(message = "Genre not provided!")
        String name
) {
}
