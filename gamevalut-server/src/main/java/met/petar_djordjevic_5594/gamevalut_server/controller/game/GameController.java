package met.petar_djordjevic_5594.gamevalut_server.controller.game;

import met.petar_djordjevic_5594.gamevalut_server.service.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @Autowired
    GameService gameService;

    public GameController() {
    }

    @PostMapping("/genre")
    private void create(){
        gameService.addGenre();
    }

    @PostMapping("/addGames")
    private void createAt(){
        gameService.addGame();
    }
}
