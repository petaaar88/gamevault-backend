package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.persistence.*;

@Entity(name = "friend_request")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uid1", nullable = false)
    private CustomUser uid1;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uid2", nullable = false)
    private CustomUser uid2;

    public FriendRequest() {
    }

    public FriendRequest(CustomUser uid1, CustomUser uid2) {
        this.uid1 = uid1;
        this.uid2 = uid2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomUser getUid1() {
        return uid1;
    }

    public void setUid1(CustomUser uid1) {
        this.uid1 = uid1;
    }

    public CustomUser getUid2() {
        return uid2;
    }

    public void setUid2(CustomUser uid2) {
        this.uid2 = uid2;
    }


}
