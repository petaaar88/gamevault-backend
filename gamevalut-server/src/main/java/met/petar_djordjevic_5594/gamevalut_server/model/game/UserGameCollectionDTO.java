package met.petar_djordjevic_5594.gamevalut_server.model.game;

import java.util.List;

public class UserGameCollectionDTO {
    private List<SingleGameInCollectionDTO> games;

    public UserGameCollectionDTO(List<SingleGameInCollectionDTO> games) {
        this.games = games;
    }

    public List<SingleGameInCollectionDTO> getGames() {
        return games;
    }

    public void setGames(List<SingleGameInCollectionDTO> games) {
        this.games = games;
    }
}


