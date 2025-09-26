package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record NewGameImageDTO(
        @NotNull(message = "Image type not provided!")
        @NotBlank(message = "Image type not provided!")
        String type,
        @NotNull(message = "Image not provided!")
        MultipartFile image
) {
}
