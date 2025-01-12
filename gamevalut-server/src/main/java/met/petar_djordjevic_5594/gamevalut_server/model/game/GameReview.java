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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_games_id", referencedColumnName = "id")
    private AcquiredGameCopy acquiredGameCopy;

    public GameReview() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public GameRating getRating() {
        return rating;
    }

    public void setRating(GameRating rating) {
        this.rating = rating;
    }

    public LocalDate getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDate postedAt) {
        this.postedAt = postedAt;
    }

    public AcquiredGameCopy getAcquiredGameCopy() {
        return acquiredGameCopy;
    }

    public void setAcquiredGameCopy(AcquiredGameCopy acquiredGameCopy) {
        this.acquiredGameCopy = acquiredGameCopy;
    }
}
