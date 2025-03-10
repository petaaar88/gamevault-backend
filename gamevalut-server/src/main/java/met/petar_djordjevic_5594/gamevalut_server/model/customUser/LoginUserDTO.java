package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginUserDTO(
        @NotNull(message = "Username not provided!")
        @NotBlank(message = "Username not provided!")
        String username,
        @NotBlank(message = "Password not provided!")
        String password
) {
}
