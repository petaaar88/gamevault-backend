package met.petar_djordjevic_5594.gamevalut_server.service.game;

import met.petar_djordjevic_5594.gamevalut_server.exception.ResourceNotFoundException;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendDTO;
import met.petar_djordjevic_5594.gamevalut_server.model.game.AcquiredGameCopy;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.game.*;
import met.petar_djordjevic_5594.gamevalut_server.model.pagination.Pages;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IAcquiredGameCopyRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGameRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGameReviewRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGenreRepository;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import met.petar_djordjevic_5594.gamevalut_server.utils.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;


@Service
public class GameService {

    @Autowired
    IAcquiredGameCopyRepository acquiredGameCopyRepository;
    @Autowired
    IGenreRepository genreRepository;
    @Autowired
    IGameRepository gameRepository;
    @Autowired
    IGameReviewRepository gameReviewRepository;

    @Autowired
    CustomUserService userService;


    public GameService() {
    }

    public void addGenre(Genre newGenre) {

        Optional<Genre> optionalGenre = genreRepository.findByName(newGenre.getName());

        if (optionalGenre.isPresent())
            throw new DataIntegrityViolationException("Genre already exists");

        genreRepository.save(newGenre);
    }

    public void addGame(Game newGame) {

        Optional<Game> optionalGame = gameRepository.findByTitle(newGame.getTitle());

        if (optionalGame.isPresent())
            throw new DataIntegrityViolationException("Game with this title already exists!");

        newGame.setNumberOfAcquisitions(BigInteger.ZERO);
        newGame.setNumberOfReviews(BigInteger.ZERO);
        newGame.setDeploymentDate(LocalDate.now());

        gameRepository.save(newGame);
    }

    public void addGenreToGame(Integer gameId, Integer genreId) {
        Game game = this.getGameById(gameId);
        Optional<Genre> optionalGenre = genreRepository.findById(genreId);

        if (optionalGenre.isEmpty()) {
            throw new ResourceNotFoundException("Genre not found");
        }

        if (optionalGenre.get().getGames().stream().anyMatch(game1 -> game1 == game)) {
            throw new DataIntegrityViolationException("This genre is already associated with the game");
        }

        optionalGenre.get().getGames().add(game);

        genreRepository.save(optionalGenre.get());
    }

    public void addSystemRequirements(Integer gameId, String type, NewGameSystemRequirementsDTO newGameSystemRequirementsDTO) {
        GameSystemRequirementsType systemRequirementsType;

        if (type.equalsIgnoreCase("minimum"))
            systemRequirementsType = GameSystemRequirementsType.Minimum;
        else if (type.equalsIgnoreCase("recommended"))
            systemRequirementsType = GameSystemRequirementsType.Recommended;
        else {
            throw new NoSuchElementException("Genre type not found!");
        }


        Game game = this.getGameById(gameId);

        if (game.getSystemRequirements().stream().anyMatch(systemRequirements -> systemRequirements.getType() == systemRequirementsType)) {
            throw new DataIntegrityViolationException("This system requirements type already assoiciated with this game ");
        }

        GameSystemRequirements gameSystemRequirements = this.convertSystemRequirementsToEntity(newGameSystemRequirementsDTO, systemRequirementsType);

        gameSystemRequirements.setGame(game);

        game.getSystemRequirements().add(gameSystemRequirements);

        gameRepository.save(game);

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

        Game game = this.getGameById(gameId);

        GameImage gameImage = new GameImage(imageType, newGameImageDTO.url());

        gameImage.setGame(game);

        game.getImages().add(gameImage);

        gameRepository.save(game);

    }

    public void addGameToUserCollection(Integer userId, Integer gameId) {
        CustomUser user = userService.getUserById(userId);
        Game game = this.getGameById(gameId);


        Optional<AcquiredGameCopy> optionalAcquiredGameCopy = acquiredGameCopyRepository.findGameCopyById(userId, gameId);

        if (optionalAcquiredGameCopy.isPresent())
            throw new DataIntegrityViolationException("User already have this game");


        AcquiredGameCopy acquiredGameCopy = new AcquiredGameCopy();

        acquiredGameCopy.setUser(user);
        acquiredGameCopy.setAcquisitionDate(LocalDate.now());
        acquiredGameCopy.setGame(game);
        acquiredGameCopy.setTimePlayed(BigInteger.ZERO);

        game.getAcquiredGameCopies().add(acquiredGameCopy);
        game.setNumberOfAcquisitions(game.getNumberOfAcquisitions().add(BigInteger.ONE));
        gameRepository.save(game);
    }

