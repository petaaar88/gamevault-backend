package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "friend_comment")
public class FriendComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "content",
            nullable = false
    )
    private String content;
    @Column(
            name = "posted_at",
            nullable = false
    )
    private LocalDate posted_at;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "users_friends_id", nullable = false)
    private Friendship friendship;

    public FriendComment() {
    }

    public FriendComment(String content, LocalDate posted_at, Friendship friendship) {
        this.content = content;
        this.posted_at = posted_at;
        this.friendship = friendship;
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

    public LocalDate getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(LocalDate posted_at) {
        this.posted_at = posted_at;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public void setFriendship(Friendship friendship) {
        this.friendship = friendship;
    }
}
