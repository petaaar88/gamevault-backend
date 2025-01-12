package met.petar_djordjevic_5594.gamevalut_server.service.game;

import met.petar_djordjevic_5594.gamevalut_server.exception.ResourceNotFoundException;
import met.petar_djordjevic_5594.gamevalut_server.model.game.AcquiredGameCopy;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.game.*;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IAcquiredGameCopyRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.ICustomUserRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGameRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGameReviewRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGameSystemRequirementsRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    IAcquiredGameCopyRepository acquiredGameCopyRepository;
    @Autowired
    IGenreRepository genreRepository;
    @Autowired
    IGameRepository gameRepository;
    @Autowired
    ICustomUserRepository userRepository;
    @Autowired
    IGameReviewRepository gameReviewRepository;


    public GameService() {
    }

    public void addGenre(Genre newGenre) {

        Optional<Genre> optionalGenre = genreRepository.findByName(newGenre.getName());

        if(optionalGenre.isPresent())
            throw new DataIntegrityViolationException("Genre already exists");

        genreRepository.save(newGenre);
    }

    public void addGame(Game newGame) {

        Optional<Game> optionalGame = gameRepository.findByTitle(newGame.getTitle());

        if (optionalGame.isPresent())
            throw new DataIntegrityViolationException("Game with this title already exists!");

        newGame.setNumberOfAcquisitions(BigInteger.ZERO);
        newGame.setNumberOfReviews(BigInteger.ZERO);
        gameRepository.save(newGame);
    }

    public void addGenreToGame(Integer gameId, Integer genreId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        Optional<Genre> optionalGenre = genreRepository.findById(genreId);

        if (optionalGame.isEmpty() || optionalGenre.isEmpty()) {
            throw new ResourceNotFoundException("Game or Genre not found");
        }

        if (optionalGenre.get().getGames().stream().anyMatch(game -> game == optionalGame.get())) {
            throw new DataIntegrityViolationException("This genre is already associated with the game");
        }

        optionalGenre.get().getGames().add(optionalGame.get());

        genreRepository.save(optionalGenre.get());
    }

    public void addSystemRequirements(Integer gameId, String type, GameSystemRequirementsDTO gameSystemRequirementsDTO) {
        GameSystemRequirementsType systemRequirementsType;

        if (type.equalsIgnoreCase("minimum"))
            systemRequirementsType = GameSystemRequirementsType.Minimum;
        else if (type.equalsIgnoreCase("recommended"))
            systemRequirementsType = GameSystemRequirementsType.Recommended;
        else {
            throw new NoSuchElementException("Genre type not found!");
        }


        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isEmpty()) {
            throw new NoSuchElementException("Game not found!");
        }

        if (optionalGame.get().getSystemRequirements().stream().anyMatch(systemRequirements -> systemRequirements.getType() == systemRequirementsType)) {
            throw new DataIntegrityViolationException("This system requirements type already assoiciated with this game ");
        }

        GameSystemRequirements gameSystemRequirements = this.convertSystemRequirementsToEntity(gameSystemRequirementsDTO, systemRequirementsType);

        gameSystemRequirements.setGame(optionalGame.get());

        optionalGame.get().getSystemRequirements().add(gameSystemRequirements);

        gameRepository.save(optionalGame.get());

    }

    public void addImage(Integer gameId, NewGameImageDTO newGameImageDTO) {

        GameImageType imageType;

        if (newGameImageDTO.type().equalsIgnoreCase("Catalog")) {
            imageType = GameImageType.Catalog;
        } else if (newGameImageDTO.type().equalsIgnoreCase("Product_Page")) {
            imageType = GameImageType.Product_Page;
        } else if (newGameImageDTO.type().equalsIgnoreCase("Icon")) {
            imageType = GameImageType.Icon;
        } else if (newGameImageDTO.type().equalsIgnoreCase("Library")) {
            imageType = GameImageType.Library;
        } else {
            throw new NoSuchElementException("Wrong image type!");
        }

        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isEmpty()) {
            throw new NoSuchElementException("Game not found!");
        }

        GameImage gameImage = new GameImage(imageType, newGameImageDTO.url());

        gameImage.setGame(optionalGame.get());

        optionalGame.get().getImages().add(gameImage);

        gameRepository.save(optionalGame.get());

    }

    public void addGameToUserCollection(Integer userId, Integer gameId) {
        Optional<CustomUser> optionalUser = userRepository.findById(userId);
        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isEmpty())
            throw new NoSuchElementException("Game not found!");


        if (optionalUser.isEmpty())
            throw new NoSuchElementException("User not found!");

        Optional<AcquiredGameCopy> optionalAcquiredGameCopy = acquiredGameCopyRepository.findGameCopyById(userId, gameId);

        if (optionalAcquiredGameCopy.isPresent())
            throw new DataIntegrityViolationException("User already have this game");


        AcquiredGameCopy acquiredGameCopy = new AcquiredGameCopy();

        acquiredGameCopy.setUser(optionalUser.get());
        acquiredGameCopy.setAcquisitionDate(LocalDate.now());
        acquiredGameCopy.setGame(optionalGame.get());
        acquiredGameCopy.setTimePlayed(BigInteger.ZERO);

        optionalGame.get().getAcquiredGameCopies().add(acquiredGameCopy);

        gameRepository.save(optionalGame.get());
    }

    public void addReview(Integer userId, Integer gameId, NewGameReviewDTO newGameReviewDTO) {

        GameRating gameRating;

        if (newGameReviewDTO.rating().equalsIgnoreCase("Positive")) {
            gameRating = GameRating.Positive;
        } else if (newGameReviewDTO.rating().equalsIgnoreCase("Mostly_Positive")) {
            gameRating = GameRating.Mostly_Positive;
        } else if (newGameReviewDTO.rating().equalsIgnoreCase("Mixed")) {
            gameRating = GameRating.Mixed;
        } else if (newGameReviewDTO.rating().equalsIgnoreCase("Negative")) {
            gameRating = GameRating.Negative;
        } else if (newGameReviewDTO.rating().equalsIgnoreCase("Mostly_Negative")) {
            gameRating = GameRating.Mostly_Negative;
        } else {
            throw new NoSuchElementException("Wrong rating type!");
        }

        Optional<CustomUser> optionalUser = userRepository.findById(userId);
        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isEmpty()) {
            throw new NoSuchElementException("Game not found!");
        }

        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("User not found!");
        }

        //TODO: Dodaj logiku koja zabranjuje da se ponovo doda komentar na igru
        Optional<AcquiredGameCopy> optionalAcquiredGameCopy = acquiredGameCopyRepository.findGameCopyById(userId, gameId);

        if(optionalAcquiredGameCopy.isEmpty())
            throw  new NoSuchElementException("User doesnt own the game!");

        if(optionalAcquiredGameCopy.get().getGameReview() != null)
            throw new NoSuchElementException("Already have review!");


        GameReview gameReview = new GameReview();

        gameReview.setPostedAt(LocalDate.now());
        gameReview.setRating(gameRating);
        gameReview.setAcquiredGameCopy(optionalAcquiredGameCopy.get());
        gameReview.setContent(newGameReviewDTO.content());

        gameReviewRepository.save(gameReview);

        optionalAcquiredGameCopy.get().setGameReview(gameReview);

        acquiredGameCopyRepository.save(optionalAcquiredGameCopy.get());
    }

    public Optional<Game> getById(Integer gameId) {
        return gameRepository.findById(gameId);
    }


    public Game convertToEntity(NewGameDTO newGameDTO) {
        return new Game(newGameDTO.description(), newGameDTO.developer(), newGameDTO.downloadUrl(), newGameDTO.releaseDate(), newGameDTO.title());
    }

    public GameSystemRequirements convertSystemRequirementsToEntity(GameSystemRequirementsDTO
                                                                            gameSystemRequirementsDTO, GameSystemRequirementsType type) {
        return new GameSystemRequirements(gameSystemRequirementsDTO.cpu(), gameSystemRequirementsDTO.gpu(), gameSystemRequirementsDTO.expectedStorage(), gameSystemRequirementsDTO.storage(), gameSystemRequirementsDTO.operatingSystem(), gameSystemRequirementsDTO.ram(), type);
    }


}
