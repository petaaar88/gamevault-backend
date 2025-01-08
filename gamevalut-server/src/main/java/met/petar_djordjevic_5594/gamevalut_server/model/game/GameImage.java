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
    private GameImageType type;
    @Column(
            name = "url",
            nullable = false
    )
    private String url;

    public GameImage() {
    }
}
