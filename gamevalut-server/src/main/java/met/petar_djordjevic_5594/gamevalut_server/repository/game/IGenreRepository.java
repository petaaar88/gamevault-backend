package met.petar_djordjevic_5594.gamevalut_server.repository.game;

import met.petar_djordjevic_5594.gamevalut_server.model.game.Game;
import met.petar_djordjevic_5594.gamevalut_server.model.game.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IGenreRepository extends JpaRepository<Genre, Integer> {

    @Query(value = "SELECT * FROM genre WHERE genre.name LIKE :name", nativeQuery = true)
    Optional<Genre> findByName(@Param("name") String name);
}
