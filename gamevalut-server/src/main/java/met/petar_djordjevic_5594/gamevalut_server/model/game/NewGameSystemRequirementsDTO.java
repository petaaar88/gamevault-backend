package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewGameSystemRequirementsDTO(
        @NotNull(message = "CPU not provided!")
        @NotBlank(message = "CPU not provided!")
        String cpu,
        @NotNull(message = "GPU not provided!")
        @NotBlank(message = "GPU not provided!")
        String gpu,
        @NotNull(message = "Expected storage not provided!")
        Integer expectedStorage,
        @NotNull(message = "Storage not provided!")
        Integer storage,
        @NotNull(message = "OS not provided!")
        @NotBlank(message = "OS not provided!")
        String operatingSystem,
        @NotNull(message = "RAM not provided!")
        Integer ram
) {
}
