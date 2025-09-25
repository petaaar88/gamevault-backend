package met.petar_djordjevic_5594.gamevalut_server.model.customUser;

public class LoginResponse {
    private String token;
    private Integer id;
    private String username;
    private String role;
    private String imageUrl;
    private String description;

    public LoginResponse(String token, Integer id, String username, String role, String imageUrl, String description) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.role = role;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getteri i setteri
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}