package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.persistence.*;
import met.petar_djordjevic_5594.gamevalut_server.model.game.AcquiredGameCopy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "username",
            nullable = false,
            unique = true
    )
    private String username;
    @Column(
            name = "password",
            nullable = false
    )
    private String password;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private CustomUserRole role;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDate createdAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AcquiredGameCopy> acquiredGameCopies = new ArrayList<>();
    @OneToMany(mappedBy = "uid1", cascade = CascadeType.ALL)
    private List<FriendRequest> sentRequests= new ArrayList<>();
    @OneToMany(mappedBy = "uid2", cascade = CascadeType.ALL)
    private List<FriendRequest> receivedRequests= new ArrayList<>();
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private List<Friendship> userWithFriends;
    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    private List<Friendship> friendsWithUser;

    public CustomUser() {
    }

    public CustomUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = CustomUserRole.USER;
        this.createdAt = LocalDate.now();
    }

    public CustomUser(String username, String password, String description, String imageUrl, LocalDate createdAt) {
        this.username = username;
        this.password = password;
        this.description = description;
        this.imageUrl = imageUrl;
        this.role = CustomUserRole.USER;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<AcquiredGameCopy> getAcquiredGameCopies() {
        return acquiredGameCopies;
    }

    public void setAcquiredGameCopies(List<AcquiredGameCopy> acquiredGameCopies) {
        this.acquiredGameCopies = acquiredGameCopies;
    }

    public List<FriendRequest> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<FriendRequest> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public List<FriendRequest> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<FriendRequest> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public List<Friendship> getUserWithFriends() {
        return userWithFriends;
    }

    public void setUserWithFriends(List<Friendship> userWithFriends) {
        this.userWithFriends = userWithFriends;
    }

    public List<Friendship> getFriendsWithUser() {
        return friendsWithUser;
    }

    public void setFriendsWithUser(List<Friendship> friendsWithUser) {
        this.friendsWithUser = friendsWithUser;
    }

    public CustomUserRole getRole() {
        return role;
    }

    public void setRole(CustomUserRole role) {
        this.role = role;
    }
}
