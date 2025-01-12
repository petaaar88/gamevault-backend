package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "game_review")
public class GameReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "content",
            nullable = false
    )
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(
            name = "rating",
            nullable = false
    )
    private GameRating rating;
    @Column(
            name = "posted_at",
            nullable = false
    )
    private LocalDate postedAt;

    public GameReview() {
    }
}
