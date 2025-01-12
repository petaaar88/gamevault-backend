package met.petar_djordjevic_5594.gamevalut_server.repository.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.game.AcquiredGameCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IAcquiredGameCopyRepository extends JpaRepository<AcquiredGameCopy, Integer> {

    @Query(value = "SELECT * FROM \n" +
            " users_games WHERE \n" +
            " users_games.user_id = :userId AND users_games.game_id = :gameId \n", nativeQuery = true)
    public Optional<AcquiredGameCopy> findGameCopyById(@Param("userId") Integer userId, @Param("gameId") Integer gameId);



}
