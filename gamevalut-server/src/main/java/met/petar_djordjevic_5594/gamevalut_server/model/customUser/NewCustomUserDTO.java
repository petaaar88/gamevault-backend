package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;


public record NewCustomUserDTO(
        @Pattern(
                regexp = "^(?=(.*[a-zA-Z]){2})[^\\s/,:*%@^()\\\\,;\"'={}^`$]{2,20}$",
                message = "Invalid username!"
        )
        @NotNull(message = "You did not choose username!")
        String username,
        @Size(
                min = 5,
                message = "Password has to have minimum of 5 characters!"
        )
        @Size(
                max = 20,
                message = "Password has to have maximum of 20 characters!"
        )
        @NotBlank(message = "You did not choose password!")
        String password,
        MultipartFile profileImage
) {
}