    @Transactional
    public void addReview(Integer userId, Integer gameId, NewGameReviewDTO newGameReviewDTO) {

        GameRating gameRating;
        Double gameRatingPointPercentage;

        if (newGameReviewDTO.rating().equalsIgnoreCase("Positive")) {
            gameRating = GameRating.Positive;
            gameRatingPointPercentage = 75.0;
        } else if (newGameReviewDTO.rating().equalsIgnoreCase("Mostly_Positive")) {
            gameRating = GameRating.Mostly_Positive;
            gameRatingPointPercentage = 100.0;
        } else if (newGameReviewDTO.rating().equalsIgnoreCase("Mixed")) {
            gameRating = GameRating.Mixed;
            gameRatingPointPercentage = 50.0;
        } else if (newGameReviewDTO.rating().equalsIgnoreCase("Negative")) {
            gameRating = GameRating.Negative;
            gameRatingPointPercentage = 25.0;
        } else if (newGameReviewDTO.rating().equalsIgnoreCase("Mostly_Negative")) {
            gameRating = GameRating.Mostly_Negative;
            gameRatingPointPercentage = 0.0;
        } else {
            throw new NoSuchElementException("Wrong rating type!");
        }

        CustomUser user = userService.getUserById(userId);
        Game game = this.getGameById(gameId);


        Optional<AcquiredGameCopy> optionalAcquiredGameCopy = acquiredGameCopyRepository.findGameCopyById(userId, gameId);

        if (optionalAcquiredGameCopy.isEmpty())
            throw new NoSuchElementException("User doesnt own the game!");

        if (optionalAcquiredGameCopy.get().getGameReview() != null)
            throw new NoSuchElementException("Already have review!");


        GameReview gameReview = new GameReview();

        gameReview.setPostedAt(LocalDate.now());
        gameReview.setRating(gameRating);
        gameReview.setAcquiredGameCopy(optionalAcquiredGameCopy.get());
        gameReview.setContent(newGameReviewDTO.content());

        gameReviewRepository.save(gameReview);

        optionalAcquiredGameCopy.get().setGameReview(gameReview);

        acquiredGameCopyRepository.save(optionalAcquiredGameCopy.get());

        BigInteger numberOfReviews = game.getNumberOfReviews();

        GameRating overallRating = game.getOverallRating();
        Double overallPercentage = game.getOverallRatingPercentage();

        if(overallPercentage == null)
            overallPercentage = 0.0;

        Double totalGameReviewPointsPercentage = overallPercentage * numberOfReviews.doubleValue();

        totalGameReviewPointsPercentage += gameRatingPointPercentage;
        numberOfReviews =  numberOfReviews.add(BigInteger.ONE);

        Double newOverallPercentage = totalGameReviewPointsPercentage / numberOfReviews.doubleValue();

        game.setOverallRatingPercentage(newOverallPercentage);
        game.setNumberOfReviews(numberOfReviews);

        if (newOverallPercentage >= 87.5)
            game.setOverallRating(GameRating.Mostly_Positive);
        else if (newOverallPercentage >= 62.5)
            game.setOverallRating(GameRating.Positive);
        else if (newOverallPercentage >= 37.5)
            game.setOverallRating(GameRating.Positive);
        else if (newOverallPercentage >= 12.5)
            game.setOverallRating(GameRating.Positive);
        else if (newOverallPercentage < 12.5)
            game.setOverallRating(GameRating.Mostly_Negative);

        gameRepository.save(game);

    }

    public List<GameOverviewDTO> getAll() {

        List<GameOverviewDTO> games = new ArrayList<>();

        gameRepository.findAll().forEach(game -> {
            games.add(this.convertToOverviewDTO(game));
        });

        return games;
    }

    public Pages getAll(Integer page, Integer limit) {

        Paginator.validatePageAndLimit(page, limit);

        List<GameOverviewDTO> games = new ArrayList<>();

        Integer offset = (page - 1) * limit;

        gameRepository.findByFilterAndPaginate(limit, offset).get().forEach(game -> {
            games.add(this.convertToOverviewDTO(game));
        });

        return Paginator.getResoultAndPages(page, limit, gameRepository.count(), games);
    }

    public List<GameProductPageImage> getProductPageImages(Integer gameId) {

        Game game = this.getGameById(gameId);

        List<GameProductPageImage> images = new ArrayList<>();

        game.getImages().forEach(gameImage -> {
            if (gameImage.getType() == GameImageType.Product_Page)
                images.add(new GameProductPageImage(gameImage.getUrl()));
        });

        return images;
    }

