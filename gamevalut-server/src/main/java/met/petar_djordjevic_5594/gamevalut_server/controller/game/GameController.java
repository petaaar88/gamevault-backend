package met.petar_djordjevic_5594.gamevalut_server.controller.game;

import jakarta.validation.Valid;
import met.petar_djordjevic_5594.gamevalut_server.exception.PaginationException;
import met.petar_djordjevic_5594.gamevalut_server.exception.ResourceNotFoundException;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendDTO;
import met.petar_djordjevic_5594.gamevalut_server.model.game.*;
import met.petar_djordjevic_5594.gamevalut_server.model.pagination.Pages;
import met.petar_djordjevic_5594.gamevalut_server.service.game.GameService;
import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    GameService gameService;

    public GameController() {
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Pages getAll(@RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "limit", defaultValue = "6") Integer limit,
                        @RequestParam(name = "title", defaultValue = "") String title) {
        return gameService.getAll(page, limit, title);
    }

    @GetMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public Pages getAllReviewsForGame(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(name = "limit", defaultValue = "5") Integer limit, @PathVariable("id") Integer gameId) {
        return gameService.getAllReviewsForGame(gameId, page, limit);
    }

    @GetMapping("/{id}/pp-images")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<GameProductPageImage>> getAllProductPageImages(@PathVariable("id") Integer gameId) {
        Map<String, List<GameProductPageImage>> images = new HashMap<>();
        images.put("images", gameService.getProductPageImages(gameId));
        return images;
    }

    @GetMapping("/{id}/description")
    @ResponseStatus(HttpStatus.OK)
    public GameDescriptionDTO getGameDescription(@PathVariable("id") Integer gameId) {
        return gameService.getDescription(gameId);
    }

    @GetMapping("/{id}/download")
    @ResponseStatus(HttpStatus.OK)
    public String getDownloadURL(@PathVariable("id") Integer gameId) {
        return gameService.getDownloadURL(gameId);
    }

    @GetMapping("/{userId}/{gameId}/has-game")
    @ResponseStatus(HttpStatus.OK)
    public boolean hasGame(@PathVariable("userId") Integer userId, @PathVariable("gameId") Integer gameId) {
        return gameService.doesUseHaveGame(userId, gameId);
    }

    @GetMapping("/{gameId}/{userId}/has-review")
    @ResponseStatus(HttpStatus.OK)
    public boolean hasReview(@PathVariable("gameId") Integer gameId, @PathVariable("userId") Integer userId) {
        return gameService.doesUserHaveReview(gameId, userId);
    }

    @GetMapping("/{gameId}/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<FriendDTO> getFriendThatOwnGame(@PathVariable("gameId") Integer gameId, @PathVariable("userId") Integer userId) {

        //TODO: Proveri da li radi
        return gameService.getFriendsThatOwnGame(gameId, userId);
    }

    @GetMapping("/{id}/system-requirements")
    @ResponseStatus(HttpStatus.OK)
    public GameSystemRequirementsDTO getSystemRequirements(@PathVariable("id") Integer gameId) {
        return gameService.getSystemRequirementsForGame(gameId);
    }

    @GetMapping("/{id}/overal-rating")
    @ResponseStatus(HttpStatus.OK)
    public GameOverallRatingDTO getOverallRating(@PathVariable("id") Integer gameId) {
        return gameService.getOverallRating(gameId);
    }

    @GetMapping("/collection/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GameInUserCollectionDTO> getUsersGameCollection(@PathVariable("userId") Integer userId) {
        return gameService.getUsersGameCollection(userId);
    }


    @GetMapping("/collection/{userId}/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public GameInUserCollectionDetailsDTO getSingleGameInCollection(@PathVariable("userId") Integer userId, @PathVariable("gameId") Integer gameId) {
        return gameService.getSingleGameInCollection(userId, gameId);
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

    @PostMapping("/publish/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void publishGame(@PathVariable("gameId") Integer gameId){
        gameService.publishGame(gameId);
    }

    @PostMapping("/{gameId}/system-requirements")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSystemRequirements(@PathVariable("gameId") Integer gameId, @RequestParam(name = "type", defaultValue = "recommended") String type, @Valid @RequestBody NewGameSystemRequirementsDTO newGameSystemRequirementsDTO) {
        gameService.addSystemRequirements(gameId, type, newGameSystemRequirementsDTO);
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

    @PatchMapping("/collection/{userId}/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserGamePlaytime(@PathVariable("gameId") Integer gameId, @PathVariable("userId") Integer userId, @Valid @RequestBody UpdateUserGamePlaytimeDTO updateUserGamePlaytimeDTO){
        gameService.updateUserGamePlaytime(gameId, userId, updateUserGamePlaytimeDTO);
    }

    @GetMapping("/{userId}/{gameId}/has-friends-that-own-game")
    @ResponseStatus(HttpStatus.OK)
    public boolean hasFriendsThatOwnGame(@PathVariable("userId") Integer userId, @PathVariable("gameId") Integer gameId) {
        return gameService.hasFriendsThatOwnGame(gameId, userId);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handelHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", ex.getMessage());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(PaginationException.class)
    public ResponseEntity<Map<String, String>> handelPaginationException(PaginationException ex) {

        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", ex.getMessage());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

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
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
