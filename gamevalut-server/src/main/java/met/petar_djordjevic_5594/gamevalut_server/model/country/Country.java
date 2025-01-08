package met.petar_djordjevic_5594.gamevalut_server.model.country;

import jakarta.persistence.*;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;

import java.util.List;

@Entity(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "name",
            nullable = false,
            unique = true
    )
    private String name;
    @Column(
            name = "code",
            nullable = false,
            unique = true
    )
    private String code;
    @Column(
            name = "flag_url",
            nullable = false
    )
    private String flagUrl;
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustomUser> users;

    public Country() {
    }

    public Country(String name, String countryCode, String flagUrl) {
        this.name = name;
        this.code = countryCode;
        this.flagUrl = flagUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public List<CustomUser> getUsers() {
        return users;
    }

    public void setUsers(List<CustomUser> users) {
        this.users = users;
    }

}
