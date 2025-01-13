package met.petar_djordjevic_5594.gamevalut_server.model.game;

import java.util.Map;

public record GameSystemRequirementsDTO
        (Map<String, NewGameSystemRequirementsDTO> requirements) {
}
