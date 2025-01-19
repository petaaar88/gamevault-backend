package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "users_friends")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "added_at",
            nullable = false
    )
    private LocalDate added_at;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser user1;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id1", nullable = false)
    private CustomUser user2;


    public Friendship() {
    }

    public Friendship(LocalDate added_at, CustomUser user1, CustomUser user2) {

        this.added_at = added_at;
        this.user1 = user1;
        this.user2 = user2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getAdded_at() {
        return added_at;
    }

    public void setAdded_at(LocalDate added_at) {
        this.added_at = added_at;
    }

    public CustomUser getUser1() {
        return user1;
    }

    public void setUser1(CustomUser user1) {
        this.user1 = user1;
    }

    public CustomUser getUser2() {
        return user2;
    }

    public void setUser2(CustomUser user2) {
        this.user2 = user2;
    }


}
