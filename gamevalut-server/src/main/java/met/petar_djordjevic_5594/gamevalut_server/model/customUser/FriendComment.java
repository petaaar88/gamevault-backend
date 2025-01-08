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
            name = "receiver_id",
            nullable = false
    )
    private Integer receiver_id;
    @Column(
            name = "sender_id",
            nullable = false
    )
    private Integer sender_id;
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

    public FriendComment(String content, Integer receiver_id, Integer sender_id, LocalDate posted_at, Friendship friendship) {
        this.content = content;
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
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

    public Integer getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(Integer receiver_id) {
        this.receiver_id = receiver_id;
    }

    public Integer getSender_id() {
        return sender_id;
    }

    public void setSender_id(Integer sender_id) {
        this.sender_id = sender_id;
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
