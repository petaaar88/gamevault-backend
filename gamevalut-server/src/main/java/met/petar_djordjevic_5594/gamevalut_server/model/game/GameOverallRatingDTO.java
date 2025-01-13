package met.petar_djordjevic_5594.gamevalut_server.model.game;

import java.math.BigInteger;

public record GameOverallRatingDTO(String rating,
                                   BigInteger numberOfReviews) {
}
