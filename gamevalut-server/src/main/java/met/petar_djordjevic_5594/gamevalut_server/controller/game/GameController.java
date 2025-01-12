package met.petar_djordjevic_5594.gamevalut_server.controller.game;

import jakarta.validation.Valid;
import met.petar_djordjevic_5594.gamevalut_server.exception.ResourceNotFoundException;
import met.petar_djordjevic_5594.gamevalut_server.model.game.*;
import met.petar_djordjevic_5594.gamevalut_server.service.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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

        gameService.addImage(gameId, newGameImageDTO);

    }

    @PostMapping("/{gameId}/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addGameToUserCollection(@PathVariable("gameId") Integer gameId, @PathVariable("userId") Integer userId) {
        gameService.addGameToUserCollection(userId, gameId);
    }

    @PostMapping("/{gameId}/{userId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReview(@PathVariable("gameId") Integer gameId, @PathVariable("userId") Integer userId, @Valid @RequestBody NewGameReviewDTO newGameReviewDTO) {
        gameService.addReview(userId, gameId, newGameReviewDTO);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex){
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex){
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }



}
