package met.petar_djordjevic_5594.gamevalut_server.repository.game;

import met.petar_djordjevic_5594.gamevalut_server.model.game.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGenreRepository extends JpaRepository<Genre, Integer> {
}
