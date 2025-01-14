package met.petar_djordjevic_5594.gamevalut_server.model.game;

public class SingleGameInCollectionDTO {
    private Integer id;
    private String title;
    private String icon;

    public SingleGameInCollectionDTO(Integer id, String title, String icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}