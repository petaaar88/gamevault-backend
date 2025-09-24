package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.persistence.*;

@Entity(name = "game_image")
public class GameImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "type",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private GameImageType type;
    @Column(
            name = "url",
            nullable = false
    )
    private String url;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public GameImage() {
    }

    public GameImage(GameImageType type, String url) {
        this.type = type;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GameImageType getType() {
        return type;
    }

    public void setType(GameImageType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameImageDTO toDTO() {
        return new GameImageDTO(this.id, this.type, this.url);
    }
}
