package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.persistence.*;
import met.petar_djordjevic_5594.gamevalut_server.model.game.Game;

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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser user;

    public AcquiredGameCopy() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public LocalDateTime getLastPlayedAt() {
        return lastPlayedAt;
    }

    public void setLastPlayedAt(LocalDateTime lastPlayedAt) {
        this.lastPlayedAt = lastPlayedAt;
    }

    public BigInteger getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(BigInteger timePlayed) {
        this.timePlayed = timePlayed;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }
}
