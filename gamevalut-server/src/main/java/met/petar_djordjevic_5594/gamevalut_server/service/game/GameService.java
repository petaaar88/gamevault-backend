package met.petar_djordjevic_5594.gamevalut_server.service.game;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.AcquiredGameCopy;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.NewCustomUserDTO;
import met.petar_djordjevic_5594.gamevalut_server.model.game.*;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.ICustomUserRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGameRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGameSystemRequirementsRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    IGameSystemRequirementsRepository gameSystemRequirementsRepository;
    @Autowired
    IGenreRepository genreRepository;
    @Autowired
    IGameRepository gameRepository;
    @Autowired
    ICustomUserRepository userRepository;

    public GameService() {
    }

    public void addGenre(Genre newGenre) {
        genreRepository.save(newGenre);
    }

    public void addGame(Game newGame) {

        newGame.setNumberOfAcquisitions(BigInteger.ZERO);
        newGame.setNumberOfReviews(BigInteger.ZERO);
        gameRepository.save(newGame);
    }

    public void addGenreToGame(Integer gameId, Integer genreId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        Optional<Genre> optionalGenre = genreRepository.findById(genreId);

        if (optionalGame.isEmpty() || optionalGenre.isEmpty()) {
            //TODO: Logika za exceptoin
        }

        if (optionalGenre.get().getGames().stream().anyMatch(game -> game == optionalGame.get())) {
            //TODO: logika kada postoji vec taj zanr
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
            //TODO:uradi exceptoin
            //ovo sam stavio da bi radilo, obrisi ga
            systemRequirementsType = GameSystemRequirementsType.Recommended;
        }


        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isEmpty()) {
            //TODO: Logika za exceptoin
        }

        if (optionalGame.get().getSystemRequirements().stream().anyMatch(systemRequirements -> systemRequirements.getType() == systemRequirementsType)) {
            //TODO: bazi exception
        }

        GameSystemRequirements gameSystemRequirements = this.convertSystemRequirementsToEntity(gameSystemRequirementsDTO, systemRequirementsType);

        gameSystemRequirements.setGame(optionalGame.get());

        optionalGame.get().getSystemRequirements().add(gameSystemRequirements);

        gameRepository.save(optionalGame.get());

    }

    public void addImage(Integer gameId, NewGameImageDTO newGameImageDTO) {

        GameImageType imageType;

        if (newGameImageDTO.type().equalsIgnoreCase("Catalog")){
            imageType = GameImageType.Catalog;
        }
        else if (newGameImageDTO.type().equalsIgnoreCase("Product_Page")) {
            imageType = GameImageType.Product_Page;
        } else if (newGameImageDTO.type().equalsIgnoreCase("Icon")) {
            imageType = GameImageType.Icon;
        } else if (newGameImageDTO.type().equalsIgnoreCase("Library")) {
            imageType = GameImageType.Library;
        } else {

            //TODO: logika za
            //obrisi ovo
            imageType = GameImageType.Icon;
        }

        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isEmpty()) {
            //TODO: Logika za exceptoin
        }

        GameImage gameImage = new GameImage(imageType, newGameImageDTO.url());

        gameImage.setGame(optionalGame.get());

        optionalGame.get().getImages().add(gameImage);

        gameRepository.save(optionalGame.get());

    }

    public void addGameToUserCollection(Integer userId, Integer gameId){
        Optional<CustomUser> optionalUser = userRepository.findById(userId);
        Optional<Game> optionalGame = gameRepository.findById(userId);

        if(optionalGame.isEmpty()){
            //TODO: uraditi exception za nevalidan id igre
        }

        if(optionalUser.isEmpty()){
            //TODO: uraditi exception za nevalidan id korisnika

        }

        //TODO: Dodaj logiku koja zabranjuje da se ponovo doda igra u korinsikovu kolekciju

        AcquiredGameCopy acquiredGameCopy = new AcquiredGameCopy();

        acquiredGameCopy.setUser(optionalUser.get());
        acquiredGameCopy.setAcquisitionDate(LocalDate.now());
        acquiredGameCopy.setGame(optionalGame.get());
        acquiredGameCopy.setTimePlayed(BigInteger.ZERO);

        optionalGame.get().getAcquiredGameCopies().add(acquiredGameCopy);

        gameRepository.save(optionalGame.get());
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
