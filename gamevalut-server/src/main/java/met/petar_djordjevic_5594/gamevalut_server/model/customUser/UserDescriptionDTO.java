package met.petar_djordjevic_5594.gamevalut_server.model.customUser;


import java.util.Map;

public record UserDescriptionDTO(
        Integer id,
        String username,
        String icon,
        String description,
        String createdAt
) {
}
