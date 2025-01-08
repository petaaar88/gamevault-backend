package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "users_games")
public class AcquiredGameCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "acquisition_date",
            nullable = false
    )
    private LocalDate acquisitionDate;
    @Column(
            name = "last_played_at",
            nullable = false
    )
    private LocalDateTime lastPlayedAt;
    @Column(
            name = "time_played",
            nullable = false
    )
    private BigInteger timePlayed;

    public AcquiredGameCopy() {
    }
}