    public GameDescriptionDTO getDescription(Integer gameId) {

        Game game = this.getGameById(gameId);

        List<String> genres = new ArrayList<>();

        game.getGenres().forEach(genre -> {
            genres.add(genre.getName());
        });

        return new GameDescriptionDTO(game.getTitle() ,game.getDescription(), genres, game.getDeveloper(), game.getReleaseDate().toString());
    }

    public String getDownloadURL(Integer gameId) {

        Game game = this.getGameById(gameId);

        //TODO: uradi da korisnik ne moze da preuzme igru ako je nema

        return game.getDownloadUrl();
    }

    //TODO: testiraj ovu metodu
    public List<FriendDTO> getFriendsThatOwnGame(Integer gameId, Integer userId) {

        Game game = this.getGameById(gameId);
        CustomUser user = userService.getUserById(userId);

        List<CustomUser> friends = userService.getAllFriends(userId);

        if (friends.isEmpty())
            return new ArrayList<>();

        List<FriendDTO> friendsThatOwnGame = new ArrayList<>();

        friends.forEach(friend -> {

            if (this.doesUserHaveGame(friend.getId(), gameId)) {

                AcquiredGameCopy acquiredGameCopy1 = friend.getAcquiredGameCopies().stream().filter(acquiredGameCopy -> acquiredGameCopy.getGame().getId() == gameId).findFirst().get();

                friendsThatOwnGame.add(new FriendDTO(friend.getId(), friend.getUsername(), friend.getImageUrl(), acquiredGameCopy1.getTimePlayed(), null));
            }
        });

        return friendsThatOwnGame;

    }

    public GameSystemRequirementsDTO getSystemRequirementsForGame(Integer gameId) {
        Game game = this.getGameById(gameId);

        Optional<List<GameSystemRequirements>> optionalGameSystemRequirements = gameRepository.findSystemRequirementsOfGame(gameId);

        if (optionalGameSystemRequirements.get().size() != 2)
            throw new DataIntegrityViolationException("Must be two system requirements per game!");

        GameSystemRequirements minimum = optionalGameSystemRequirements.get().stream().filter(systemRequirements -> systemRequirements.getType() == GameSystemRequirementsType.Minimum).findFirst().get();
        GameSystemRequirements recommended = optionalGameSystemRequirements.get().stream().filter(systemRequirements -> systemRequirements.getType() == GameSystemRequirementsType.Recommended).findFirst().get();

        NewGameSystemRequirementsDTO minDTO = new NewGameSystemRequirementsDTO(minimum.getCpu(), minimum.getGpu(), minimum.getExpectedStorage(), minimum.getStorage(), minimum.getOperatingSystem(), minimum.getRam());
        NewGameSystemRequirementsDTO reqDTO = new NewGameSystemRequirementsDTO(recommended.getCpu(), recommended.getGpu(), recommended.getExpectedStorage(), recommended.getStorage(), recommended.getOperatingSystem(), recommended.getRam());

        return new GameSystemRequirementsDTO(minDTO, reqDTO);

    }

    public GameOverallRatingDTO getOverallRating(Integer gameId) {
        Game game = this.getGameById(gameId);

        GameRating overallRating = game.getOverallRating();

        return new GameOverallRatingDTO(overallRating != null ? overallRating.getValue() : null, game.getNumberOfReviews());

    }

    public List<GameReviewDTO> getAllReviewsForGame(Integer gameId) {

        Game game = this.getGameById(gameId);

        Optional<List<GameReview>> optionalGameReviews = gameRepository.findAllGameReviews(gameId);

        if (optionalGameReviews.isEmpty())
            return new ArrayList<>();

        List<GameReviewDTO> gameReviewDTOS = new ArrayList<>();

        optionalGameReviews.get().forEach(gameReview -> {
            CustomUser user = gameReview.getAcquiredGameCopy().getUser();

            Map<String, String> userMap = new HashMap<>();

            userMap.put("id", user.getId().toString());
            userMap.put("username", user.getUsername());
            userMap.put("icon", user.getImageUrl());
            gameReviewDTOS.add(new GameReviewDTO(gameReview.getContent(), gameReview.getRating().getValue(), gameReview.getPostedAt().toString(), userMap));
        });

        return gameReviewDTOS;
    }

