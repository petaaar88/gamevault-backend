package met.petar_djordjevic_5594.gamevalut_server.repository.game;

import met.petar_djordjevic_5594.gamevalut_server.model.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IGameRepository extends JpaRepository<Game, Integer> {

    @Query(value = "SELECT * FROM game WHERE game.title LIKE :title", nativeQuery = true)
    Optional<Game> findByTitle(@Param("title") String title);
}
