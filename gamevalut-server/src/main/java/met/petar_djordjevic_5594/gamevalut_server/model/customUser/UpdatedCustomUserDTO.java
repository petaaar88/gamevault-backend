package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class UpdatedCustomUserDTO {
    @Pattern(
            regexp = "^(?=(.*[a-zA-Z]){2})[^\\s/,:*%@^()\\\\,;\"'={}^`$]{2,20}$",
            message = "Invalid username!"
    )
    private String username;
    @Size( max = 420,min = 0, message = "Description must be at most 420 characters long!")
    private String description;
    private MultipartFile profileImage;  // MultipartFile može biti null ako korisnik ne uploaduje fajl

    // Getteri i setteri
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }
}
