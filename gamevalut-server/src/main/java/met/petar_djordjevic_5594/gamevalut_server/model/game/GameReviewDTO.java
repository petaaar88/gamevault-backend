package met.petar_djordjevic_5594.gamevalut_server.model.game;

import java.util.Map;

public record GameReviewDTO(
        String content,
        String rating,
        String postedOn,
        Map<String, String> user
){
}
