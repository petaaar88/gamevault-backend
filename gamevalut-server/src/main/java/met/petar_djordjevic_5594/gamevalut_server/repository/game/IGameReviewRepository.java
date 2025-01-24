package met.petar_djordjevic_5594.gamevalut_server.repository.game;

import met.petar_djordjevic_5594.gamevalut_server.model.game.GameReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IGameReviewRepository extends JpaRepository<GameReview, Integer> {
    @Query(value = "SELECT COUNT(game_review.id) FROM game_review JOIN users_games ON users_games.id = game_review.users_games_id WHERE users_games.game_id = :gameId", nativeQuery = true)
    Long countReviewsByGameId(@Param("gameId") Integer gameId);
    @Query(value = "SELECT game_review.* FROM game_review JOIN users_games ON users_games.id = game_review.users_games_id WHERE users_games.game_id = :gameId LIMIT :limit OFFSET :offset", nativeQuery = true)
    Optional<List<GameReview>> findByGameIdAndPaginate(@Param("gameId") Integer gameId, @Param("limit") Integer limit, @Param("offset") Integer offset);
}
