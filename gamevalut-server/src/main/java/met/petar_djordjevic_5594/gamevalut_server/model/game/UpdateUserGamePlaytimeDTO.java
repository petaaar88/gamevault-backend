package met.petar_djordjevic_5594.gamevalut_server.model.game;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;
import java.time.LocalDateTime;

public record UpdateUserGamePlaytimeDTO(
        @NotNull(message = "Time played not provided!")
        BigInteger timePlayed,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "Last played at not provided!")
        LocalDateTime lastPlayedAt
) {
}
