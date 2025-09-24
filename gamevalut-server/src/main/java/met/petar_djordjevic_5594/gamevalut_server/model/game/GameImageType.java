package met.petar_djordjevic_5594.gamevalut_server.model.game;

public enum GameImageType {
    Catalog("Catalog"),
    Product_Page("Product_Page"),
    Icon("Icon"),
    Library("Library");

    private final String displayName;

    GameImageType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName; // opcionalno, za lepši prikaz
    }
}
