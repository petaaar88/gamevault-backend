package met.petar_djordjevic_5594.gamevalut_server.repository.game;

import met.petar_djordjevic_5594.gamevalut_server.model.game.GameReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGameReviewRepository extends JpaRepository<GameReview, Integer> {
}
