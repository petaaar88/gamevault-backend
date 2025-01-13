package met.petar_djordjevic_5594.gamevalut_server.model.game;

import java.util.List;

public record GameDescriptionDTO(
        String description,
        List<String> genres,
        String developer,
        String release
) {
}
