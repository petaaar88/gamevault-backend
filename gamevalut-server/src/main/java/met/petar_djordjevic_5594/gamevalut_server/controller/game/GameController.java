package met.petar_djordjevic_5594.gamevalut_server.controller.game;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import met.petar_djordjevic_5594.gamevalut_server.model.game.*;
import met.petar_djordjevic_5594.gamevalut_server.service.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    GameService gameService;

    public GameController() {
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createGame(@Valid @RequestBody NewGameDTO newGameDTO) {
        gameService.addGame(gameService.convertToEntity(newGameDTO));
    }

    @PostMapping("/genres")
    @ResponseStatus(HttpStatus.CREATED)
    public void createGenre(@Valid @RequestBody NewGenreDTO newGenreDTO) {
        gameService.addGenre(new Genre(newGenreDTO.name()));
    }

    @PostMapping("/{gameId}/genres/{genreId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addGenreToGame(@PathVariable("gameId") Integer gameId, @PathVariable("genreId") Integer genreId) {
        gameService.addGenreToGame(gameId, genreId);
    }

    @PostMapping("/{gameId}/system-requirements")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSystemRequirements(@PathVariable("gameId") Integer gameId, @RequestParam("type") String type, @Valid @RequestBody GameSystemRequirementsDTO gameSystemRequirementsDTO) {
        gameService.addSystemRequirements(gameId, type, gameSystemRequirementsDTO);
    }

    @PostMapping("/{gameId}/image")
    @ResponseStatus(HttpStatus.CREATED)
    public void addImage(@PathVariable("gameId") Integer gameId, @Valid @RequestBody NewGameImageDTO newGameImageDTO) {

        gameService.addImage(gameId,newGameImageDTO);

    }


}
