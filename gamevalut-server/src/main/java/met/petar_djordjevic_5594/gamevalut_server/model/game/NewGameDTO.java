package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record NewGameDTO(
        @NotNull(message = "Title not provided!")
        @NotBlank(message = "Title not provided!")
         String title,
        @NotBlank(message = "Description not provided!")
         String description,
        @NotBlank(message = "Developer not provided!")
        String developer,
        @NotNull(message = "Release date not provided!")
        LocalDate releaseDate,
        @NotNull(message = "Game files not provided!")
        MultipartFile gameFiles
) {
}
