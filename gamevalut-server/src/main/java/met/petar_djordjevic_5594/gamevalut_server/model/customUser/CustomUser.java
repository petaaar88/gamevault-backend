package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

import jakarta.persistence.*;
import met.petar_djordjevic_5594.gamevalut_server.model.country.Country;
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
    @Column(name = "image_url")
    private String imageUrl;
    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDate createdAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AcquiredGameCopy> acquiredGameCopies = new ArrayList<>();

    public CustomUser() {
    }

    public CustomUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public CustomUser(String username, String password, String description, String imageUrl, LocalDate createdAt, Country country) {
        this.username = username;
        this.password = password;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.country = country;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