    public Pages getAllReviewsForGame(Integer gameId, Integer page, Integer limit){
        Paginator.validatePageAndLimit(page, limit);

        Game game = this.getGameById(gameId);

        List<GameReviewDTO> games = new ArrayList<>();

        Integer offset = (page - 1) * limit;

        List<GameReview> gameReviews = gameReviewRepository.findByGameIdAndPaginate(gameId,limit, offset).get();

        if(gameReviews.isEmpty())
            return Paginator.getResoultAndPages(page, limit, gameReviewRepository.countReviewsByGameId(gameId), null);

        List<GameReviewDTO> gameReviewDTOS = new ArrayList<>();


        gameReviews.forEach(gameReview -> {
            CustomUser user = gameReview.getAcquiredGameCopy().getUser();

            Map<String, String> userMap = new HashMap<>();

            userMap.put("id", user.getId().toString());
            userMap.put("username", user.getUsername());
            userMap.put("icon", user.getImageUrl());
            gameReviewDTOS.add(new GameReviewDTO(gameReview.getContent(), gameReview.getRating().getValue(), gameReview.getPostedAt().toString(), userMap));
        });

        return Paginator.getResoultAndPages(page, limit, gameReviewRepository.countReviewsByGameId(gameId), gameReviewDTOS);
    }

    public List<GameInUserCollectionDTO> getUsersGameCollection(Integer userId) {
        CustomUser user = userService.getUserById(userId);

        Optional<List<Game>> optionalUserGameCollection = gameRepository.findAllUserGameCollection(userId);

        if (optionalUserGameCollection.get().isEmpty())
            return new ArrayList<>();

        List<GameInUserCollectionDTO> usersGameCollection = new ArrayList<>();

        optionalUserGameCollection.get().forEach(game -> {

            game.getImages().forEach(gameImage -> {
                if (gameImage.getType() == GameImageType.Icon)
                    usersGameCollection.add(new GameInUserCollectionDTO(game.getId(), game.getTitle(), gameImage.getUrl()));

            });

        });

        return usersGameCollection;
    }

    public GameInUserCollectionDetailsDTO getSingleGameInCollection(Integer userId, Integer gameId) {
        CustomUser user = userService.getUserById(userId);
        Game game = this.getGameById(gameId);

        if (!this.doesUserHaveGame(user.getId(), game.getId()))
            throw new NoSuchElementException("User doesnt own this game!");

        AcquiredGameCopy acquiredGameCopy = user.getAcquiredGameCopies().stream().filter(acquiredGameCopy1 -> acquiredGameCopy1.getGame().getId() == game.getId()).findFirst().get();


        GameImage gameImage = game.getImages().stream().filter(gameImage1 -> gameImage1.getType() == GameImageType.Library).findFirst().get();


        GameInUserCollectionDetailsDTO gameInUserCollectionDetailsDTO = new GameInUserCollectionDetailsDTO(game.getId(), acquiredGameCopy.getTimePlayed(), acquiredGameCopy.getLastPlayedAt(), game.getTitle(), game.getDescription(), gameImage.getUrl(), this.getFriendsThatOwnGame(game.getId(), userId));

        return gameInUserCollectionDetailsDTO;
    }

    public boolean doesUserHaveGame(Integer userId, Integer gameId) {
        Game game = this.getGameById(gameId);
        CustomUser user = userService.getUserById(userId);

        return gameRepository.findIfUserHaveGame(gameId, userId).isPresent();
    }

    public Game getGameById(Integer gameId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isEmpty())
            throw new NoSuchElementException("Game not found!");

        return optionalGame.get();
    }


    public Game convertToEntity(NewGameDTO newGameDTO) {
        return new Game(newGameDTO.description(), newGameDTO.developer(), newGameDTO.downloadUrl(), newGameDTO.releaseDate(), newGameDTO.title());
    }

    public GameSystemRequirements convertSystemRequirementsToEntity(NewGameSystemRequirementsDTO
                                                                            newGameSystemRequirementsDTO, GameSystemRequirementsType type) {
        return new GameSystemRequirements(newGameSystemRequirementsDTO.cpu(), newGameSystemRequirementsDTO.gpu(), newGameSystemRequirementsDTO.expectedStorage(), newGameSystemRequirementsDTO.storage(), newGameSystemRequirementsDTO.operatingSystem(), newGameSystemRequirementsDTO.ram(), type);
    }

    public GameOverviewDTO convertToOverviewDTO(Game game) {
        Map<String, String> rating = new HashMap<>();
        rating.put("rating", (game.getOverallRating() != null) ? game.getOverallRating().getValue() : null);
        rating.put("rating_percentage", (game.getOverallRatingPercentage() != null) ? game.getOverallRatingPercentage().toString() : null);
        rating.put("reviews", game.getNumberOfReviews().toString());
        return new GameOverviewDTO(game.getId(), game.getTitle(), game.getNumberOfAcquisitions(), game.getDeveloper(), rating);
    }

}
