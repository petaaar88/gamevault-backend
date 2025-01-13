package met.petar_djordjevic_5594.gamevalut_server.model.game;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public record GameOverviewDTO(
        Integer id,
        String title,
        BigInteger acquisitions,
        String developer,
        Map<String, String> rating

) {
}
