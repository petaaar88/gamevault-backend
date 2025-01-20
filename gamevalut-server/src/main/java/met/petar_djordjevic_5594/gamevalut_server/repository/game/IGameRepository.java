package met.petar_djordjevic_5594.gamevalut_server.repository.game;

import met.petar_djordjevic_5594.gamevalut_server.model.game.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IGameRepository extends JpaRepository<Game, Integer> {

    @Query(value = "SELECT * FROM game WHERE game.title LIKE :title", nativeQuery = true)
    Optional<Game> findByTitle(@Param("title") String title);

    @Query(value = "SELECT game.* FROM game JOIN users_games ON game.id = users_games.game_id JOIN user ON  users_games.user_id = user.id WHERE users_games.user_id = :userId AND users_games.game_id = :gameId;", nativeQuery = true)
    Optional<Game> findIfUserHaveGame(@Param("gameId")Integer gameId,@Param("userId")Integer userId);

    @Query(value = "SELECT * FROM game_system_requirements WHERE game_id = :gameId", nativeQuery = true)
    Optional<List<GameSystemRequirements>> findSystemRequirementsOfGame(@Param("gameId") Integer gameId);

    @Query(value = "SELECT * FROM game_review JOIN users_games ON game_review.users_games_id = users_games.id WHERE users_games.game_id = :gameId",nativeQuery = true)
    Optional<List<GameReview>> findAllGameReviews(@Param("gameId")Integer gameId);

    @Query(value = "SELECT " +
            "    game.* " +
            " FROM " +
            "    game " +
            "JOIN " +
            " users_games " +
            "ON " +
            " users_games.game_id = game.id " +
            "WHERE " +
            "    users_games.user_id = :userId ", nativeQuery = true)
    Optional<List<Game>> findAllUserGameCollection(@Param("userId") Integer userId);

}
