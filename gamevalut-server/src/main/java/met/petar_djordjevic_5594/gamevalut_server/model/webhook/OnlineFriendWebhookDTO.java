package met.petar_djordjevic_5594.gamevalut_server.model.webhook;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OnlineFriendWebhookDTO(
        @NotNull(message = "User id not proviede!")
        Integer id,
        @NotBlank(message = "Username not provided!")
        String username,
        @NotBlank(message = "Icon not provided!")
        String icon,
        @NotBlank(message = "Url not provided!")
        String url
) {
}
