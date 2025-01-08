package met.petar_djordjevic_5594.gamevalut_server.repository.game;

import met.petar_djordjevic_5594.gamevalut_server.model.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGameRepository extends JpaRepository<Game, Integer> {
}
