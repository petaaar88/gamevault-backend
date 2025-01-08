package met.petar_djordjevic_5594.gamevalut_server.service.game;

import met.petar_djordjevic_5594.gamevalut_server.model.game.Game;
import met.petar_djordjevic_5594.gamevalut_server.model.game.GameRating;
import met.petar_djordjevic_5594.gamevalut_server.model.game.Genre;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGameRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGameSystemRequirementsRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.game.IGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    IGameSystemRequirementsRepository gameSystemRequirementsRepository;
    @Autowired
    IGenreRepository genreRepository;
    @Autowired
    IGameRepository gameRepository;

    public GameService() {
    }

    public void addGenre(){
        genreRepository.save(new Genre("Action"));
    }

    public void addGame(){
        Optional<Genre> genre = genreRepository.findById(1);

        Game game = new Game(LocalDate.now(),"Najjjaka igra", "dasdfasf","htttp://adsf", BigInteger.ONE,BigInteger.TEN, GameRating.Positive,12,LocalDate.now(),"najjke");
        gameRepository.save(game);
        genre.get().getGames().add(game);

        genreRepository.save(new Genre("Action"));
    }
}
