package met.petar_djordjevic_5594.gamevalut_server.model.game;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentPlayedGameDTO {
    private String image;
    private String title;
    private String playtime;
    private String lastPlayedAt;

    public RecentPlayedGameDTO(String image, String title, String playtime, String lastPlayedAt) {
        this.image = image;
        this.title = title;
        this.playtime = playtime;
        this.lastPlayedAt = lastPlayedAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public String getLastPlayedAt() {
        return lastPlayedAt;
    }

    public void setLastPlayedAt(String lastPlayedAt) {
        this.lastPlayedAt = lastPlayedAt;
    }

}
