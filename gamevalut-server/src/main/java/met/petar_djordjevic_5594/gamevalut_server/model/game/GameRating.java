package met.petar_djordjevic_5594.gamevalut_server.model.game;

public enum GameRating {
    Mostly_Positive("Mostly Postive"),
    Positive("Positive"),
    Mixed("Mixed"),
    Negative("Negative"),
    Mostly_Negative("Mostly Negative");

    private final String value;

    GameRating(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
